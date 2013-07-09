import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/**
 * Simple email class.
 *
 * @author frank hanner
 */
public class Emailer {
    private final Logger LOG = Logger.getLogger(Emailer.class);

    private String host;

    public boolean sendEmail(String[] to, String from, String subject, String body){
        return sendEmail(to, from, subject, body, null);
    }

    public boolean sendEmail(String[] to, String from, String subject, String body, File attachment) {
        boolean sent = false;

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        LOG.info("Preparing to send message...");
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            for (String name : to)
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(name));

            // Set Subject: header field
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(body);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            if (attachment != null) {
                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                messageBodyPart.setDataHandler(new DataHandler(source));
                String filename = attachment.getName();
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
            }

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            sent = true;
            LOG.info("Sent message successfully....");
        } catch (MessagingException mex) {
            LOG.error("Unable to send message....");
            mex.printStackTrace();
        }
        return sent;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
