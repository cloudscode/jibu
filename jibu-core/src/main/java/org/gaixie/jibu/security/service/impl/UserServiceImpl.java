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
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.MD5;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.UserService;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User 服务接口的默认实现。
 */
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    /**
     * 使用 Guice 进行 DAO 的依赖注入。
     * <p>
     */
    @Inject public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User get(String username) {
        Connection conn = null;
        User user = null;
        try {
            conn = ConnectionUtils.getConnection();
            user = userDAO.get(conn,username);
        } catch(SQLException e) {
            logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return user;

    }


    public void add(User user) throws JibuException {
        Connection conn = null;
        if (user.getPassword()!=null) {
            String cryptpassword = MD5.encodeString(user.getPassword(),null);
            user.setPassword(cryptpassword);
        }
        try {
            conn = ConnectionUtils.getConnection();
            userDAO.save(conn,user);
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            throw new JibuException("UserService.001");
        }
    }

    public List<User> find(User user) {
        Connection conn = null;
        List<User> users= null;
        if (user.getPassword()!=null) {
            String cryptpassword = MD5.encodeString(user.getPassword(),null);
            user.setPassword(cryptpassword);
        }
        try {
            conn = ConnectionUtils.getConnection();
            users = userDAO.find(conn,user);
        } catch(SQLException e) {
            logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return users;
    }
}
