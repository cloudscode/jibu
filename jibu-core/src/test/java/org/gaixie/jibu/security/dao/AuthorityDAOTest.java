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
import java.util.List;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.CoreTestSupport;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.dao.AuthorityDAO;
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.utils.ConnectionUtils;

public class AuthorityDAOTest extends CoreTestSupport {
    private Connection conn;
    private AuthorityDAO authDAO;

    @Before public void setup() throws Exception {
        authDAO = getInjector().getInstance(AuthorityDAO.class); 
        conn = ConnectionUtils.getConnection();
    }


    @Test public void testAuthDAO() throws Exception {
        List<Authority> auths = authDAO.getAll(conn);
        int count = auths.size();

        Authority auth = new Authority("jibu.security.test1","Test1Servlet.z",1);
        authDAO.save(conn,auth);
        auth = new Authority("jibu.security.test2","Test2Servlet.z",1);
        authDAO.save(conn,auth);
        auth = new Authority("jibu.adt.ad","/adt-ad-auth1.z",1);
        authDAO.save(conn,auth);

        auths = authDAO.getAll(conn);
        Assert.assertTrue(count + 3 == auths.size());

        auths = authDAO.findByValue(conn,"Test1Servlet.z");
        Assert.assertTrue(1 == auths.size());

        auths = authDAO.findByName(conn,"jibu.security");
        Assert.assertTrue(2 == auths.size());

        auth = authDAO.get(conn,"Test1Servlet.z",1);
        auth.setValue("Test3Servlet.z");
        authDAO.update(conn, auth);
        auths = authDAO.findByValue(conn,"Test1Servlet.z");
        Assert.assertTrue(0 == auths.size());

        authDAO.delete(conn, auth);
        auths = authDAO.getAll(conn);
        Assert.assertTrue(count + 2 == auths.size());
    }

    @After public void tearDown() {
        DbUtils.rollbackAndCloseQuietly(conn);
    }
}
