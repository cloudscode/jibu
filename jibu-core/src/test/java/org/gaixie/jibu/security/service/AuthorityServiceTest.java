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
import org.gaixie.jibu.cache.Cache;
import org.gaixie.jibu.utils.CacheUtils;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.AuthorityService;
import org.gaixie.jibu.security.service.RoleService;
import org.gaixie.jibu.security.service.UserService;

public class AuthorityServiceTest extends CoreTestSupport {
    private AuthorityService authService;
    private RoleService roleService;
    private UserService userService;

    @Before public void setup() throws Exception {
        authService = getInjector().getInstance(AuthorityService.class); 
        roleService = getInjector().getInstance(RoleService.class); 
        userService = getInjector().getInstance(UserService.class); 

        authService.add(new Authority("ast-v-auth1","/ast-v-auth1.z",5));
        authService.add(new Authority("ast-v-auth1","/ast-v-auth1.z",2));
        authService.add(new Authority("ast-v-auth2","/ast-v-auth2.z",1));
        authService.add(new Authority("ast-v-auth3","/ast-v-auth3.z",1));
        userService.add(new User("ast-v-user1","ast-v-user1","123456"));

        /*
         * 建立如下角色继承关系
         * ROLE_BASE
         *     |-----ast-v-role1
         *     |          |------ast-v-role2
         *     |-----ast-v-role3
         */
        Role parent = roleService.get(1);
        roleService.addChild(new Role("ast-v-role1","ast-v-role1"),parent);
        roleService.addChild(new Role("ast-v-role3","ast-v-role3"),parent);
        parent = roleService.get("ast-v-role1");
        roleService.addChild(new Role("ast-v-role2","ast-v-role2"),parent);

        /* 将权限绑定到不同的角色
         * ROLE_BASE 
         *     |-----ast-v-role1 <==> (/ast-v-auth1.z , 5)
         *     |          |------ast-v-role2 <==> (/ast-v-auth1.z , 2)
         *     |-----ast-v-role3 <==> (/ast-v-auth2.z , 1)
         */
        Authority auth = authService.get("/ast-v-auth1.z",5);
        Role role = roleService.get("ast-v-role1");
        roleService.bind(role, auth);
        auth = authService.get("/ast-v-auth1.z",2);
        role = roleService.get("ast-v-role2");
        roleService.bind(role, auth);
        auth = authService.get("/ast-v-auth2.z",1);
        role = roleService.get("ast-v-role3");
        roleService.bind(role, auth);

        /* 将用户绑定到ast-v-role2
         * ROLE_BASE
         *     |-----ast-v-role1
         *     |          |------ast-v-role2 <==> (ast-v-user1)
         *     |-----ast-v-role3 
         */
        role = roleService.get("ast-v-role2");
        User user = userService.get("ast-v-user1");
        roleService.bind(role,user);
    }


    @Test public void testUpdateAndDelete() throws Exception {
        authService.add(new Authority("ast-v-auth4","/ast-v-auth4.z",1));
        Authority auth = authService.get("/ast-v-auth4.z",1);
        Role role = roleService.get("ROLE_BASE");
        roleService.bind(role, auth);

        List<Authority> auths = authService.getAll();
	Cache cache = CacheUtils.getAuthCache();
        List<Authority> authsc = (List<Authority>)cache.get("authorities");
        Assert.assertNotNull(authsc);

        auth.setMask(2);
        authService.update(auth);
        Assert.assertNull(authService.get("/ast-v-auth4.z",1));
        // 更新后 cache失效，等待下次加载
        authsc = (List<Authority>)cache.get("authorities");
        Assert.assertNull(authsc);
        authService.delete(auth);
        Assert.assertNull(authService.get(auth.getId()));
    }

    @Test public void testFindByName() throws Exception {
        List<Authority> auths = authService.findByName("ast-v-auth");
        Assert.assertTrue(auths.size()==4);
        auths = authService.findByName("ast-v-auth1");
        Assert.assertTrue(auths.size()==2);
    }

