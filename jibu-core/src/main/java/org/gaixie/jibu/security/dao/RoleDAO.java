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
package org.gaixie.jibu.security.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;

/**
 * Role 数据访问对象接口。
 * <p>
 */
public interface RoleDAO {

    /**
     * 通过 Role id 得到一个 Role。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param id Role id。
     * 
     * @throws SQLException
     * @return Role
     */
    public Role get(Connection conn, int id) throws SQLException;

    /**
     * 通过 Role name 得到一个 Role。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param name Role name。
     * 
     * @throws SQLException
     * @return Role
     */
    public Role get(Connection conn, String name) throws SQLException;

    /**
     * 为给定 Role 增加一个孩子 Role。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param role 新增的 Role。
     * @param parent 给定的父 Role。
     *
     * @throws SQLException 
     */
    public void saveChild(Connection conn, Role role, Role parent) throws SQLException ;


    /**
     * 得到全部 Role。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     *
     * @throws SQLException 
     * @return Role List
     */
    public List<Role> getAll(Connection conn) throws SQLException ;

    /**
     * 将 Role 和 Authority 进行绑定。
     *
     * @param conn 一个有效的数据库链接。
     * @param role Role。
     * @param auth Authority。
     *
     * @throws SQLException 
     */
    public void bind(Connection conn, Role role, Authority auth) throws SQLException;

    /**
     * 通过给定 Authority，得到所有已经与其绑定的 Role。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param auth Authority。
     *
     * @throws SQLException 
     * @return Role List
     */
    public List<Role> findByAuthority(Connection conn, Authority auth) throws SQLException;

    /**
     * 将 Role 和 User 进行绑定。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param role 角色
     * @param user 用户
     *
     * @throws SQLException 
     */
    public void bind(Connection conn, Role role, User user) throws SQLException;

    /**
     * 通过给定 User，得到所有已经与其绑定的 Role。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param user User。
     *
     * @throws SQLException 
     * @return Role List
     */
    public List<Role> findByUser(Connection conn, User user) throws SQLException;

    /**
     * 通过authority id 取得匹配的Role name。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param id Authority id。
     *
     * @throws SQLException 
     * @return Role name List
     */
    public List<String> findByAuthid(Connection conn, int id) throws SQLException;

    /**
     * 通过 username 取得匹配的 Role name。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param name User username。
     *
     * @throws SQLException 
     * @return Role name List
     */
    public List<String> findByUsername(Connection conn, String name) throws SQLException;
}
