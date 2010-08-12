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

import com.google.inject.AbstractModule;

import org.gaixie.jibu.security.service.AuthorityService;
import org.gaixie.jibu.security.service.LoginService;
import org.gaixie.jibu.security.service.RoleService;
import org.gaixie.jibu.security.service.UserService;
import org.gaixie.jibu.security.service.impl.AuthorityServiceImpl;
import org.gaixie.jibu.security.service.impl.LoginServiceImpl;
import org.gaixie.jibu.security.service.impl.RoleServiceImpl;
import org.gaixie.jibu.security.service.impl.UserServiceImpl;

/**
 * Security 服务层的 Bind 类。
 * <p> 
 */
public class SecurityServiceModule extends AbstractModule {

    @Override protected void configure() {
        bind(LoginService.class).to(LoginServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(RoleService.class).to(RoleServiceImpl.class);
        bind(AuthorityService.class).to(AuthorityServiceImpl.class);
    }
}