    @Test public void testVerify() throws Exception {
        // 开始验证测试
        // ast-v-user1 由于角色继承，得到 /ast-v-auth1.z 的 读写(1+4,2)权限
        boolean bl = authService.verify("/ast-v-auth1.z",1,"ast-v-user1");
        Assert.assertTrue(bl);
        bl = authService.verify("/ast-v-auth1.z",2,"ast-v-user1");
        Assert.assertTrue(bl);
        // ast-v-user1 没有ast-v-auth1.z的删除(8)权限
        bl = authService.verify("/ast-v-auth1.z",8,"ast-v-user1");
        Assert.assertTrue(!bl);
        // ast-v-user1 没有ast-v-auth3.z的所有权限，没有继承关系
        bl = authService.verify("/ast-v-auth2.z",1,"ast-v-user1");
        Assert.assertTrue(!bl);
        // 访问一个存在，但没有做权限设置的auth，默认验证成功
        bl = authService.verify("/ast-v-auth3.z",1,"ast-v-user1");
        Assert.assertTrue(bl);
        // 访问一个不存在的auth，验证失败
        bl = authService.verify("/ast-v-auth4.z",1,"ast-v-user1");
        Assert.assertTrue(!bl);

        // 特殊角色ROLE_ADMIN下的用户 admin拥有所有权限，不判断继承关系
        bl = authService.verify("/ast-v-auth1.z",8,"admin");
        Assert.assertTrue(bl);
        bl = authService.verify("/ast-v-auth3.z",2,"admin");
        Assert.assertTrue(bl);
        bl = authService.verify("/ast-v-auth3.z",8,"admin");
        Assert.assertTrue(bl);
        // ROLE_ADMIN 角色下的用户可以访问没有设置的权限
        bl = authService.verify("/ast-v-auth4.z",8,"admin");
        Assert.assertTrue(bl);
    }

    @Test public void testFindNamesByUsername() throws Exception {
        // 判断user 应该拥有一个权限/ast-v-auth1.z
        List<String> names = authService.findNamesByUsername("ast-v-user1");
        Assert.assertTrue(2==names.size());
        Assert.assertTrue(names.contains("ast-v-auth1"));
        Assert.assertTrue(names.contains("ast-v-auth3"));
        // ROLE_ADMIN拥有所有权限，distinct后得到/ast-v-auth1.z 和/ast-v-auth2.z
        names = authService.findNamesByUsername("admin");
        Assert.assertTrue(names.size()>=3);
        Assert.assertTrue(names.contains("ast-v-auth2"));
    }

    // 重负荷测试，不用每次执行
    //@Test 
        public void testPerformance() throws Exception {
        // 增加 10 级角色，层层继承
        Role parent = roleService.get(1);
        for (int i=0;i<10;i++) {
            Role role = new Role("ast-pf-role"+i,"ast-pf-role"+1);
            roleService.addChild(role,parent);
            parent = roleService.get(role.getName());
        }
        parent = roleService.get(1);
        // 增加1000个auth (500个不同的auth.value), 全部绑定到ROLE_BASE
        // 全部占用160k cache
        for (int i=0;i<500;i++) {
            Authority auth = new Authority("ast-pf-auth"+i,"/ast-pf-auth"+i+".z",1);
            authService.add(auth);
            auth = authService.get(auth.getValue(),auth.getMask());
            roleService.bind(parent, auth);
            auth = new Authority("ast-pf-auth"+i,"/ast-pf-auth"+i+".z",4);
            authService.add(auth);
            auth = authService.get(auth.getValue(),auth.getMask());
            roleService.bind(parent, auth);
        }

        // 增加500个user，绑定到最底层的role
        Role role = roleService.get("ast-pf-role9");
        for (int i=0;i<500;i++) {
            User user = new User("ast-pf-user"+i,"ast-pf-user"+i,"123456");
            userService.add(user);
            user = userService.get(user.getUsername());
            roleService.bind(role,user);
        }

        // 取菜单时间，毫秒级，第一次没有Cache
        long start = System.currentTimeMillis();
        List<String> names = authService.findNamesByUsername("ast-pf-user100");
        System.out.println("no cache:" + (System.currentTimeMillis() - start) +" ms, get "+names.size()+" menus.");
        start = System.currentTimeMillis();
        names = authService.findNamesByUsername("ast-pf-user100");
        System.out.println("in cache:" + (System.currentTimeMillis() - start) +" ms, get "+names.size()+" menus.");

        // 验证时间，纳秒级，一个user在cache大约占用0.4k
        start = System.nanoTime();
        boolean bl = authService.verify("/ast-pf-auth100.z",1,"ast-pf-user101");
        System.out.println("no cache:" + (System.nanoTime() - start) +" ns, verify: "+bl);
        start = System.nanoTime();
        bl = authService.verify("/ast-pf-auth100.z",1,"ast-pf-user101");
        System.out.println("in cache:" + (System.nanoTime() - start) +" ns, verify: "+bl);
    }

    @After public void tearDown() {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
            QueryRunner run = new QueryRunner();
            run.update(conn, "DELETE from role_authority_map "); 
            run.update(conn, "DELETE from user_role_map where user_id >1"); 
            run.update(conn, "DELETE from roles where id > 2"); 
            run.update(conn, "DELETE from authorities "); 
            run.update(conn, "DELETE from userbase where id >1"); 
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            System.out.println(e.getMessage());
        }
    }
}
