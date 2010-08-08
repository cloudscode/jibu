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
 * 角色的查询，增加，修改，删除等服务
 */
public interface RoleService {

    /**
     * 通过角色名得到角色
     *
     * @param id Role.id
     * @return Role
     */
    public Role get(int id) ;

    /**
     * 通过角色名得到角色
     *
     * @param name Role.name
     * @return Role
     */
    public Role get(String name) ;

    /**
     * 为给定角色增加一个新的子角色
     *
     * @param role 新角色
     * @param parent 父角色
     * @exception JibuException 如果角色名已存在，抛出此异常
     */
    public void addChild(Role role, Role parent) throws JibuException;

    /**
     * 得到一个完整的角色集合
     *
     * @return 角色集合
     */
    public List<Role> getAll();

    /**
     * 将给定角色与权限资源进行绑定
     *
     * @param role 角色
     * @param auth 权限资源
     * @exception JibuException 如果操作出现并发修改抛出
     */
    public void bind(Role role, Authority auth) throws JibuException;

    /**
     * 给定权限资源，查询与之绑定的角色
     *
     * @param auth 权限资源
     * @return 角色集合
     */
    public List<Role> findByAuthority(Authority auth);

    /**
     * 将给定角色与用户进行绑定
     *
     * @param role 角色
     * @param user 用户
     * @exception JibuException 如果操作出现并发修改抛出
     */
    public void bind(Role role, User user) throws JibuException;

    /**
     * 给定角色，查询与之绑定的角色
     *
     * @param user 角色
     * @return 角色集合
     */
    public List<Role> findByUser(User user);
}
