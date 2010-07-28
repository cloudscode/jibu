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
package org.gaixie.jibu.security.service;

import java.sql.Connection;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.MD5;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.LoginService;
import org.gaixie.jibu.security.service.impl.LoginServiceImpl;

public class LoginServiceTest {
    private LoginService loginService;
    private UserDAO userDAO;
    private Connection conn;

    @Before public void setup() {
        userDAO = EasyMock.createMock(UserDAO.class);
        conn = EasyMock.createMock(Connection.class);
        loginService = new LoginServiceImpl(userDAO);
    }


    @Test public void testLogin() throws Exception {
        String username = "admin";
        String password = "123456";
        String cryptpassword = MD5.encodeString(password,null);
        EasyMock.expect(userDAO.login(conn,username,cryptpassword))
            .andReturn(null);

        EasyMock.replay(userDAO);
        try {
            loginService.login(conn,"admin", "123456");
        } catch (JibuException e) {
            Assert.assertTrue("001B-0001".equals(e.getMessage()));
        }
        EasyMock.verify(userDAO);
    }
}
