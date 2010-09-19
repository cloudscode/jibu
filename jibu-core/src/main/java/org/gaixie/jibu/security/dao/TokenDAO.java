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

import org.gaixie.jibu.security.model.Token;

/**
 * Token 数据访问对象接口。
 * <p>
 *
 */
public interface TokenDAO {

    /**
     * 通过 id 得到一个 Token。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param id Token id。
     *
     * @throws SQLException
     * @return 一个 Token，如果没有对应的数据，返回 null。
     */
    public Token get(Connection conn, int id) throws SQLException;

    /**
     * 通过 key 得到一个 Token。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param value Token value。
     *
     * @throws SQLException
     * @return 一个 Token，如果没有对应的数据，返回 null。
     */
    public Token get(Connection conn, String value) throws SQLException;

    /**
     * 增加一个新的 Token。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param token 要增加的 Token。
     *
     * @throws SQLException
     */
    public void save(Connection conn, Token token) throws SQLException;

    /**
     * 删除 Token。
     * <p>
     *
     * @param conn 一个有效的数据库链接。
     * @param token 要删除的 Token。
     *
     * @throws SQLException
     */
    public void delete(Connection conn, Token token) throws SQLException;
}