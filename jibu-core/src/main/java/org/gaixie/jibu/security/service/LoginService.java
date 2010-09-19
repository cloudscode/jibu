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

import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.model.Token;
import org.gaixie.jibu.security.model.User;

/**
 * 系统登录服务接口。
 * <p>
 */
public interface LoginService {

    /**
     * 通过用户名及密码进行登录验证。
     * <p>
     *
     * @param username 登录用户名。
     * @param password 没有进行hash的登录密码。
     *
     * @exception LoginException 用户名或密码错误时抛出。
     */
    public void login(String username, String password) throws LoginException;

    /**
     * 通过用户名产生一个用于密码找回的令牌，有效期为 1 天。
     * <p>
     *
     * @param username 要获取 Token 的用户 username。
     * @return 一个可以密码找回的 Token，如果 username 无效，返回null。
     */
    public Token generateToken(String username);

    /**
     * 通过一个 Token value ，进行密码重置。
     * <p>
     *
     * @param tokenValue Token value。
     * @param password 要重置的新密码。
     * @exception JibuException password 为null或者空串，tokenValue 无效或者已过期时抛出。
     */
    public void resetPassword(String tokenValue , String password) throws JibuException;
}
