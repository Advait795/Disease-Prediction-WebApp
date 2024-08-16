/*Author: Adwait Dalvi ad918
 * All the mail features received from eamilservlet are incorporated and sends an email. 
 */

package diseasewebapp;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//sendEmail class takes all mail server details and generates Email and sends it 
public class sendEmail {
    // all email features
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String Email_User = "CareBioMed07@gmail.com";
    private static final String Email_Password = "vicb zhsp esxx gnib";

    public void sendEmail(String to, String subject, String body) throws Exception {

        // setup email server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Email_User, Email_Password);
            }
        });

        // Create a email, From:EmailID, subject, email body
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Email_User));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setContent(body, "text/html");

        Transport.send(message);

    }

}