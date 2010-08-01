/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gaixie.jibu.itest;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public final class JettyService implements Runnable {
    private final WebAppContext webapp;

    private String warpath;
    private boolean running;
    private Thread thread;
    private Server server;

    public JettyService(String warpath) {
        this.warpath = warpath;
        this.webapp = new WebAppContext();
    }
    
    public void start() throws Exception {
        this.thread = new Thread(this, "Jetty HTTP Service");
        this.thread.start();
    }

    public void stop() throws Exception {
        this.running = false;
        this.thread.interrupt();

        try {
            this.thread.join(3000);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

    private void startJetty() {
        try {
	    this.server = new Server(8080);
            this.webapp.setContextPath("/");
            this.webapp.setWar(warpath);
            this.server.setHandler(webapp);
	    this.server.start();
            //this.server.join();

        } catch (Exception e) {
            System.out.println("Exception while initializing Jetty.");
        }
    }

    private void stopJetty() {
        try {
            this.server.stop();
        } catch (Exception e) {
            System.out.println("Exception while stopping Jetty.");
        }
    }

    public boolean isStarted(){
        if (null != this.server)
            return this.server.isStarted();
        return false;
    }

    public void run() {
        this.running = true;
        while (this.running) {
	    try {
		Thread.sleep( 1000 ); // simple polling for now
	    }
	    catch( InterruptedException e ) {}

            startJetty();

            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {}
            }

            stopJetty();
        }
    }
}
