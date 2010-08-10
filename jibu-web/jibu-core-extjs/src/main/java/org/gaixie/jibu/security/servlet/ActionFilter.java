/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gaixie.jibu.security.servlet;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gaixie.jibu.security.service.AuthorityService;

@Singleton public class ActionFilter implements Filter {

    @Inject private Injector injector;

    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    private FilterConfig filterConfig = null;
    
    public void init(FilterConfig filterConfig)
        throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        AuthorityService authService = injector.getInstance(AuthorityService.class);

        HttpSession ses = ((HttpServletRequest) req).getSession(false);


        String servletPath  = ((HttpServletRequest) req).getServletPath();
        String crud = req.getParameter("crud");
        boolean allowedRequest = false;
        if(null != crud ) {
            int i = 0;
            try {
                i = Integer.parseInt(crud);
                if (null!=ses) {
                    String username = (String) ses.getValue("username"); 
                    if (null != username ) {
                        allowedRequest = authService.verify(servletPath,i,username);
                    }
                }
            } catch (NumberFormatException e) {
                // resp 
            }
        }

        if (!allowedRequest) {
            ServletOutputStream output=res.getOutputStream();
            output.println("{\"message\":\"no permission\",\"success\":false}");
            output.close();
            return;
        }

        chain.doFilter(req, res);
    }

    public void destroy() {
        this.filterConfig = null;
    }
}