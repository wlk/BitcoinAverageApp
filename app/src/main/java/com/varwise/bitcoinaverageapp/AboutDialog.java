package com.varwise.bitcoinaverageapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutDialog extends Dialog {
    public AboutDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.about);
        TextView tv = (TextView) findViewById(R.id.info_text);
        tv.setText(Html.fromHtml("<h3>Bitcoin Average App</h3>APP is using<br> https://bitcoinaverage.com/ API<br>Version 1.0 by Varwise<br>Copyright 2015<br><b>http://www.varwise.com</b><br><br>"));
        tv.setLinkTextColor(Color.BLUE);
        Linkify.addLinks(tv, Linkify.ALL);
    }
}
