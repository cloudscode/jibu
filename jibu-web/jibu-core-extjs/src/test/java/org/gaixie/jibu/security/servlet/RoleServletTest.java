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
package org.gaixie.jibu.security.servlet;

import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.easymock.EasyMock; 
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.RoleService;
import org.gaixie.jibu.security.service.UserService;
import org.gaixie.jibu.security.servlet.RoleServlet;
import org.gaixie.jibu.security.servlet.NullHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;  
import org.junit.Test;  

public class RoleServletTest {
    private RoleServlet roleServlet;
    private NullHttpServletRequest nullRequest;
    private Injector mockInjector;
    private UserService mockUserService;
    private RoleService mockRoleService;

    @Before public void setUp() {
        roleServlet = new RoleServlet();
        nullRequest = new NullHttpServletRequest();

        //创建request和response的Mock  
        mockInjector = EasyMock.createMock(Injector.class);
        mockUserService = EasyMock.createMock(UserService.class);
        mockRoleService = EasyMock.createMock(RoleService.class);

    }

    @Test public void testGetAllRole() throws Exception {
        List<Role> roles = new ArrayList<Role>();
        Role role = new Role("r1","r1-d");
        role.setId(1);
        role.setLft(1);
        role.setRgt(10);
        role.setDepth(0);
        roles.add(role);
        role = new Role("r1.1","r1.1-d");
        role.setId(2);
        role.setLft(2);
        role.setRgt(3);
        role.setDepth(1);
        roles.add(role);
        role = new Role("r1.2","r1.2-d");
        role.setId(3);
        role.setLft(4);
        role.setRgt(7);
        role.setDepth(1);
        roles.add(role);
        role = new Role("r1.2.1","r1.2.1-d");
        role.setId(4);
        role.setLft(5);
        role.setRgt(6);
        role.setDepth(2);
        roles.add(role);
        role = new Role("r1.3","r1.3-d");
        role.setId(5);
        role.setLft(8);
        role.setRgt(9);
        role.setDepth(1);
        roles.add(role);
        EasyMock.expect(mockInjector.getInstance(RoleService.class)).andReturn(mockRoleService);
        EasyMock.expect(mockRoleService.getAll()).andReturn(roles);


        EasyMock.replay(mockRoleService);
        EasyMock.replay(mockInjector);

        String rst = roleServlet.getAllRole(nullRequest,mockInjector);
        StringBuilder sb = new StringBuilder();
        sb.append("[{id:1,text:\"r1\",description:\"r1-d\",checked:false,leaf:false,children:");
        sb.append("[{id:2,text:\"r1.1\",description:\"r1.1-d\",checked:false,leaf:true},");
        sb.append("{id:3,text:\"r1.2\",description:\"r1.2-d\",checked:false,leaf:false,children:");
        sb.append("[{id:4,text:\"r1.2.1\",description:\"r1.2.1-d\",checked:false,leaf:true}]},");
        sb.append("{id:5,text:\"r1.3\",description:\"r1.3-d\",checked:false,leaf:true}]}]");
        Assert.assertTrue(sb.toString().equals(rst));
        EasyMock.verify(mockRoleService);
        EasyMock.verify(mockInjector);
    }
}