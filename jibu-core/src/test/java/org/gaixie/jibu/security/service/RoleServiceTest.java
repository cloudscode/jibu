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
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.service.RoleException;
import org.gaixie.jibu.security.service.RoleService;

public class RoleServiceTest extends CoreTestSupport {
    private RoleService roleService;

    @Before public void setup() throws Exception {
        roleService = getInjector().getInstance(RoleService.class);
        Connection conn = ConnectionUtils.getConnection();
        QueryRunner run = new QueryRunner();
        run.update(conn, "INSERT INTO roles (name,description,lft,rgt) values ('ROLE_BASE','ROLE_BASE',1,2)");
        DbUtils.commitAndClose(conn);
    }


    @Test public void testGet() throws Exception {
        Role root = roleService.get("ROLE_BASE");
        Assert.assertNotNull(root);
        Assert.assertEquals(root,roleService.get(root.getId()));
    }

    @Test public void testAdd() throws Exception {
        Role root = roleService.get("ROLE_BASE");
        Assert.assertTrue(root.getLft()==1);
        Assert.assertTrue(root.getRgt()==2);

        roleService.add(new Role("ROLE_L1","ROLE_L1"),root);
        root = roleService.get("ROLE_BASE");
        Assert.assertTrue(root.getLft()==1);
        Assert.assertTrue(root.getRgt()==4);

        roleService.add(new Role("ROLE_L2","ROLE_L2"),root);
        root = roleService.get("ROLE_BASE");
        Assert.assertTrue(root.getLft()==1);
        Assert.assertTrue(root.getRgt()==6);

        Role role = roleService.get("ROLE_L2");
        Assert.assertTrue(role.getLft()==2);
        Assert.assertTrue(role.getRgt()==3);

        role = roleService.get("ROLE_L1");
        Assert.assertTrue(role.getLft()==4);
        Assert.assertTrue(role.getRgt()==5);

    }

    @Test public void testUpdate() throws Exception {
        Role role = roleService.get("ROLE_BASE");
        role.setName("UPDATE_ROLE");
        roleService.update(role);
        role = roleService.get("UPDATE_ROLE");
        Assert.assertNotNull(role);
    }

    @Test (expected = RoleException.class)
        public void testDeleteNotLeaf() throws Exception {
        Role root = roleService.get("ROLE_BASE");
        roleService.add(new Role("ROLE_L1","ROLE_L1"),root);
        roleService.delete(root);
    }

    @Test public void testDeleteLeaf() throws Exception {
        Role root = roleService.get("ROLE_BASE");
        roleService.add(new Role("ROLE_L1","ROLE_L1"),root);
        roleService.add(new Role("ROLE_L2","ROLE_L2"),root);
        Role role = roleService.get("ROLE_L2");
        roleService.delete(role);

        root = roleService.get("ROLE_BASE");
        Assert.assertTrue(root.getLft()==1);
        Assert.assertTrue(root.getRgt()==4);

        role = roleService.get("ROLE_L1");
        Assert.assertTrue(role.getLft()==2);
        Assert.assertTrue(role.getRgt()==3);
    }

    @Test public void testGetAll() throws Exception {
        List<Role> roles = roleService.getAll();
        Assert.assertTrue(1 == roles.size());
    }

    // 关于 RoleService 的 find(user),find(Authority) 方法在 AuthorityServiceTest中被测试。
    @After public void tearDown() {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
            QueryRunner run = new QueryRunner();
            run.update(conn, "DELETE from roles");
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            System.out.println(e.getMessage());
        }
    }
}
