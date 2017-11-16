package main.java.endpoints;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.handlers.TracingHandler;
import main.java.Application;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by David on 10/14/2017.
 */
public class TraceServlet extends HttpServlet {

    private static final String REGION_NAME = Regions.getCurrentRegion() != null ? Regions.getCurrentRegion().getName():"local";
    private static final String BUCKET_NAME = String.format("elasticbeanstalk-samples-%s", REGION_NAME);
    private static final String OBJECT_KEY = "java-sample-app-v2.zip";
    // Create AWS clients instrumented with AWS XRay tracing handler

    private static final AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(REGION_NAME)
            .withRequestHandlers(new TracingHandler())
            .build();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        if (Application.XRAY_ENABLED) {
            traceS3();
        }
    }

    private void traceS3() {
        // Add subsegment to current request to track call to S3
        Subsegment subsegment = AWSXRay.beginSubsegment("## Getting object metadata");
        try {
            // Gets metadata about this sample app object in S3
            s3Client.getObjectMetadata(BUCKET_NAME, OBJECT_KEY);
        } catch (Exception ex) {
            subsegment.addException(ex);
        } finally {
            AWSXRay.endSubsegment();
        }
    }
}
