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
package org.gaixie.jibu.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * JavaMail class.
 */
public class JavaMailSender {

    /**
     * 通过 javax.mail 发送邮件。
     * <p>
     *
     * @param from 发送方邮件地址。
     * @param recipients 接收方邮件地址，多个邮件地址以逗号分隔。   
     * @param subj 邮件标题。
     * @param text 邮件正文。
     */
    public void sendEmail(String from, String recipients, String subj, String text) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            InternetAddress[] address = InternetAddress.parse(recipients);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipients(Message.RecipientType.TO,address);
            msg.setSubject(subj);
            msg.setText(text);
            Transport.send(msg);
        } catch (AddressException e) {
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    } 
}