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

/**
 * Authrity的查询，增加，修改，删除等服务
 */
public interface AuthorityService {

    /**
     * 通过Authority Id得到Authority
     *
     * @param id Authority Id
     * @return Authority
     */
    public Authority get(int id);

    /**
     * 通过Authority.value与Authority.mask得到Authority
     *
     * @param value Authority.value
     * @param mask Authority.mask
     * @return Authority
     */
    public Authority get(String value, int mask);

    /**
     * 增加一个新Authority
     *
     * @param auth Authority
     * @exception JibuException 如果Authority已存在，抛出此异常
     */
    public void add(Authority auth) throws JibuException;

    /**
     * 根据Authority type查询Authority
     *
     * @param type Authority type
     * @return Authority List
     */
    public List<Authority> findByType(String type);

    /**
     * 根据username查询拥有的Role.name
     *
     * @param username User.username
     * @return Role.name List
     */
    public List<String> findRoleNamesByUsername(String username);

    /**
     * 根据username得到所拥有的类型为ACTION的authority.name
     *
     * @param username User.username
     * @return Authority.name List
     */
    public List<String> findNamesByUsername(String username);

    /**
     * 验证用户对给定操作是否有权限
     *
     * @param action 用户操作，对应于authority 的value属性
     * @param crud 用户操作的类型，有create, retrive,update,delete, 
     * 用于与authority 的mask属性进行比较。
     * @param username 用户名
     * @return ture 有权限，false 无权限
     */
    public boolean verify(String action, int crud, String username);
}
