package main.java.endpoints;

import main.java.Application;
import main.java.data.tables.pojos.Groups;
import main.java.data.tables.pojos.Journals;
import main.java.data.tables.pojos.Users;
import main.java.util.Repository;
import org.joda.time.DateTime;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.mail.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static main.java.data.Tables.USERS;

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
            DateTime today = DateTime.now();

            String SUBJECT = SUBJECT_PREFIX + " for " + (today.getMonthOfYear()) + "/" + today.getDayOfMonth(); //TODO: Missing Date
            List<UserJournal> journals = getJournalsByEmails(TO);
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

    static class UserJournal{
        private String entry;
        private String firstname;
        private String lastname;
        UserJournal(String e, String f, String l){
            entry = e;
            firstname = f;
            lastname = l;
        }
        String getEntry(){
            return entry;
        }
        String getFirstname(){
            return firstname;
        }
        String getLastname(){
            return lastname;
        }
    }

    static List<UserJournal> getJournalsByEmails(List<String> emails) throws ServletException{
        ArrayList<UserJournal> journals = new ArrayList<>();
        if (emails.size() == 0){
            return journals;
        }
        List<Users> users = Repository.getDsl().selectFrom(USERS)
                .where(USERS.EMAIL.in(emails))
                .fetchInto(Users.class);

        users.forEach(u -> {
            try {
                Journals journalRecord = JournalServlet.getJournalById(u.getId());
                if (journalRecord != null) {
                    UserJournal uj = new UserJournal(journalRecord.getEntry(), u.getFirstName(), u.getLastName());
                    journals.add(uj);
                }
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        });
        return journals;
    }

    private static String formatJournalsToEmail(List<UserJournal> journals) {
        StringBuilder sb = new StringBuilder();
        journals.forEach(j ->
            sb.append(j.getFirstname())
              .append(" ")
              .append(j.getLastname())
              .append(":\n\n")
              .append(j.getEntry())
              .append("\n\n\n")
        );
        return sb.toString();
    }

    private static void addRecipient(MimeMessage msg, String to){
        try {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        } catch (MessagingException e) {
            System.out.println(String.format("Error adding recipient %s",to));
        }
    }
}
