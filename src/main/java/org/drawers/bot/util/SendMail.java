package org.drawers.bot.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;

public class SendMail {

    private static final SendMail sendMail = new SendMail();
    private String adminEmail;
    private int sendMailDelay = 15 * 60 * 1000; // ms
    private long lastSendTime = 0L;

    private SendMail() {
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public static SendMail getInstance() {
        return sendMail;
    }

    public void sendMail(String subject, String body)
    {
        if (System.currentTimeMillis() - lastSendTime < sendMailDelay) {
            return;
        }
        lastSendTime = System.currentTimeMillis();
        try {
            final Properties p = new Properties();
            p.put("mail.smtp.host", "localhost");
            final Message msg = new MimeMessage(Session.getDefaultInstance(p));
            msg.setFrom(new InternetAddress("bot@drawers.in"));
            msg.addRecipient(RecipientType.TO, new InternetAddress(adminEmail));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
        } catch (Exception ex) {
            System.err.println("Failed to send email due to:" + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}