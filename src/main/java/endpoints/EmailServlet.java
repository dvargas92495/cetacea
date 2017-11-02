package main.java.endpoints;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.mail.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by David on 10/24/2017.
 */
public class EmailServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Sending Emails");
        ArrayList<Integer> groupIds = GroupServlet.getAllGroupIds();
        groupIds.forEach(EmailServlet::sendEmail);
    }

    private static void sendEmail(int groupId) {
        String FROM = "noreply@cetacea.xyz"; //TODO: Magic String
        String FROMNAME = "No Reply"; //TODO: Groupname
        ArrayList<String> TO = GroupServlet.getEmailAddressesByGroup(groupId);

        String SMTP_USERNAME = System.getenv("CETACEA_MAIL_USER");
        if (SMTP_USERNAME == null) {
            System.out.println("Missing SMTP username, please set the CETACEA_MAIL_USER environment variable");
            return;
        }
        String SMTP_PASSWORD = System.getenv("CETACEA_MAIL_PASSWORD");
        if (SMTP_PASSWORD == null) {
            System.out.println("Missing SMTP password, please set the CETACEA_MAIL_PASSWORD environment variable");
            return;
        }

        String HOST = "email-smtp.us-east-1.amazonaws.com"; //TODO: Magic String
        int PORT = 587; //TODO: Magic Int

        String SUBJECT = "Daily Journal"; //TODO: Magic String & Missing Date
        ArrayList<String> journals = JournalServlet.getJournalsByEmails(TO);
        String BODY = formatJournalsToEmail(journals);

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM, FROMNAME));
            TO.forEach(em -> addRecipient(message,em));
            message.setSubject(SUBJECT);
            message.setText(BODY);
            Transport transport = session.getTransport();
            try {
                transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
                transport.sendMessage(message, message.getAllRecipients());
            } catch (MessagingException mex) {
                System.out.println("Email wasn't sent");
                mex.printStackTrace();
            } finally {
                if (transport != null) {transport.close();}
            }
        }catch (MessagingException | UnsupportedEncodingException mex) {
            mex.printStackTrace();
        }
    }

    private static String formatJournalsToEmail(ArrayList<String> journals) {
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
