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
        int bool = intent.getIntExtra("구분", 0);
        if (bool == 1){ // 학생일때 정보
            int projectNum = intent.getIntExtra("프로젝트 번호", -1);
            int teamNum = intent.getIntExtra("팀 번호", -1);
            String userId = intent.getStringExtra("userId");
            String userName = intent.getStringExtra("userName");
            String userPhone = intent.getStringExtra("userPhone");
            String userDept = intent.getStringExtra("userDept");
            String userSno = intent.getStringExtra("userSno");
//            String userId = getArguments().getString("userId");
//            userName = getArguments().getString("userName");
//            userPhone = getArguments().getString("userPhone");
//            userDept = getArguments().getString("userDept");
//            userSno = getArguments().getString("userSno");
//            projectNum = getArguments().getInt("프로젝트 번호");
//            teamNum = getArguments().getInt("팀 번호");
        } else if (bool == 2){ // 교수일때 정보
            int projectNum = intent.getIntExtra("프로젝트 번호", -1);
            int teamNum = intent.getIntExtra("팀 번호", -1);
            String userId = intent.getStringExtra("userId");
            String userName = intent.getStringExtra("userName");
            String userPhone = intent.getStringExtra("userPhone");
            String userDept = intent.getStringExtra("userDept");
            String userSno = intent.getStringExtra("userSno");
//            projectNum = getArguments().getInt("프로젝트 번호");
//            teamNum = getArguments().getInt("팀 번호");
//            professorNum = getArguments().getInt("교수 코드");
//            userName = getArguments().getString("교수 이름");
//            projectName = getArguments().getString("프로젝트 이름");
//            teamName = getArguments().getString("팀 이름");
        }



        t1 = (TextView) findViewById(R.id.textView2);
        t2 = (TextView) findViewById(R.id.textView3);
        t3 = (TextView) findViewById(R.id.textView4);
        t4 = (TextView) findViewById(R.id.textView5);
        t5 = (TextView) findViewById(R.id.textView6);



        t1.setText(userId);
        t2.setText(userName);
        t5.setText(userPhone);
        t3.setText(userDept);
        t4.setText(userSno);
    }
}
