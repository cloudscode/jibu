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
import org.gaixie.jibu.security.service.UserService;
import org.gaixie.jibu.security.service.impl.UserServiceImpl;

public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private Connection conn;

    @Before public void setup() {
        userDAO = EasyMock.createMock(UserDAO.class);
        conn = EasyMock.createMock(Connection.class);
        userService = new UserServiceImpl(userDAO);
    }


    @Test public void testGet() throws Exception {
        User user = new User();
        EasyMock.expect(userDAO.get(conn,"admin"))
            .andReturn(user);
        EasyMock.replay(userDAO);
        userService.get(conn,"admin");
        EasyMock.verify(userDAO);
    }

    @Test public void testAdd() throws Exception {
        User user = new User("tommy wang","testUserServiceAdd","123456","bitorb@gmail.com",true);
        if (user.getPassword()!=null) {
            String cryptpassword = MD5.encodeString(user.getPassword(),null);
            user.setPassword(cryptpassword);
        }
        userDAO.save(conn,user);
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(userDAO);
        user.setPassword("123456");
        userService.add(conn,user);
        EasyMock.verify(userDAO);
    }

}
