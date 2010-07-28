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

import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.annotation.Transaction;
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
    
    /**
     * 通过用户名及密码进行登录验证
     *
     * @param conn 一个有效的数据库链接。
     * @param username 登录用户名
     * @param password 没有进行hash的登录密码
     *
     * @exception JibuException 用户名或密码错误时抛出
     */
    @Transaction
        public void login(Connection conn, String username, String password) throws JibuException {
        String cryptpassword = MD5.encodeString(password,null);
        User user = userDAO.login(conn,username,cryptpassword);
        // 001B-0001 说明：001 为模块编号即jibu-core，B 表示一个B类错误，会在界面上有多语言提示
        // 0001为错误的编号
        if (null == user) throw new JibuException("001B-0001");
    }
}
