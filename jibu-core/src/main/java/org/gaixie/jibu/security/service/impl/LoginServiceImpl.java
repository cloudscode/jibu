/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.gaixie.jibu.security.service.impl;

import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.MD5;
import org.gaixie.jibu.security.dao.TokenDAO;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.Token;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.LoginException;
import org.gaixie.jibu.security.service.LoginService;
import org.gaixie.jibu.security.service.TokenException;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统登录服务接口的默认实现。
 * <p>
 */
public class LoginServiceImpl implements LoginService {
    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    private final TokenDAO tokenDAO;
    private final UserDAO userDAO;

    @Inject public LoginServiceImpl(TokenDAO tokenDAO,UserDAO userDAO) {
        this.tokenDAO = tokenDAO;
        this.userDAO = userDAO;
    }

    public void login(String username, String password) throws LoginException {
        Connection conn = null;
        String cryptpassword = MD5.encodeString(password,null);
        try {
            conn = ConnectionUtils.getConnection();
            User user = userDAO.login(conn,username,cryptpassword);
            if (null == user) throw new LoginException("Username or password is incorrect.");
        } catch(SQLException e) {
            logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            // userDAO = null; ?
        }
    }

    public Token generateToken(String username) {
        Connection conn = null;
        Token token = null;
        try {
            conn = ConnectionUtils.getConnection();
            User user = userDAO.get(conn,username);
            if (user == null) return null;

            Random randomGenerator = new Random();
            long randomLong = randomGenerator.nextLong();

            //            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            //calendar.setTime(date);
            calendar.add(calendar.DAY_OF_MONTH, +1);
            long time = calendar.getTimeInMillis();
            Timestamp ts = new Timestamp(time);
            String key = MD5.encodeString(Long.toString(randomLong) + time, null);
            token = new Token(key,"password",ts,user.getId());

            tokenDAO.save(conn,token);
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            logger.error(e.getMessage());
            return null;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return token;
    }

    public void resetPassword(String tokenValue, String password) throws JibuException {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
            Token token = tokenDAO.get(conn,tokenValue);
            if (token == null) throw new TokenException("Token is null");

            Calendar calendar = Calendar.getInstance();
            long time = calendar.getTimeInMillis();
            Timestamp ts = new Timestamp(time);
            if(ts.after(token.getExpiration())) throw new TokenException("Token expired.");

            if (password == null || password.isEmpty()) {
                throw new JibuException("Password is invalid.");
            }

            User user = new User();
            String cryptpassword = MD5.encodeString(password,null);
            user.setPassword(cryptpassword);
            user.setId(token.getUser_id());
            userDAO.update(conn,user);
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            throw new JibuException(e.getMessage());
        }
    }
}
