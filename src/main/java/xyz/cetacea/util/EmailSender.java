package xyz.cetacea.util;

import xyz.cetacea.Application;
import xyz.cetacea.data.tables.pojos.Groups;
import xyz.cetacea.data.tables.pojos.Journals;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.endpoints.JournalServlet;
import xyz.cetacea.queries.UserGroupLinksQueries;
import xyz.cetacea.queries.UsersQueries;
import org.joda.time.DateTime;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.mail.*;
import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by David on 10/24/2017.
 */
public class EmailSender {

    private static final String FROM = "noreply@cetacea.xyz";
    private static final String SUBJECT_PREFIX = "Daily Journal";

    public static void sendEmail(Groups group) {
        String FROM_NAME = group.getName();
        if (!checkMailEnvironmentVairables())
        {
            return;
        }

        try {
            List<Integer> userIds = UserGroupLinksQueries.getUserIdsByGroupId(group.getId());
            List<String> TO = UsersQueries.getEmailsByUserIds(userIds);

            DateTime today = DateTime.now();

            String SUBJECT = group.getName().trim() + " " + SUBJECT_PREFIX + " for " + (today.getMonthOfYear()) + "/" + today.getDayOfMonth(); //TODO: Missing Date
            List<UserJournal> journals = getJournalsByEmails(TO); //TODO: Pass in group time
            if (journals.size() == 0){
                System.out.println("No journals to send for Group: " + group.toString());
                return;
            }
            String BODY = formatJournalsToEmail(journals);

            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", Environment.MAIL_PORT);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props);

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM, FROM_NAME));
            TO.forEach(em -> addRecipient(message,em));
            message.setSubject(SUBJECT);
            message.setText(BODY, "UTF-8");
            try (Transport transport = session.getTransport()) {
                transport.connect(Environment.MAIL_HOST, Environment.MAIL_USER, Environment.MAIL_PASSWORD);
                transport.sendMessage(message, message.getAllRecipients());
            } catch (MessagingException mex) {
                System.out.println("Email wasn't sent");
                mex.printStackTrace();
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

    private static List<UserJournal> getJournalsByEmails(List<String> emails) throws ServletException{
        ArrayList<UserJournal> journals = new ArrayList<>();
        if (emails.size() == 0){
            return journals;
        }

        OffsetDateTime end = OffsetDateTime.now().withHour(11).withMinute(0)
                .withSecond(0).withNano(0).withOffsetSameLocal(ZoneOffset.UTC); //TODO: edit to be parameter
        OffsetDateTime start = end.minusDays(1);
        List<Users> users = UsersQueries.getUsersByEmails(emails);

        users.forEach(u -> {
            try {
                Journals journalRecord = JournalServlet.getJournalById(u.getId(), start, end);
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

    private static boolean checkMailEnvironmentVairables() {
        if (Environment.MAIL_USER == null) {
            System.out.println("Missing SMTP username, please set the CETACEA_MAIL_USER environment variable");
            return false;
        }
        if (Environment.MAIL_PASSWORD == null) {
            System.out.println("Missing SMTP password, please set the CETACEA_MAIL_PASSWORD environment variable");
            return false;
        }
        if (Environment.MAIL_HOST == null) {
            System.out.println("Missing SMTP host, please set the CETACEA_MAIL_HOST environment variable");
            return false;
        }
        return true;
    }
}
