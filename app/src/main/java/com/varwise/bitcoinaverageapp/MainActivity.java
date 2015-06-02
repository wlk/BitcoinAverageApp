package com.varwise.bitcoinaverageapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import hotchemi.android.rate.AppRate;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private static List<String> currencyOptions = Arrays.asList("AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYR", "BZD", "CAD", "CDF", "CHF", "CLF", "CLP", "CNY", "COP", "CRC", "CUC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EEK", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LTL", "LVL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MTL", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "STD", "SVC", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XAG", "XAU", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMK", "ZMW", "ZWL");
    private InterstitialAd interstitial;
    private Tracker t;
    Spinner currencySpinner;
    TextView avg24hValue, askValue, bidValue, lastValue, volumeBTCValue, timestamp;
    SharedPreferences preferences;
    public static GoogleAnalytics analytics;
    private AdView adView;
    private boolean adsEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currencySpinner = (Spinner) findViewById(R.id.currencySpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyOptions);
        currencySpinner.setOnItemSelectedListener(this);
        currencySpinner.setAdapter(spinnerAdapter);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int spinnerSelection = preferences.getInt("spinnerSelection", 149);

        currencySpinner.setSelection(spinnerSelection);

        avg24hValue = (TextView) findViewById(R.id.avg24hValue);
        askValue = (TextView) findViewById(R.id.askValue);
        bidValue = (TextView) findViewById(R.id.bidValue);
        lastValue = (TextView) findViewById(R.id.lastValue);
        volumeBTCValue = (TextView) findViewById(R.id.volumeBTCValue);
        timestamp = (TextView) findViewById(R.id.timestampValue);

        if (adsEnabled) {
            adView = (AdView) findViewById(R.id.adViewMainScreen);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("0457F45F2F3B38D51216287AD98A2C3D").addTestDevice("3AC2DCEE575018317C028D0C93F19AD0").addTestDevice("2D7D6AE8606296EB97A2A9B3681B90F6").build();
            adView.loadAd(adRequest);
        }

        setupGoogleAnalytics();

        maybeShowAppRate();

        maybeShowInterstitial();
    }

    private void setupGoogleAnalytics() {
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);
        t = analytics.newTracker(getResources().getString(R.string.googleAnalytics));
        t.enableExceptionReporting(true);
        t.enableAdvertisingIdCollection(true);
        t.enableAutoActivityTracking(true);
    }

    private void maybeShowAppRate() {
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(2)
                .setRemindInterval(1)
                .setShowNeutralButton(true)
                .setDebug(false)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
    }

    private void maybeShowInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("0457F45F2F3B38D51216287AD98A2C3D").addTestDevice("3AC2DCEE575018317C028D0C93F19AD0").addTestDevice("2D7D6AE8606296EB97A2A9B3681B90F6").build();

        int appRuns = preferences.getInt("appRuns", 0);
        preferences.edit().putInt("appRuns", ++appRuns).apply();

        if (appRuns > 10 && appRuns % 10 == 0) {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId("ca-app-pub-5829945009169600/2217384767");
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    interstitial.show();
                }
            });

            interstitial.loadAd(adRequest);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AboutDialog about = new AboutDialog(this);
            about.setTitle("About Bitcoin Average App");
            about.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        t.send(new HitBuilders.EventBuilder().setCategory("MainActivity").setAction("click").setLabel("onItemSelected").build());

        preferences.edit().putInt("spinnerSelection", currencySpinner.getSelectedItemPosition()).apply();

        GetTickerTask gtt = new GetTickerTask(this);
        gtt.execute(currencySpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onTickerUpdated(Ticker ticker) {
        avg24hValue.setText(currencyFormat(ticker.avg24h) + " " + currencySpinner.getSelectedItem().toString());
        askValue.setText(currencyFormat(ticker.ask) + " " + currencySpinner.getSelectedItem().toString());
        bidValue.setText(currencyFormat(ticker.bid) + " " + currencySpinner.getSelectedItem().toString());
        lastValue.setText(currencyFormat(ticker.last) + " " + currencySpinner.getSelectedItem().toString());
        volumeBTCValue.setText(currencyFormat(ticker.volume_btc) + " BTC");
        timestamp.setText(ticker.timestamp.split(" -")[0]);

        if (ticker.ask.equals(BigDecimal.ZERO)) {
            Toast.makeText(MainActivity.this, "There was a problem connecting to Bitcoin Average. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }

    public static String currencyFormat(BigDecimal n) {
        return n.setScale(2, BigDecimal.ROUND_DOWN).toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adsEnabled) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adsEnabled) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adsEnabled) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
