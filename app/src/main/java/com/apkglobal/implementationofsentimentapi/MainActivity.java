package com.apkglobal.implementationofsentimentapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Struct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hod.api.hodclient.HODApps;
import hod.api.hodclient.HODClient;
import hod.api.hodclient.IHODClientCallback;
import hod.response.parser.HODErrorCode;
import hod.response.parser.HODErrorObject;
import hod.response.parser.HODResponseParser;
import hod.response.parser.SentimentAnalysisResponse;

public class MainActivity extends AppCompatActivity implements IHODClientCallback {
    String key = "3791adf2-b8d5-4524-b884-758707bb624b";
    HODResponseParser hodParser;
    HODClient hodClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        hodClient = new HODClient(key,this);
        hodParser = new HODResponseParser();
        String hodApp = HODApps.ANALYZE_SENTIMENT;
        Map<String,Object> params =  new HashMap<String,Object>();
        params.put("text", text);
        hodClient.PostRequest(params, hodApp, HODClient.REQ_MODE.SYNC);
    }

    @Override
    public void requestCompletedWithContent(String response) {
        SentimentAnalysisResponse resp = hodParser.ParseSentimentAnalysisResponse(response);

        if (resp != null) {
            String positive = "";
            for (SentimentAnalysisResponse.Entity ent : resp.positive) {
                if (ent.original_text != null)
                    positive += "Statement: " + ent.original_text + "\n";
                if (ent.sentiment != null)
                    positive += "Sentiment: " + ent.sentiment + "\n";
                if (ent.topic != null)
                    positive += "Topic: " + ent.topic + "\n";
                if (ent.score != null)
                    positive += "Score: " + ent.score.toString() + "\n";
            }
            String negative = "";
            for (SentimentAnalysisResponse.Entity ent : resp.negative) {
                if (ent.original_text != null)
                    negative += "Statement: " + ent.original_text + "\n";
                if (ent.sentiment != null)
                    negative += "Sentiment: " + ent.sentiment + "\n";
                if (ent.topic != null)
                    negative += "Topic: " + ent.topic + "\n";
                if (ent.score != null)
                    negative += "Score: " + ent.score.toString() + "\n";
            }
            /*String sentiment = positive;
            sentiment += negative;
            sentiment += "Aggregate: \n" + resp.aggregate.sentiment + "\n";*/
            Double sentiment = resp.aggregate.score;
            parseSentiment(sentiment);

           /* TextView tv = (TextView) findViewById(R.id.trial_tv);
            tv.setText(sentiment);
*/

            //print sentiment result
        } else { // check status or handle error
            List<HODErrorObject> errors = hodParser.GetLastError();
            String errorMsg = "";
            for (HODErrorObject err: errors) {
                if (err.error == HODErrorCode.QUEUED) {
                    // sleep for a few seconds then check the job status again
                    hodClient.GetJobStatus(err.jobID);
                    return;
                } else if (err.error == HODErrorCode.IN_PROGRESS) {
                    // sleep for for a while then check the job status again
                    hodClient.GetJobStatus(err.jobID);
                    return;
                } else {
                    errorMsg += String.format("Error code: %d\nError Reason: %s\n", err.error, err.reason);
                    if (err.detail != null)
                        errorMsg += "Error detail: " + err.detail + "\n";
                }
                // print error message.
            }
        }

    }

    private void parseSentiment(Double sentiment) {
        Double l1,l2,l3,l4,l5,l6;
        l1= Double.valueOf(30);
        l2= Double.valueOf(-30);
        l3= Double.valueOf(-30);
        l4= Double.valueOf(-60);
        l5= Double.valueOf(60);
        /*l6= Double.valueOf(30);*/
        sentiment = sentiment*Double.valueOf(100);

        String text = sentiment.toString();
        if (sentiment<l1&&sentiment>l2)
        {
            text = "You have neutral Sentiment for the Person.";
        }
        else if (sentiment<=l2&&sentiment>=l4)
        {
            text = "You have a quiet negative Sentiment for the Person.";
        }
        else if (sentiment<=l5&&sentiment>=l1)
        {
            text = "You have a quiet positive Sentiment for the Person.";
        }
        else if (sentiment<l4)
        {
            text = "You have highly negative Sentiment for the Person.";
        }
        else if (sentiment>l5)
        {
            text = "You have highly positive Sentiment for the Person.";
        }
        TextView tv = (TextView) findViewById(R.id.trial_tv);
        tv.setText(text);
    }


    @Override
    public void requestCompletedWithJobID(String response) {

    }

    @Override
    public void onErrorOccurred(String errorMessage) {

    }
}
