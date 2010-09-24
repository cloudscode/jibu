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

import com.google.inject.Singleton;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * LoginFilter 对所有请求进行拦截并验证是否登录。
 * <p>
 * 对于特定的 URL 请求进行拦截，判断 HttpSession 中是否有 username。
 * 如果没有，直接以 JSON 格式返回提示信息。
 */
@Singleton public class LoginFilter implements Filter {

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
        HttpSession ses = ((HttpServletRequest) req).getSession(false);
        boolean allowedRequest = false;

        if (null!=ses) {
            if (null != ses.getAttribute("username")) {
                allowedRequest = true;
            }
        }

        if (!allowedRequest) {
            ((HttpServletResponse) res).sendRedirect("Login.x");
            return;
        }
        //不要cache 通过此Filter的response。
        //http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
        ((HttpServletResponse)res).setHeader("Pragma", "no-cache");
        ((HttpServletResponse)res).setHeader("Cache-Control","no-cache,no-store,max-age=0");
        chain.doFilter(req, res);
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
