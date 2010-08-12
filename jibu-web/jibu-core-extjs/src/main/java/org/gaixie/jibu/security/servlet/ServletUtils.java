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

import java.beans.IntrospectionException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.utils.BeanConverter;

/**
 * Servlet 工具类。
 * <p>
 */
public class ServletUtils {

    /**
     * 将 HttpServletRequest 中的值按照约定自动装入指定的 Javabean。
     * <p>
     * 如果 request 中的 name 满足 ClassName.property 的格式，会自动
     * 取出对应的 value 并装入Bean 相应属性 。如：User.password=123456，
     * 那么会将 123456 set 到 User 的 password 属性中。 <br>

     * @param cls 要生成的 Bean Class。
     * @param req 要处理的 Request。
     * @return 生成的 Javabean
     */
    public static <T> T httpToBean(Class<T> cls, HttpServletRequest req) 
        throws JibuException {
        HashMap map = new HashMap();
        Enumeration names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = req.getParameter(name);
            if (!value.trim().isEmpty()) {
                map.put(name, req.getParameter(name));
            }
        }
        T bean = BeanConverter.mapToBean(cls, map);
        return bean;
    }
}