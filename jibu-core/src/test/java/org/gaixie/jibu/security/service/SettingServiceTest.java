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
import org.gaixie.jibu.security.model.Setting;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.SettingService;
import org.gaixie.jibu.security.service.UserService;

public class SettingServiceTest extends CoreTestSupport {
    private SettingService settingService;
    private UserService userService;

    @Before public void setup() throws Exception {
        settingService = getInjector().getInstance(SettingService.class); 
        userService = getInjector().getInstance(UserService.class); 
        Setting setting = new Setting("theme","blue",0,true);
        settingService.add(setting);
        setting = new Setting("theme","gray",1,true);
        settingService.add(setting);
    }


    @Test public void testGet() throws Exception {
        Setting setting = settingService.get(1);
        Assert.assertNotNull(setting);
        Setting s = settingService.get("theme","blue");
        Assert.assertTrue(setting.equals(s));
        s = settingService.getDefault("theme");
        Assert.assertTrue(setting.equals(s));
    }

    @Test public void testGetByUsername() throws Exception {
        Setting dfsetting = settingService.getDefault("theme");
        User user = userService.get("admin"); 
        Setting s = settingService.getByUsername("theme","admin");
        // 没有bind，所以取到 null
        Assert.assertNull(s);
        Setting setting = settingService.get("theme","gray");
        settingService.bind(setting.getId(),user.getUsername());
        s = settingService.getByUsername("theme","admin");
        // bind后，取绑定的 setting
        Assert.assertTrue(setting.equals(s));
        // 取消bind后，取到null
        settingService.unbind(setting.getId(),user.getUsername());
        s = settingService.getByUsername("theme","admin");
        Assert.assertNull(s);
    }

    @Test public void testFindByName() throws Exception {
        List<Setting> settings = settingService.findByName("theme");
        Assert.assertTrue(settings.size()==2);
    }


    @After public void tearDown() {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
            QueryRunner run = new QueryRunner();
            run.update(conn, "DELETE from user_setting_map"); 
            run.update(conn, "DELETE from settings"); 
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            System.out.println(e.getMessage());
        }
    }
}
