package com.varwise.bitcoinaverageapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GetTickerTask extends AsyncTask<String, Void, Ticker> {
    private static final String API_BASE = "https://api.bitcoinaverage.com/ticker/global/";
    private MainActivity mainActivity;

    public GetTickerTask(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPostExecute(Ticker ticker) {
        mainActivity.onTickerUpdated(ticker);
    }

    @Override
    protected Ticker doInBackground(String... currencyCode) {
        StringBuilder builder = new StringBuilder();
        HttpClient httpclient = new DefaultHttpClient();
        InputStream content = null;
        try {
            HttpResponse httpResponse = httpclient.execute(new HttpGet(API_BASE + currencyCode[0] + "/"));

            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e("GetTickerTask", "Failed to download file");
            }

            JSONObject jObject = new JSONObject(builder.toString());
            Ticker t = Ticker.fromJson(jObject);
            return t;

        } catch (Exception e) {
            Log.d("GetTickerTask", e.getLocalizedMessage());
        } finally {
            try {
                if (content != null) content.close();
            } catch (Exception squish) {
            }
        }
        return Ticker.defaultTicker();
    }
}
