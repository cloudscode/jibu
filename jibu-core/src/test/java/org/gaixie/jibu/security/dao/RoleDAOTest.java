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
import java.util.List;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.CoreTestSupport;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.dao.AuthorityDAO;
import org.gaixie.jibu.security.dao.RoleDAO;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.utils.ConnectionUtils;

public class RoleDAOTest extends CoreTestSupport {
    private Connection conn;
    private RoleDAO roleDAO;
    private AuthorityDAO authDAO;
    private UserDAO userDAO;

    @Before public void setup() throws Exception {
        roleDAO = getInjector().getInstance(RoleDAO.class); 
        authDAO = getInjector().getInstance(AuthorityDAO.class); 
        userDAO = getInjector().getInstance(UserDAO.class); 
        conn = ConnectionUtils.getConnection();
    }


    @Test public void testSaveChild() throws Exception {
        List<Role> roles = roleDAO.getAll(conn);
        int count = roles.size();
        Role parent = roleDAO.get(conn,1);
        Assert.assertNotNull(parent);

        Role role = new Role("rdt-sc","test for savechild method");
        roleDAO.saveChild(conn,role,parent);
        roles = roleDAO.getAll(conn);
        Assert.assertTrue(count +1  == roles.size());
    }

    @Test public void testBindAuth() throws Exception {
        Authority auth = new Authority("rdt-ba","action","rdt-ba.z",1);
        authDAO.save(conn,auth);
        auth = authDAO.get(conn,"rdt-ba.z",1);
        Assert.assertNotNull(auth);
        Role parent = roleDAO.get(conn,1);
        Assert.assertNotNull(parent);
        roleDAO.bind(conn,parent,auth);
        List<Role> roles = roleDAO.findByAuthority(conn,auth);
        Assert.assertTrue(1  == roles.size());
        List<String> names = roleDAO.findByAuthid(conn,auth.getId());
        Assert.assertTrue(1  == names.size());
    }

    @Test public void testBindUser() throws Exception {
        User user = new User("rdt-bu","rdt-bu","123456");
        userDAO.save(conn,user);
        user = userDAO.get(conn,"rdt-bu");
        Assert.assertNotNull(user);
        Role parent = roleDAO.get(conn,1);
        Assert.assertNotNull(parent);
        roleDAO.bind(conn,parent,user);
        List<Role> roles = roleDAO.findByUser(conn,user);
        Assert.assertTrue(1  == roles.size());
        List<String> names = roleDAO.findByUsername(conn,user.getUsername());
        Assert.assertTrue(1  == names.size());
    }

    @After public void tearDown() {
        DbUtils.rollbackAndCloseQuietly(conn);
    }
}
