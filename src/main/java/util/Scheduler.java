package main.java.util;

import main.java.endpoints.EmailServlet;
import main.java.endpoints.GroupServlet;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by David on 11/1/2017.
 */
public class Scheduler extends TimerTask{

    public void run(){
        System.out.println(new Date() + ": Sending Scheduled Emails");
        try {
            ArrayList<Integer> groupIds = GroupServlet.getAllGroupIds();
            groupIds.forEach(EmailServlet::sendEmail);
        } catch(ServletException ex) {
            ex.printStackTrace();
        }
    }

}
