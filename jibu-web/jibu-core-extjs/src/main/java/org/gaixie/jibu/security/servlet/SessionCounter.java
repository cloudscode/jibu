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

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;

/**
 * 统计当前活动的 Session 数目。
 * <p>
 */
public class SessionCounter implements HttpSessionListener {

    private static int activeSessions = 0;

    public void sessionCreated(HttpSessionEvent se) {
        activeSessions++;
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        // 如果容器有session恢复功能，可能会导致activeSessions <0，如tomcat 的 saveOnRestart
        if(activeSessions>0) {
            activeSessions--;
        }
    }

    public static int getActiveSessions() {
        return activeSessions;
    }
}