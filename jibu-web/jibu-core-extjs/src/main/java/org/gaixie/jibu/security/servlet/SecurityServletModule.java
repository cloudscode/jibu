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

import com.google.inject.servlet.ServletModule;

import org.gaixie.jibu.security.servlet.AuthorityServlet;
import org.gaixie.jibu.security.servlet.LoginFilter;
import org.gaixie.jibu.security.servlet.LoginServlet;
import org.gaixie.jibu.security.servlet.MainServlet;
import org.gaixie.jibu.security.servlet.MonitorServlet;
import org.gaixie.jibu.security.servlet.RoleServlet;
import org.gaixie.jibu.security.servlet.SettingServlet;
import org.gaixie.jibu.security.servlet.UserServlet;

/**
 * Security Servlet 的 Bind 类。
 * <p>
 * Servlet mapping ，以及 Filter的拦截策略都在这里定义。
 */
public class SecurityServletModule extends ServletModule {
    @Override protected void configureServlets() {
        filter("*.y","*.z").through(LoginFilter.class);
        serve("/").with(LoginServlet.class);
        serve("/Login.x").with(LoginServlet.class);
        serve("/Main.y").with(MainServlet.class);
        serve("/Setting.y").with(SettingServlet.class);
        serve("/User.z").with(UserServlet.class);
        serve("/Authority.z").with(AuthorityServlet.class);
        serve("/Role.z").with(RoleServlet.class);
        serve("/Monitor.z").with(MonitorServlet.class);
    }
}