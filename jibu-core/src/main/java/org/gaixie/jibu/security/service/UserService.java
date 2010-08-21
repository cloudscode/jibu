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
import org.gaixie.jibu.security.model.Criteria;
import org.gaixie.jibu.security.model.User;

/**
 * User 服务接口。
 * <p>
 */
public interface UserService {

    /**
     * 通过 username 得到 User。
     * <p>
     *
     * @param username User username。
     * @return User
     */
    public User get(String username) ;

    /**
     * 增加一个新 User，password 不能为空，且是没有 hash 过的。
     * <p>
     *
     * @param user User
     * @exception JibuException 如果用户名已存在，抛出。
     */
    public void add(User user) throws JibuException;

    /**
     * 查询所有匹配给定 User 属性的 User List。
     * <p>
     *
     * @param user 用来传递查询条件的 User。
     * @return User List
     */
    public List<User> find(User user);

    /**
     * 查询所有匹配给定 User 属性，并且符合 Criteria 约束的 User List。
     * <p>
     * 如果 criteria 为 null，等同于调用 {@code List<User> find(User user)}。<br>
     * @param user 用来传递查询条件的 User。
     * @param criteria 用来传递分页，排序等附加查询条件。
     * @return User List
     * @see #find(User user)
     */
    public List<User> find(User user, Criteria criteria);
}
