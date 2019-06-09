package com.example.choi.teamhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class PersonalData extends AppCompatActivity {
    TextView t1, t2, t3,t4, t5, t6, t7, t8, t9, t10;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data);

        Intent intent = getIntent();
        int projectNum = intent.getIntExtra("프로젝트 번호", -1);
        int teamNum = intent.getIntExtra("팀 번호", -1);
        String userId = intent.getStringExtra("userId");
        String userName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userPhone");
        String userDept = intent.getStringExtra("userDept");
        String userSno = intent.getStringExtra("userSno");

        t1 = (TextView) findViewById(R.id.textView2);
        t2 = (TextView) findViewById(R.id.textView3);
        t3 = (TextView) findViewById(R.id.textView4);
        t4 = (TextView) findViewById(R.id.textView5);
        t5 = (TextView) findViewById(R.id.textView6);
        t6 = (TextView) findViewById(R.id.textView7);
        t7 = (TextView) findViewById(R.id.textView8);
        t8 = (TextView) findViewById(R.id.textView9);
        t9 = (TextView) findViewById(R.id.textView10);
        t10 = (TextView) findViewById(R.id.textView11);

        t1.setText("아이디");
        t2.setText(userId);
        t3.setText("이름");
        t4.setText(userName);
        t5.setText("전화번호");
        t6.setText(userPhone);
        t7.setText("학과");
        t8.setText(userDept);
        t9.setText("학번");
        t10.setText(userSno);
    }
}
