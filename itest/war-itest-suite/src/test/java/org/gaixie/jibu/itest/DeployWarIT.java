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
package org.gaixie.jibu.itest;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.GetMethodWebRequest;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.gaixie.jibu.itest.JettyService;

public class DeployWarIT {
    private static JettyService server;
    @BeforeClass
        public static void beforeClass() throws Exception {

        server = new JettyService(System.getProperty("warPath"));
        server.start();
        boolean started = server.isStarted();
        while (!started) {
	    try {
		Thread.sleep( 1000 ); // simple polling for now
                started = server.isStarted();                
	    }
	    catch( InterruptedException e ) {
            }
        }
    }


    @Test
        public void test1() throws Exception {
        WebConversation wc = new WebConversation();
        WebRequest req = new GetMethodWebRequest( "http://localhost:8080/LoginServlet.x" );
        req.setParameter("ci","login");
        req.setParameter("username","admin");
        req.setParameter("password","123456");

        WebResponse wr = wc.getResponse(req);
        System.out.println(wr.getText());
    }

    @AfterClass
        public static void afterClass() throws Exception {
        server.stop();
    }
}
