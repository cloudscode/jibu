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

import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.MD5;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.LoginService;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统登录服务接口实现
 */
public class LoginServiceImpl implements LoginService {
    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    private final UserDAO userDAO;

    @Inject public LoginServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    public void login(String username, String password) throws JibuException {
        Connection conn = null;
        String cryptpassword = MD5.encodeString(password,null);
        try {
            conn = ConnectionUtils.getConnection();
            User user = userDAO.login(conn,username,cryptpassword);
            if (null == user) throw new JibuException("LoginService.001");
        } catch(SQLException e) {
            logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            // userDAO = null; ?
        }
    }
}
