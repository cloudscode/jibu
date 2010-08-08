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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.gaixie.jibu.CoreTestSupport;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.UserService;

public class UserServiceTest extends CoreTestSupport {
    private UserService userService;

    @Before public void setup() {
        userService = getInjector().getInstance(UserService.class); 
    }


    @Test public void testGet() throws Exception {
        User user = userService.get("admin");
        Assert.assertNotNull(user);
        user = userService.get("notExistUser");
        Assert.assertNull(user); 
    }

    @Test public void testAdd() throws Exception {
        User user = new User("tommy wang","testUserServiceAdd","123456","bitorb@gmail.com",true);
        userService.add(user);
        try {
            userService.add(user);
        } catch (JibuException e) {
            Assert.assertTrue("UserService.001".equals(e.getMessage()));
        }

        user = userService.get("testUserServiceAdd");
        Assert.assertNotNull(user);
    }
}
