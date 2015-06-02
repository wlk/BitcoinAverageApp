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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private static List<String> currencyOptions = Arrays.asList("AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYR", "BZD", "CAD", "CDF", "CHF", "CLF", "CLP", "CNY", "COP", "CRC", "CUC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EEK", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LTL", "LVL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MTL", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "STD", "SVC", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XAG", "XAU", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMK", "ZMW", "ZWL");

    Spinner currencySpinner;
    TextView avg24hValue, askValue, bidValue, lastValue, volumeBTCValue, timestamp;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currencySpinner = (Spinner) findViewById(R.id.currencySpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyOptions);
        currencySpinner.setOnItemSelectedListener(this);
        currencySpinner.setAdapter(spinnerAdapter);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int spinnerSelection = preferences.getInt("spinnerSelection", 150);

        currencySpinner.setSelection(spinnerSelection);

        avg24hValue = (TextView)findViewById(R.id.avg24hValue);
        askValue = (TextView)findViewById(R.id.askValue);
        bidValue = (TextView)findViewById(R.id.bidValue);
        lastValue = (TextView)findViewById(R.id.lastValue);
        volumeBTCValue = (TextView)findViewById(R.id.volumeBTCValue);
        timestamp = (TextView)findViewById(R.id.timestampValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        preferences.edit().putInt("spinnerSelection", currencySpinner.getSelectedItemPosition()).apply();

        GetTickerTask gtt = new GetTickerTask(this);
        gtt.execute(currencySpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onTickerUpdated(Ticker ticker) {
        avg24hValue.setText(currencyFormat(ticker.avg24h));
        askValue.setText(currencyFormat(ticker.ask));
        bidValue.setText(currencyFormat(ticker.bid));
        lastValue.setText(currencyFormat(ticker.last));
        volumeBTCValue.setText(currencyFormat(ticker.volume_btc));
        timestamp.setText(ticker.timestamp.split(" -")[0]);

        if(ticker.ask.equals(BigDecimal.ZERO)){
            Toast.makeText(MainActivity.this, "There was a problem connecting to Bitcoin Average. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }

    public static String currencyFormat(BigDecimal n) {
        return n.setScale(2, BigDecimal.ROUND_DOWN).toString();
    }
}
