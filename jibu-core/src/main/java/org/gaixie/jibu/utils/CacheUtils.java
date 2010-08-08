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
package org.gaixie.jibu.utils;

import org.gaixie.jibu.cache.Cache;
import org.gaixie.jibu.cache.DefaultCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CacheUtils {
    private static final Logger logger = LoggerFactory.getLogger(CacheUtils.class);
    private static Cache authCache=null;
    private static Cache userCache=null;

    public static synchronized Cache getAuthCache() {
        if(authCache != null) {
            return authCache;
        }
        authCache = new DefaultCache("cache.authCache", 256 * 1024l, -1l);
        return authCache;
    }

    public static synchronized Cache getUserCache() {
        if(userCache != null) {
            return userCache;
        } 
        userCache = new DefaultCache("cache.userCache", 128 * 1024l, 1000 * 60 * 30l);
        return userCache;
    }

}
