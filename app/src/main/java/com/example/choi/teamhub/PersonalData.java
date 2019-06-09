package com.example.choi.teamhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class PersonalData extends AppCompatActivity {
    TextView t1, t2, t3,t4, t5, t6, t7, t8, t9, t10;
    int projectNum;
    int teamNum;
    String userId;
    String userName;
    String userPhone;
    String userDept;
    String userSno;
    String processNum;
    String projectName;
    String teamName;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data);

        Intent intent = getIntent();
        int bool = intent.getIntExtra("구분", 0);
        if (bool == 1){ // 학생일때 정보
            projectNum = intent.getIntExtra("프로젝트 번호", -1);
            teamNum = intent.getIntExtra("팀 번호", -1);
            userId = intent.getStringExtra("userId");
            userName = intent.getStringExtra("userName");
            userPhone = intent.getStringExtra("userPhone");
            userDept = intent.getStringExtra("userDept");
            userSno = intent.getStringExtra("userSno");
        } else if (bool == 2){ // 교수일때 정보
            projectNum = intent.getIntExtra("프로젝트 번호", -1);
            teamNum = intent.getIntExtra("팀 번호", -1);
            processNum = intent.getStringExtra("교수 코드");
            userName = intent.getStringExtra("교수 이름");
            projectName = intent.getStringExtra("프로젝트 이름");
            teamName = intent.getStringExtra("팀 이름");
            userSno = intent.getStringExtra("userSno");

        }



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

        //dddddddddddd
        if (bool == 1){ // 학생일때 정보
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
        } else if (bool == 2){ // 교수일때 정보
            projectNum = intent.getIntExtra("프로젝트 번호", -1);
            teamNum = intent.getIntExtra("팀 번호", -1);
            processNum = intent.getStringExtra("교수 코드");
            userName = intent.getStringExtra("교수 이름");
            projectName = intent.getStringExtra("프로젝트 이름");
            teamName = intent.getStringExtra("팀 이름");
            userSno = intent.getStringExtra("userSno");

        }
        t1.setText(userId);
        t2.setText(userName);
        t5.setText(userPhone);
        t3.setText(userDept);
        t4.setText(userSno);
    }
}
