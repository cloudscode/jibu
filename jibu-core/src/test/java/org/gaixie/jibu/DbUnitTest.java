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
package org.gaixie.jibu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


import org.apache.commons.dbutils.QueryRunner;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.gaixie.jibu.utils.ConnectionUtils;

/*
 * 在所有测试用力执行以前，先执行此用例，目的是初始化数据库，建表，插入初始数据。
 * 数据库配置通过ConnectionUtils类加载dbcp.properties
 * dbunit 在对hsqldb数据库会有一个警告，目前对derby支持的最好。
 */
public class DbUnitTest extends DatabaseTestCase {
    private FlatXmlDataSet loadedDataSet;

    // Provide a connection to the database
    protected IDatabaseConnection getConnection() throws Exception {
        // 必须设置dbcp的autocommit为true，否则 getDataSet() 方法不能够插入初始化数据。
        ConnectionUtils.setDefaultAutoCommit(true);
        Connection conn = ConnectionUtils.getConnection();
        createDatabase(conn);
        return new DatabaseConnection(conn);
    }

    protected IDataSet getDataSet() throws Exception {
        loadedDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream("/dataset.xml"));
        return loadedDataSet;
    }

    // Check that the data has been loaded.
    public void testCheckDataLoaded() throws Exception{
        assertNotNull(loadedDataSet);
        int rowCount = loadedDataSet.getTable("USERBASE").getRowCount();
        assertEquals(1, rowCount);
    }

    private void createDatabase(Connection conn) throws SQLException, IOException {
        StringBuilder command = new StringBuilder();
        InputStream is = this.getClass().getResourceAsStream("/dbscripts/derby/createdb.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        QueryRunner run = new QueryRunner();
        String line;
        while ((line = reader.readLine()) != null) {
            command = handleLine(command, line, run, conn);
        }
        reader.close();
        checkForMissingLineTerminator(command);
    }

    private StringBuilder handleLine(StringBuilder command 
                                     ,String line
                                     ,QueryRunner run
                                     ,Connection conn) throws SQLException {

        String trimmedLine = line.trim();
        if (isComment(trimmedLine)) {
            //System.out.println(command);
        } else if (trimmedLine.endsWith(";")) {
            command.append(line.substring(0, line.lastIndexOf(";")));
            command.append(" ");
            run.update(conn, command.toString()); 
            command.setLength(0);
        } else if (trimmedLine.length() > 0) {
            command.append(line);
            command.append(" \n");
        }
        return command;
    }

    private boolean isComment(String trimmedLine) {
        return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
    }

    private void checkForMissingLineTerminator(StringBuilder command) 
        throws SQLException {
        if (command != null && command.toString().trim().length() > 0) {
            throw new SQLException("Missing end-of-line terminator (;) => " + command);
        }
    }
}
