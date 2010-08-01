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
import org.gaixie.jibu.annotation.Transaction;
import org.gaixie.jibu.security.MD5;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.UserService;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户的查询，增加，修改，删除等服务
 */
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    @Inject public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transaction
    public User get(Connection conn,String username) throws JibuException {
        User user = null;
        user = userDAO.get(conn,username);
        return user;
    }

    /**
     * 增加一个新用户，密码不能为空，且是没有hash过的
     *
     * @param user 用户对象
     *
     * @exception JibuException 如果用户名已存在，抛出此异常
     */
    @Transaction
    public void add(Connection conn, User user) throws JibuException {
        if (user.getPassword()!=null) {
            String cryptpassword = MD5.encodeString(user.getPassword(),null);
            user.setPassword(cryptpassword);
        }
        userDAO.save(conn,user);
    }
}
