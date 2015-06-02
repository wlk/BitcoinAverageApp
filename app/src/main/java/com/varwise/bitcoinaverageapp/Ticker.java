package com.varwise.bitcoinaverageapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Ticker {
    public final BigDecimal avg24h, ask, bid, last, volume_btc, volume_percent;
    public final String timestamp;

    public Ticker(BigDecimal avg24h, BigDecimal ask, BigDecimal bid, BigDecimal last, BigDecimal volume_btc, BigDecimal volume_percent, String timestamp) {
        this.avg24h = avg24h;
        this.ask = ask;
        this.bid = bid;
        this.last = last;
        this.volume_btc = volume_btc;
        this.volume_percent = volume_percent;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Ticker(" + avg24h + ")";
    }

    public static Ticker fromJson(JSONObject jObj) throws JSONException {
        return new Ticker(
                new BigDecimal(jObj.optDouble("24h_avg", 0.00)),
                new BigDecimal(jObj.optDouble("ask", 0.00)),
                new BigDecimal(jObj.optDouble("bid", 0.00)),
                new BigDecimal(jObj.optDouble("last", 0.00)),
                new BigDecimal(jObj.optDouble("volume_btc", 0.00)),
                new BigDecimal(jObj.optDouble("volume_percent", 0.00)),
                jObj.getString("timestamp")
        );
    }

    public static Ticker defaultTicker() {
        return new Ticker(BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "No data");

    }
}
