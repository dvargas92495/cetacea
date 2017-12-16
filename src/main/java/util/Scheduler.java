package main.java.util;

import main.java.Application;
import main.java.data.tables.pojos.Groups;
import main.java.endpoints.EmailServlet;
import main.java.endpoints.GroupServlet;

import javax.servlet.ServletException;
import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static main.java.data.Tables.*;

/**
 * Created by David on 11/1/2017.
 */
public class Scheduler {

    //Change pool size based on AWS
    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private static class Master implements Runnable {
        public void run(){
            System.out.println(new Date() + ": Scheduling Emails");
            try {
                List<Groups> groups = GroupServlet.getAllGroups();
                groups.forEach(g -> {
                    LocalTime timeToSend = LocalTime.of(11, 0); //TODO: Get from group configuration
                    long delay = Application.PRODUCTION.equals(Application.ENVIRONMENT) ?
                            computeNextDelay(timeToSend.getHour(), timeToSend.getMinute(), timeToSend.getSecond()): 0;
                    executorService.schedule(
                        new EmailSender(g),
                        delay,
                        TimeUnit.SECONDS
                    );
                });
            } catch(ServletException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class EmailSender implements Runnable {
        private Groups group;
        EmailSender(Groups g) {
            group = g;
        }
        public void run() {
            System.out.println(new Date() + ": Sending Email to " + group.toString());
            EmailServlet.sendEmail(group);
        }
    }

    public static void init(){
        long delay = Application.PRODUCTION.equals(Application.ENVIRONMENT) ? computeNextDelay(3, 0, 0): 0;
        executorService.scheduleAtFixedRate(
            new Master(),
            delay,
            TimeUnit.SECONDS.convert(1, TimeUnit.DAYS),
            TimeUnit.SECONDS
        );
    }

    private static long computeNextDelay(int targetHour, int targetMin, int targetSec)
    {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.systemDefault();
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
        if(zonedNow.compareTo(zonedNextTarget) > 0) {
            zonedNextTarget = zonedNextTarget.plusDays(1);
        }
        Duration duration = Duration.between(zonedNow, zonedNextTarget);
        return duration.getSeconds();
    }

}
