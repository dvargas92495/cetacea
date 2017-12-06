package main.java.endpoints;

import main.java.Application;
import main.java.data.tables.pojos.Groups;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.mail.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by David on 10/24/2017.
 */
public class EmailServlet extends HttpServlet {

    private static final String FROM = "noreply@cetacea.xyz";
    private static final String SUBJECT_PREFIX = "Daily Journal";

    public static void sendEmail(Groups group) {
        String FROMNAME = group.getName();

        if (Application.MAIL_USER == null) {
            System.out.println("Missing SMTP username, please set the CETACEA_MAIL_USER environment variable");
            return;
        }
        if (Application.MAIL_PASSWORD == null) {
            System.out.println("Missing SMTP password, please set the CETACEA_MAIL_PASSWORD environment variable");
            return;
        }

        String HOST = "email-smtp.us-east-1.amazonaws.com"; //TODO: Magic String
        int PORT = 587; //TODO: Magic Int

        try {
            List<String> TO = GroupServlet.getEmailAddressesByGroup(group.getId());

            String SUBJECT = SUBJECT_PREFIX; //TODO: Missing Date
            List<String> journals = JournalServlet.getJournalsByEmails(TO);
            if (journals.size() == 0){
                System.out.println("No journals to send for Group: " + group.toString());
                return;
            }
            String BODY = formatJournalsToEmail(journals);

            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", PORT);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props);

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM, FROMNAME));
            TO.forEach(em -> addRecipient(message,em));
            message.setSubject(SUBJECT);
            message.setText(BODY);
            Transport transport = session.getTransport();
            try {
                if (Application.PRODUCTION.equals(Application.ENVIRONMENT)) {
                    transport.connect(HOST, Application.MAIL_USER, Application.MAIL_PASSWORD);
                    transport.sendMessage(message, message.getAllRecipients());
                } else {
                    System.out.println(message.getContent());
                    System.out.println(Arrays.asList(message.getAllRecipients()));
                }
            } catch (MessagingException | IOException mex) {
                System.out.println("Email wasn't sent");
                mex.printStackTrace();
            } finally {
                if (transport != null) {transport.close();}
            }
        }catch (MessagingException | UnsupportedEncodingException | ServletException mex) {
            mex.printStackTrace();
        }
    }

    private static String formatJournalsToEmail(List<String> journals) {
        return String.join("\n\n\n", journals);
    }

    private static void addRecipient(MimeMessage msg, String to){
        try {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        } catch (MessagingException e) {
            System.out.println(String.format("Error adding recipient %s",to));
        }
    }
}
