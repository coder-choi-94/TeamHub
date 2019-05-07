package com.example.choi.teamhub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class StuMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main);

        /*가져오는 정보
        intent.putExtra("아이디", ID);
        intent.putExtra("교수 코드", PCODE);
        intent.putExtra("프로젝트이름", PNAME);
        intent.putExtra("프로젝트 번호", P_NUM);
        intent.putExtra("팀 이름", teamList.get(p).getName());
        intent.putExtra("팀 번호", teamList.get(p).getNum());
         */
    }
}
