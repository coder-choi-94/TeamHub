package com.example.choi.teamhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.choi.teamhub.R;

public class SettingItem extends LinearLayout {
    TextView textView;
    public SettingItem(Context context){
        super(context);
        init(context);
    }
    private void init(Context context){
        LayoutInflater myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myInflater.inflate(R.layout.setting_item, this, true);

        textView = (TextView) findViewById(R.id.setting_item);
    }
    public void setItem(String item){textView.setText(item);}
}
