package com.example.choi.teamhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.choi.teamhub.DTO.ProjectDto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PersonalData extends AppCompatActivity {
    TextView t1, t2, t3,t4, t5, t6, t7, t8, t9, t10;
    private int projectNum;
    private int teamNum;
    private String userId;
    private String userName;
    private String userPhone;
    private String userDept;
    private String userSno;
    private String processNum;
    private String projectName;
    private String teamName;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data);

        Intent intent = getIntent();
        int bool = intent.getIntExtra("구분", 0);
        Log.e("bool", String.valueOf(bool));
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
            processNum = intent.getStringExtra("code");
            userName = intent.getStringExtra("name");
            projectName = intent.getStringExtra("프로젝트 이름");
            teamName = intent.getStringExtra("팀 이름");
            userId = intent.getStringExtra("id");
            userPhone = intent.getStringExtra("phone");
            userDept = intent.getStringExtra("dept");

            Log.e("data", String.valueOf(projectNum));
            Log.e("data", ""+processNum);
            Log.e("data", String.valueOf(teamNum));
            Log.e("data", String.valueOf(projectName));
            Log.e("data", String.valueOf(userId));
            Log.e("data", String.valueOf(userPhone));
            Log.e("data", String.valueOf(userDept));
        }


        t1 = (TextView) findViewById(R.id.textView2);
        t2 = (TextView) findViewById(R.id.textView3);
        t3 = (TextView) findViewById(R.id.textView4);
        t4 = (TextView) findViewById(R.id.textView5);
        t5 = (TextView) findViewById(R.id.textView6);

        if (bool == 1){ // 학생일때 정보
            t1.setText(userId);
            t2.setText(userName);
            t5.setText(userPhone);
            t3.setText(userDept);
            t4.setText(userSno);
        } else if (bool == 2){ // 교수일때 정보
            t1.setText(userId);
            t2.setText(processNum);
            t5.setText(userPhone);
            t3.setText(userName);
            t4.setText(userDept);

        }



    }




}
