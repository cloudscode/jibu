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


/**
 * A base exception class for Jibu.
 */
public class JibuException extends Exception {
    public JibuException() {
        super();
    }
    
    
    /**
     * Construct JibuException with message string.
     *
     * @param s Error message string.
     */
    public JibuException(String s) {
        super(s);
    }
    
    
    /**
     * Construct JibuException, wrapping existing throwable.
     *
     * @param s Error message
     * @param t Existing connection to wrap.
     */
    public JibuException(String s, Throwable t) {
        super(s, t);
    }
    
    
    /**
     * Construct JibuException, wrapping existing throwable.
     *
     * @param t Existing exception to be wrapped.
     */
    public JibuException(Throwable t) {
        super(t);
    }
}
