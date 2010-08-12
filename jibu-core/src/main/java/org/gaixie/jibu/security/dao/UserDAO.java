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

import org.gaixie.jibu.security.model.User;

/**
 * User 数据访问对象接口。
 * <p>
 *
 */
public interface UserDAO {

    /**
     * 通过 username 得到一个 User。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param username User username。
     * 
     * @throws SQLException 
     * @return User
     */
    public User get(Connection conn, String username) throws SQLException;

    /**
     * 通过 username 及 password 得到一个 User。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param username User username。
     * @param password hash 后的 password。
     *
     * @throws SQLException 
     * @return User
     */
    public User login(Connection conn, String username, String password) throws SQLException;

    /**
     * 增加一个新 User。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param user User。
     *
     * @throws SQLException
     */
    public void save(Connection conn, User user) throws SQLException;

    /**
     * 根据 User 属性查询符合条件的 User List。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param user User，属性值会作为查询匹配的条件。
     *
     * @throws SQLException 
     */
    public List<User> find(Connection conn, User user) throws SQLException;

}
