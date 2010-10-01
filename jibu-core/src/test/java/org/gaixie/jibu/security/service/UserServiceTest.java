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
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.gaixie.jibu.CoreTestSupport;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.gaixie.jibu.security.model.Criteria;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.UserService;

public class UserServiceTest extends CoreTestSupport {
    private UserService userService;

    @Before public void setup() throws Exception {
        userService = getInjector().getInstance(UserService.class);
        userService.add(new User("Administrator","admin","123456","jibu.gaixie@gmail.com",true));
    }


    @Test public void testGet() throws Exception {
        User user = userService.get("admin");
        Assert.assertNotNull(user);
        user = userService.get(user.getId());
        Assert.assertNotNull(user);
        user = userService.get("notExistUser");
        Assert.assertNull(user);
    }

    @Test public void testAdd() throws Exception {
        User user = new User("tommy wang","testUserServiceAdd","123456","bitorb@gmail.com",true);
        userService.add(user);
        user = userService.get("testUserServiceAdd");
        Assert.assertNotNull(user);
    }

    @Test public void testUpdate() throws Exception {
        User user = userService.get("admin");
        user.setFullname("updateFullname");
        userService.update(user);
        user = userService.get("admin");
        Assert.assertTrue("updateFullname".equals(user.getFullname()));
    }

    @Test public void testDelete() throws Exception {
        User user = userService.get("admin");
        userService.delete(user);
        user = userService.get("admin");
        Assert.assertNull(user);
    }

    @Test public void testFind() throws Exception {
        User user = new User("tommy wang","testUserServiceAdd",
                             "123456","bitorb@gmail.com",true);

        userService.add(user);
        user = new User("tommy wang","testUserServiceAdd1",
                        "123456","bitorb@gmail.com",true);
        userService.add(user);
        user = new User();
        user.setFullname("tommy wang");
        List<User> users = userService.find(user);
        Assert.assertTrue(2 == users.size());

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
        Assert.assertTrue(0 == crt.getTotal());
        users = userService.find(user,crt);
        Assert.assertTrue(1 == users.size());
        Assert.assertTrue("testUserServiceAdd1".equals(users.get(0).getUsername()));
        Assert.assertTrue(2 == crt.getTotal());
    }

    // 关于 UserService 的 find(Role),find(Authority) 方法在 AuthorityServiceTest中被测试。
    @After public void tearDown() {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
            QueryRunner run = new QueryRunner();
            run.update(conn, "DELETE from userbase");
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            System.out.println(e.getMessage());
        }
    }
}
