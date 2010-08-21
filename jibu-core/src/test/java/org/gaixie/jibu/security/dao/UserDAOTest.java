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
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.Criteria;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.utils.ConnectionUtils;

public class UserDAOTest extends CoreTestSupport {
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
        User user = new User("Tommy Wang","tommy","123456");
        userDAO.save(conn,user);
        user = userDAO.get(conn,"tommy");
        Assert.assertNotNull(user);
    }

    @Test public void testFind() throws Exception {
        User user = new User("Tommy Wang","tommy","123456","1@x.xxx",true);
        userDAO.save(conn,user);
        user = new User("Tommy Wang","tommy1","123456","2@x.xxx",true);
        userDAO.save(conn,user);

        user = new User();
        user.setFullname("Tommy Wang");
        // where fullname = 'Tommy Wang' and enabled = true
        List<User> users = userDAO.find(conn,user);
        Assert.assertTrue(2 == users.size());
        user.setFullname(null);
        // where enabled = true;
        users = userDAO.find(conn,user);
        // admin + tommy + tomm1
        Assert.assertTrue(3 == users.size());
    }

    @Test public void testFindByCriteria() throws Exception {
        User user = new User("Tommy Wang","tommy","123456","1@x.xxx",true);
        userDAO.save(conn,user);
        user = new User("Tommy Wang","tommy1","123456","2@x.xxx",true);
        userDAO.save(conn,user);

        user = new User();

        // 测试 Criteria 条件查询
        Criteria crt = new Criteria();
        crt.setStart(0);
        crt.setLimit(1);
        crt.setSort("username");
        crt.setDir("DESC");
        // 没有 crt  select * from where enabled = true
        // 有 分页 crt  SELECT * FROM ( 
        //                            select ROW_NUMBER() OVER() AS R, userbase.* 
        //                            from userbase) as TR
        //               where R >0 AND R <=1
        //               ORDER BY username DESC
        List<User> users = userDAO.find(conn,user,crt);
        Assert.assertTrue(1 == users.size());
        Assert.assertTrue("tommy1".equals(users.get(0).getUsername()));
        crt.setStart(1);
        crt.setLimit(2);
        users = userDAO.find(conn,user,crt);
        Assert.assertTrue(2 == users.size());
        Assert.assertTrue("tommy".equals(users.get(0).getUsername()));
        Assert.assertTrue("admin".equals(users.get(1).getUsername()));
    }

    @After public void tearDown() {
        DbUtils.rollbackAndCloseQuietly(conn);
    }
}
