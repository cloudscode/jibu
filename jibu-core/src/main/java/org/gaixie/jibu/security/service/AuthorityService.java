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
 * Authrity 服务接口。
 * <p>
 */
public interface AuthorityService {

    /**
     * 通过Authority Id 得到 Authority。
     *
     * @param id Authority Id
     * @return Authority
     */
    public Authority get(int id);

    /**
     * 通过 Authority value 与 mask 得到 Authority。
     *
     * @param value Authority value
     * @param mask Authority mask
     * @return Authority
     */
    public Authority get(String value, int mask);

    /**
     * 增加一个新的 Authority。
     *
     * @param auth Authority
     * @exception JibuException 如果Authority已存在抛出
     */
    public void add(Authority auth) throws JibuException;

    /**
     * 得到所有 Authority。
     *
     * @return Authority List
     */
    public List<Authority> getAll();

    /**
     * 根据 username 得到有权限的Authority name。
     *
     * @param username User username
     * @return Authority name List
     */
    public List<String> findNamesByUsername(String username);

    /**
     * 验证 User 对给定 Action 是否有权限。
     *
     * @param action 用户操作，对应于 Authority 的 value 属性
     * @param crud 用户操作的类型，有create, retrive, update, delete 
     * 用于与Authority 的 mask 属性进行比较。
     * @param username User username
     * @return ture 有权限，false 无权限
     */
    public boolean verify(String action, int crud, String username);

    /**
     * 删除 Authority。
     *
     * @param auth Authority
     * @exception JibuException 删除失败时抛出
     */
    public void delete(Authority auth) throws JibuException;


    /**
     * 更新 Authority。
     *
     * @param auth Authority
     * @exception JibuException 更新失败时抛出
     */
    public void update(Authority auth) throws JibuException;

    /**
     * 根据 Authority name 模糊查询 Authority。
     *
     * @param name Authority name
     * @return Authority List
     */
    public List<Authority> findByName(String name);
}
