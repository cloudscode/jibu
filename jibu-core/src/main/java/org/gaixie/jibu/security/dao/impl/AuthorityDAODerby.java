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
package org.gaixie.jibu.security.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.gaixie.jibu.security.dao.AuthorityDAO;
import org.gaixie.jibu.security.model.Authority;

/**
 * Authority 数据访问接口的 Derby 实现。
 * <p>
 */
public class AuthorityDAODerby implements AuthorityDAO {
    private QueryRunner run = null;

    public AuthorityDAODerby() {
	this.run = new QueryRunner();
    }

    public Authority get( Connection conn, int id) throws SQLException {
        ResultSetHandler<Authority> h = new BeanHandler(Authority.class);
        return run.query(conn
                         , "SELECT id,name,value,mask FROM authorities WHERE id=? "
                         , h
                         , id);
    }

    public Authority get( Connection conn, String value, int mask) throws SQLException {
        ResultSetHandler<Authority> h = new BeanHandler(Authority.class);
        return run.query(conn
                         , "SELECT id,name,value,mask FROM authorities WHERE value=? and mask =?"
                         , h
                         , value
                         , mask);
    }

    public void save(Connection conn, Authority auth) throws SQLException {
        run.update(conn
                   , "INSERT INTO authorities (name,value,mask) values (?,?,?)"
                   , auth.getName()
                   , auth.getValue()
                   , auth.getMask()); 
    }

    /**
     * {@inheritDoc}
     * <p>
     * 返回值永远不会为 null，无值 size()==0  。
     */
    public List<Authority> getAll( Connection conn) throws SQLException {
        ResultSetHandler<List<Authority>> h = new BeanListHandler(Authority.class);
        return run.query(conn
                         , "SELECT distinct name,value FROM authorities order by name "
                         , h);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 返回值永远不会为 null，无值 size()==0  。
     */
    public List<Authority> findByValue( Connection conn, String value) throws SQLException {
        ResultSetHandler<List<Authority>> h = new BeanListHandler(Authority.class);
        return run.query(conn
                         , "SELECT id,name,value,mask FROM authorities WHERE value =? "
                         , h
                         , value);
    }
}
