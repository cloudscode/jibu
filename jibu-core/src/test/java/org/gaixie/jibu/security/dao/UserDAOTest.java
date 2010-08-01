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
package org.gaixie.jibu.security.dao;

import java.sql.Connection;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.utils.ConnectionUtils;

public class UserDAOTest extends DAOTestSupport {
    private Connection conn;
    private UserDAO userDAO;

    @Before public void setup() throws Exception {
        userDAO = getInjector().getInstance(UserDAO.class); 
        conn = ConnectionUtils.getConnection();
    }


    @Test public void testGet() throws Exception {
        User user = userDAO.get(conn,"admin");
        Assert.assertNotNull(user);
        user = userDAO.get(conn,"notExistUser");
        Assert.assertNull(user); 
    }

    @Test public void testSave() throws Exception {
        User user = new User("tommy","testsave","123456","bito@xxx.com",true);
        userDAO.save(conn,user);
        try {
            userDAO.save(conn,user);
        } catch (JibuException e) {
            Assert.assertTrue("001B-0002".equals(e.getMessage()));
        }
    }

    @After public void tearDown() {
        DbUtils.rollbackAndCloseQuietly(conn);
    }
}
