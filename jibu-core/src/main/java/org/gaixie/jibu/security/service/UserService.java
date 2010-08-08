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

import java.util.List;

import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.model.User;

/**
 * 用户的查询，增加，修改，删除等服务
 */
public interface UserService {

    /**
     * 通过用户名得到用户对象
     *
     * @param username 登录用户名
     * @return 用户对象
     */
    public User get(String username) ;

    /**
     * 增加一个新用户，密码不能为空，且是没有hash过的
     *
     * @param user 用户对象
     * @exception JibuException 如果用户名已存在，抛出此异常
     */
    public void add(User user) throws JibuException;

    /**
     * 查询所有匹配给定用户属性的用户集合
     *
     * @param user 用来传递查询条件的用户对象。
     * @return 用户对象集合
     */
    public List<User> find(User user);

}
