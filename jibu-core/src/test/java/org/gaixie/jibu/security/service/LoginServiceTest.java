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
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.gaixie.jibu.CoreTestSupport;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.model.Token;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.LoginException;
import org.gaixie.jibu.security.service.LoginService;
import org.gaixie.jibu.security.service.UserService;
import org.gaixie.jibu.utils.ConnectionUtils;

public class LoginServiceTest extends CoreTestSupport {
    private LoginService loginService;
    private UserService userService;

    @Before public void setup() throws Exception {
        loginService = getInjector().getInstance(LoginService.class);
        userService = getInjector().getInstance(UserService.class);
        userService.add(new User("Administrator","admin","123456","jibu.gaixie@gmail.com",true));
    }


    @Test public void testLoginSuccess() throws LoginException {
        loginService.login("admin", "123456");
    }

    @Test (expected = LoginException.class)
        public void testLoginFailed() throws LoginException {
        loginService.login("admin", "654321");
    }

    @Test public void testGenerateToken() throws JibuException {
        Token token = loginService.generateToken("wrong-username");
        Assert.assertNull(token);
        token = loginService.generateToken("admin");
        Assert.assertNotNull(token);
    }

    @Test public void testResetPassword() throws JibuException {
        Token token = loginService.generateToken("admin");
        Assert.assertNotNull(token);

        // 成功的修改密码，由 123456 改为 654321
        loginService.resetPassword(token.getValue(),"654321");
        loginService.login("admin","654321");
    }

    @Test (expected = TokenException.class)
        public void testTokenExpired() throws Exception {
        Token token = loginService.generateToken("admin");
        Assert.assertNotNull(token);

        // 将token 的有效期提前两天，让token过期。
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.DAY_OF_MONTH, -2);
        long time = calendar.getTimeInMillis();
        Timestamp ts = new Timestamp(time);

        Connection conn = ConnectionUtils.getConnection();
        QueryRunner run = new QueryRunner();
        run.update(conn
                   , "Update tokens set expiration =? where value = ?"
                   ,ts
                   ,token.getValue()); 
        DbUtils.commitAndClose(conn);

        loginService.resetPassword(token.getValue(),"654321");
    }

    @Test (expected = TokenException.class)
        public void testTokenInvalid() throws Exception {
        Token token = loginService.generateToken("admin");
        Assert.assertNotNull(token);
        loginService.resetPassword("aaaaa","654321");        
    }

    @Test (expected = JibuException.class)
        public void testTokenPasswordInvalid() throws Exception {
        Token token = loginService.generateToken("admin");
        Assert.assertNotNull(token);
        loginService.resetPassword(token.getValue(),"");        
    }

    @After public void tearDown() {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
            QueryRunner run = new QueryRunner();
            run.update(conn, "DELETE from tokens"); 
            run.update(conn, "DELETE from userbase"); 
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            System.out.println(e.getMessage());
        }
    }
}
