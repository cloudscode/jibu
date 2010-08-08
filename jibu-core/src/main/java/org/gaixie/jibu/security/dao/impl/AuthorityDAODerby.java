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
 * AuthorityDAO接口的Derby实现
 */
public class AuthorityDAODerby implements AuthorityDAO {
    public Authority get( Connection conn, int id) throws SQLException {
        ResultSetHandler<Authority> h = new BeanHandler(Authority.class);
        QueryRunner run = new QueryRunner();
        return run.query(conn
                         , "SELECT id,name,type,value,mask FROM authorities WHERE id=? "
                         , h
                         , id);
    }

    public Authority get( Connection conn, String value, int mask) throws SQLException {
        ResultSetHandler<Authority> h = new BeanHandler(Authority.class);
        QueryRunner run = new QueryRunner();
        return run.query(conn
                         , "SELECT id,name,type,value,mask FROM authorities WHERE value=? and mask =?"
                         , h
                         , value
                         , mask);
    }

    public void save(Connection conn, Authority auth) throws SQLException {
        QueryRunner run = new QueryRunner();
        run.update(conn
                   , "INSERT INTO authorities (name,type,value,mask) values (?,?,?,?)"
                   , auth.getName()
                   , auth.getType()
                   , auth.getValue()
                   , auth.getMask()); 
    }

    public List<Authority> findByType( Connection conn, String type) throws SQLException {
        ResultSetHandler<List<Authority>> h = new BeanListHandler(Authority.class);
        QueryRunner run = new QueryRunner();
        return run.query(conn
                         , "SELECT distinct name,value,type FROM authorities WHERE type =? order by name "
                         , h
                         , type);
    }

    public List<Authority> findByValue( Connection conn, String value) throws SQLException {
        ResultSetHandler<List<Authority>> h = new BeanListHandler(Authority.class);
        QueryRunner run = new QueryRunner();
        return run.query(conn
                         , "SELECT id,name,value,type,mask FROM authorities WHERE value =? "
                         , h
                         , value);
    }

}
