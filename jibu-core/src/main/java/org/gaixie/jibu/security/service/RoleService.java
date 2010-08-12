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
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;

/**
 * Role 服务接口。
 * <p>
 */
public interface RoleService {

    /**
     * 通过 Role id 得到 Role。
     * <p>
     *
     * @param id Role id。
     * @return Role
     */
    public Role get(int id) ;

    /**
     * 通过 Role name 得到 Role 。
     * <p>
     *
     * @param name Role name。
     * @return Role
     */
    public Role get(String name) ;

    /**
     * 为给定 Role 增加一个新的子 Role 。
     * <p>
     *
     * @param role 新 Role。
     * @param parent 父 Role。
     * @exception JibuException 如果角色名已存在，抛出。
     */
    public void addChild(Role role, Role parent) throws JibuException;

    /**
     * 得到一个完整的 Role List。
     * <p>
     *
     * @return Role List
     */
    public List<Role> getAll();

    /**
     * 将给定 Role 与 Authorit 进行绑定。
     * <p>
     *
     * @param role Role。
     * @param auth Authority。
     * @exception JibuException 如果操作出现并发修改抛出。
     */
    public void bind(Role role, Authority auth) throws JibuException;

    /**
     * 给定 Authority，查询与之绑定的 Role。
     * <p>
     *
     * @param auth Authority。
     * @return Role List
     */
    public List<Role> findByAuthority(Authority auth);

    /**
     * 将给定 Role 与 User 进行绑定。
     * <p>
     *
     * @param role Role。
     * @param user User。
     * @exception JibuException 如果操作出现并发修改抛出。
     */
    public void bind(Role role, User user) throws JibuException;

    /**
     * 给定 User，查询与之绑定的 Role。
     * <p>
     *
     * @param user User。
     * @return Role List
     */
    public List<Role> findByUser(User user);
}
