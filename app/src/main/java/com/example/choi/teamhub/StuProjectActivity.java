package com.example.choi.teamhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class StuProjectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_in_project);
        Intent intent = getIntent();
        String id = intent.getStringExtra("아이디");
        int pcode = intent.getIntExtra("교수 코드", 0);
        String pname = intent.getStringExtra("프로젝트 이름");
        String pw = intent.getStringExtra("비밀번호");

        String message = "학생 아이디 : " + id + " / 이 프로젝트의 교수 코드 : " + pcode + " / 프로젝트 이름 : " + pname + " / 이 프로젝트 비밀번호 : " + pw;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
