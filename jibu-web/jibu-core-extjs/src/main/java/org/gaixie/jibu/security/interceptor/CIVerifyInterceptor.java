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
package org.gaixie.jibu.security.interceptor;

import com.google.inject.Injector;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.gaixie.jibu.security.service.AuthorityService;
import org.gaixie.jibu.security.servlet.ServletUtils;

public class CIVerifyInterceptor implements MethodInterceptor {

    @Override
        public Object invoke(MethodInvocation inv) throws Throwable {
        Object []args = inv.getArguments();
        Object obj = null;
        HttpServletRequest req = (HttpServletRequest)args[0];
        Injector injector = (Injector)args[1];
        String value = req.getServletPath()+"?ci="+req.getParameter("ci");
        String username = ServletUtils.getUsername(req);
        AuthorityService authService = injector.getInstance(AuthorityService.class);
        boolean allowed = authService.verify(value,username);
        if (allowed) {
            obj = inv.proceed();
        } else {
            ResourceBundle rb =
                ResourceBundle.getBundle("i18n/CoreExtjsResources",
                                     ServletUtils.getLocale(req));
            obj = "{\"message\":\""+rb.getString("message.003")+"\",\"success\":false}";
        }
        return obj;
    }
}