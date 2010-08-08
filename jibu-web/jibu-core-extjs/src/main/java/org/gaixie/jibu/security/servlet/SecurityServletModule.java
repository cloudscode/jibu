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

import java.util.HashMap;
import java.util.Map;

import org.gaixie.jibu.security.servlet.ActionFilter;
import org.gaixie.jibu.security.servlet.LoginFilter;
import org.gaixie.jibu.security.servlet.LoginServlet;
import org.gaixie.jibu.security.servlet.MainServlet;
import org.gaixie.jibu.security.servlet.UserServlet;

public class SecurityServletModule extends ServletModule {
    @Override protected void configureServlets() {
        filter("*.y","*.z").through(LoginFilter.class);
        filter("*.z").through(ActionFilter.class);
        serve("/LoginServlet.x").with(LoginServlet.class);

        Map<String, String> params = new HashMap<String, String>();
        params.put("main.title", "Jibu Web Application");
        // 以逗号分隔的js文件名，会被一次加载 
        params.put("js.names", "jibu-core-all.js");
        serve("/MainServlet.y").with(MainServlet.class,params);
        serve("/UserServlet.z").with(UserServlet.class,params);
    }
}