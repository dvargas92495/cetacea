package xyz.cetacea.endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import xyz.cetacea.data.tables.pojos.*;
import xyz.cetacea.queries.JournalsQueries;
import xyz.cetacea.util.RequestHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 9/25/2017.
 */
public class JournalServlet extends HttpServlet{

    // get journals
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String idParam = request.getParameter("id");
        String date = request.getParameter("date");

        // return 1 journal for 1 user from a specific date
        if (date != null && idParam != null) {
            int userId = Integer.parseInt(idParam);
            OffsetDateTime day = OffsetDateTime.parse(date);

            OffsetDateTime from = day.withHour(7).withMinute(0).withSecond(0);
            OffsetDateTime to = day.plusDays(1).withHour(7).withMinute(0).withSecond(0);

            Journals journal = getJournalById(userId, from, to);
            response.getWriter().println(new Gson().toJson(journal, Journals.class));
            return;

        }
        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject resBody = new JsonObject();
            resBody.addProperty("isError", true);
            resBody.addProperty("message", "No id sent on journal get");
            response.getWriter().println(resBody.toString());
            return;
        }
        int queryId;
        try {
            queryId = Integer.parseInt(idParam);
        } catch(NumberFormatException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject resBody = new JsonObject();
            resBody.addProperty("isError", true);
            resBody.addProperty("message", "Invalid id sent on journal get");
            response.getWriter().println(resBody.toString());
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        Journals journalRecord = getJournalById(queryId);
        if (journalRecord == null){
            journalRecord = new Journals();
        }
        response.getWriter().println(new Gson().toJson(journalRecord, Journals.class));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);
        String entry = params.get("entry");
        OffsetDateTime timeFromClient = OffsetDateTime.parse(params.get("timestamp"));
        Timestamp timestamp = Timestamp.valueOf(timeFromClient.toLocalDateTime());
        int userId = Integer.parseInt(params.get("user_id"));
        Journals journalRecord = getJournalById(userId);
        if (journalRecord != null) {
            JournalsQueries.updateJournal(entry, timestamp, journalRecord.getId());
        } else {
            JournalsQueries.createJournal(entry, timestamp, userId);
        }
    }

    static Journals getJournalById(int userId) throws ServletException{
        OffsetDateTime[] dateRange = getDateRange();
        return getJournalById(userId, dateRange[0], dateRange[1]);
    }

    static Journals getJournalById(int userId, OffsetDateTime start, OffsetDateTime end) throws ServletException{
        System.out.println("Getting a journal for " + userId + " between " + start.toString() + " and " + end.toString());
        Timestamp t0 = Timestamp.valueOf(start.toLocalDateTime());
        Timestamp t1 = Timestamp.valueOf(end.toLocalDateTime());
        return JournalsQueries.getJournalForUserBetweenTimestamps(userId, t0, t1);
    }

    private static OffsetDateTime[] getDateRange(){
        //TODO: Get range based on Group configuration (Daily, Weekly, etc...)
        LocalTime timeToSend = LocalTime.of(11, 0); //TODO: Get from group configuration
        OffsetDateTime[] dateRange = new OffsetDateTime[2];
        dateRange[1] = OffsetDateTime.now();
        dateRange[1] = dateRange[1].minusSeconds(dateRange[1].getOffset().getTotalSeconds());
        dateRange[0] = dateRange[1].withHour(timeToSend.getHour())
                                   .withMinute(timeToSend.getMinute())
                                   .withSecond(timeToSend.getSecond())
                                   .withNano(0);
        if (dateRange[0].compareTo(dateRange[1]) >= 0){
            dateRange[0] = dateRange[0].minusDays(1);
        }
        return dateRange;
    }
}
