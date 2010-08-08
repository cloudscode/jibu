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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.gaixie.jibu.security.dao.RoleDAO;
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;

/**
 *  Role DAO接口的Derby实现
 */
public class RoleDAODerby implements RoleDAO {
    private QueryRunner run = null;

    public RoleDAODerby() {
	this.run = new QueryRunner();
    }

    public Role get(Connection conn, int id) throws SQLException {
	ResultSetHandler<Role> h = new BeanHandler(Role.class);
	return run.query(conn
			 , "SELECT id, name, description, lft, rgt FROM roles WHERE id=? "
			 , h
			 , id); 
    }

    public Role get(Connection conn, String name) throws SQLException {
	ResultSetHandler<Role> h = new BeanHandler(Role.class);
	return run.query(conn
			 , "SELECT id, name, description, lft, rgt FROM roles WHERE name=? "
			 , h
			 , name); 
    }

    public void saveChild(Connection conn, Role role, Role parent) throws SQLException {
	if (null==parent) return ;
	run.update(conn
		   , "UPDATE roles set lft=lft+2 where lft > ?"
		   , parent.getLft()); 
	run.update(conn
		   , "UPDATE roles set rgt=rgt+2 where rgt > ?"
		   , parent.getLft()); 
	run.update(conn
		   , "INSERT INTO roles (name,description,lft,rgt) values (?,?,?,?)"
		   , role.getName()
		   , role.getDescription()
		   , parent.getLft()+1
		   , parent.getLft()+2); 
    }

    public List<Role> getAll(Connection conn) throws SQLException {
	ResultSetHandler<List<Role>> h = new BeanListHandler(Role.class);
	return  run.query(conn
			  ,"SELECT id, name, description, lft, rgt "+
			  " FROM roles "+
			  " ORDER BY lft"
			  , h);
    }

    public void bind(Connection conn, Role role, Authority auth) throws SQLException {
	run.update(conn
		   , "INSERT INTO role_authority_map (role_id,authority_id) values (?,?)"
		   , role.getId()
		   , auth.getId()); 

    }

    public List<Role> findByAuthority(Connection conn, Authority auth) throws SQLException {
	ResultSetHandler<List<Role>> h = new BeanListHandler(Role.class);
	return  run.query(conn
			  ,"SELECT node.id, node.name, node.description, node.lft, node.rgt "+
			  " FROM roles AS node, role_authority_map AS ram "+
			  " WHERE node.id = ram.role_id "+
			  " AND ram.authority_id = ? "
			  , h,auth.getId());
    }

    public void bind(Connection conn, Role role, User user) throws SQLException {
	run.update(conn
		   , "INSERT INTO user_role_map (user_id,role_id) values (?,?)"
		   , user.getId()
		   , role.getId()); 
    }

    public List<Role> findByUser(Connection conn, User user) throws SQLException {
	ResultSetHandler<List<Role>> h = new BeanListHandler(Role.class);
	return  run.query(conn
			  ,"SELECT parent.id, parent.name, parent.description, parent.lft, parent.rgt "+
			  " FROM roles AS node, roles AS parent, user_role_map AS urm "+
			  " WHERE node.id = urm.role_id "+
			  " AND node.lft BETWEEN parent.lft AND parent.rgt "+
			  " AND urm.user_id = ? "
			  , h,user.getId());
    }

    public List<String> findByAuthid(Connection conn, int id) throws SQLException {
	ResultSetHandler<List<String>> h = new ResultSetHandler<List<String>>() {
	    public List<String> handle(ResultSet rs) throws SQLException {
		List<String> result = new ArrayList<String>();
		while(rs.next()) {
		    result.add(rs.getString(1));
		}
		int len = result.size();
		if (len <=0) return null;
		return result;
	    }
	};
	return  run.query(conn
			  ,"SELECT r.name "+
			  " FROM roles AS r, role_authority_map AS ram "+
			  " WHERE r.id = ram.role_id "+
			  " AND ram.authority_id =? "
			  , h
                          , id);
    }

    public List<String> findByUsername(Connection conn, String username) throws SQLException {
	ResultSetHandler<List<String>> h = new ResultSetHandler<List<String>>() {
	    public List<String> handle(ResultSet rs) throws SQLException {
		List<String> result = new ArrayList<String>();
		while(rs.next()) {
		    result.add(rs.getString(1));
		}
		int len = result.size();
		if (len <=0) return null;
		return result;
	    }
	};

	return  run.query(conn
			  ,"SELECT parent.name "+
			  " FROM roles AS node, roles AS parent, user_role_map AS urm, userbase u "+
			  " WHERE node.id = urm.role_id "+
			  " AND node.lft BETWEEN parent.lft AND parent.rgt "+
			  " AND u.id = urm.user_id " +
			  " AND u.username = ? "
			  , h,username);
    }

}
