package main.java.util;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by David on 11/1/2017.
 */
public class Scheduler extends TimerTask{

    public void run(){
        System.out.println(new Date() + ": Sending Scheduled Emails");

    }

    public static void main(String[] args){
        System.out.println("ooooh another main file");
    }

}
