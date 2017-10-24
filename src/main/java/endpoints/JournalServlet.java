package main.java.endpoints;

import static main.java.data.Tables.*;

import main.java.Repository;
import main.java.util.RequestHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Created by David on 9/25/2017.
 */
public class JournalServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        DSLContext create = Repository.getContext();
        if (create == null){
            return;
        }
        int queryId = Integer.parseInt(request.getParameter("id"));
        OffsetDateTime[] dateRange = getDateRange();
        Result<Record> result = create.select()
                .from(JOURNALS)
                .where(JOURNALS.USER_ID.eq(queryId))
                .and(JOURNALS.TIMESTAMP.ge(dateRange[0]))
                .and(JOURNALS.TIMESTAMP.le(dateRange[1]))
                .fetch();
        for (Record r : result) {
            Integer id = r.getValue(JOURNALS.ID);
            String je = r.getValue(JOURNALS.ENTRY);
            //Date ts = r.getValue(JOURNALS.TIMESTAMP);

            System.out.println("ID: " + id + " first name: " + je + " last name: " + "ts");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);
        DSLContext create = Repository.getContext();
        if (create == null){
            return;
        }
        String entry = params.get("entry");
        OffsetDateTime timestamp = OffsetDateTime.parse(params.get("timestamp"));
        int user_id = Integer.parseInt(params.get("user_id"));
        create.insertInto(JOURNALS, JOURNALS.ENTRY, JOURNALS.TIMESTAMP, JOURNALS.USER_ID)
                .values(entry, timestamp, user_id)
                .execute();
    }

    private OffsetDateTime[] getDateRange(){
        //TODO: Get Day based on input^
        //TODO: Get Time based on Group configuration
        OffsetDateTime[] dateRange = new OffsetDateTime[2];
        LocalDate today = LocalDate.of(2017, 10, 24);
        LocalDateTime start = today.atTime(6, 0);
        LocalDateTime end = today.atTime(6, 0).plusDays(1);
        //dateRange[0] = OffsetDateTime.of(2017, 10, 24, 6, 0, 0 ,0);
        //dateRange[1] = OffsetDateTime.of(end.toLocalDate());
        return dateRange;
    }
}
