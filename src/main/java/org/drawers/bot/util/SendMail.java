package org.drawers.bot.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;

public class SendMail {
    public static void sendMail(String from, String to, String subject, String body)
    {
        try {
            final Properties p = new Properties();
            p.put("mail.smtp.host", "localhost");
            final Message msg = new MimeMessage(Session.getDefaultInstance(p));
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
        } catch (Exception ex) {
            System.err.println("Failed to send email due to:" + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}