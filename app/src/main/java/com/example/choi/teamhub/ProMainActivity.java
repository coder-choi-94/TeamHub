package com.example.choi.teamhub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.teamhub.DTO.ChatDto;
import com.example.choi.teamhub.Fragment.ChatFragment;
import com.example.choi.teamhub.Fragment.NoticeFrament;
import com.example.choi.teamhub.Fragment.ParticipationFragment;
import com.example.choi.teamhub.Fragment.SettingFragment;
import com.example.choi.teamhub.Fragment.TodoFragment;

import java.util.List;

public class ProMainActivity extends AppCompatActivity {
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 3개의 메뉴에 들어갈 Fragment들
    //private ChatFragment chatFragment = new ChatFragment();
    private ParticipationFragment participationFragment = new ParticipationFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private TodoFragment todoFragment = new TodoFragment();
    private String PCODE;
    private String PNAME;
    private String P_NAME;
    private String teamName;
    private int P_NUM;
    private int teamNum;
    private String userName;
    private String userSno;
    private String userId;
    private String userPhone;
    private String userDept;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_main);

        Intent intent = getIntent();
        PCODE = intent.getStringExtra("code");
        PNAME = intent.getStringExtra("name");
        P_NAME = intent.getStringExtra("프로젝트 이름");
        teamName = intent.getStringExtra("팀 이름");
        P_NUM = intent.getIntExtra("프로젝트 번호", 0);
        teamNum = intent.getIntExtra("팀 번호", 0);
        userId = intent.getStringExtra("id");
        userPhone = intent.getStringExtra("phone");
        userDept = intent.getStringExtra("dept");
        Log.e("proMain", String.valueOf(P_NAME));
        Log.e("proMain", String.valueOf(P_NUM));
        Log.e("proMain", String.valueOf(PCODE));
        Log.e("proMain", String.valueOf(PNAME));
        Log.e("proMain", String.valueOf(userId));
        Log.e("proMain", String.valueOf(userPhone));
        Log.e("proMain", String.valueOf(userDept));

        String s_num = String.valueOf(P_NUM);
        String message = "교수 코드 : " + PCODE + " / 교수 이름 : " + PNAME + " / 프로젝트 이름 : " + P_NAME + " / 프로젝트 번호 : " + P_NUM;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        Bundle bundle = new Bundle(7);
        bundle.putInt("구분", 2);
        bundle.putInt("프로젝트 번호", P_NUM);
        bundle.putInt("팀 번호", teamNum);
        bundle.putString("code", PCODE);
        bundle.putString("name", PNAME);
        bundle.putString("id", userId);
        bundle.putString("phone", userPhone);
        bundle.putString("dept", userDept);

        participationFragment.setArguments(bundle);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, participationFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Bundle bundle;
                switch (item.getItemId()) {
                    case R.id.navigation_participation:

                        bundle = new Bundle(7);
                        bundle.putInt("구분", 2);
                        bundle.putInt("프로젝트 번호", P_NUM);
                        bundle.putInt("팀 번호", teamNum);
                        bundle.putString("code", PCODE);
                        bundle.putString("name", PNAME);
                        bundle.putString("id", userId);
                        bundle.putString("phone", userPhone);
                        bundle.putString("dept", userDept);

                        participationFragment.setArguments(bundle);
                        transaction.replace(R.id.frame_layout, participationFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_todo:
                        bundle = new Bundle(7);
                        bundle.putInt("구분", 2);
                        bundle.putInt("프로젝트 번호", P_NUM);
                        bundle.putInt("팀 번호", teamNum);
                        bundle.putString("code", PCODE);
                        bundle.putString("name", PNAME);
                        bundle.putString("id", userId);
                        bundle.putString("phone", userPhone);
                        bundle.putString("dept", userDept);
                        //
                        todoFragment.setArguments(bundle);
                        transaction.replace(R.id.frame_layout, todoFragment).commitAllowingStateLoss();
                        break;
//                    case R.id.navigation_chat:
//                        bundle = new Bundle(7);
//                        bundle.putInt("프로젝트 번호", projectNum);
//                        bundle.putInt("팀 번호", teamNum);
//                        bundle.putString("userId", userId);
//                        bundle.putString("userName", userName);
//                        bundle.putString("userPhone", userPhone);
//                        bundle.putString("userDept", userDept);
//                        bundle.putString("userSno", userSno);
//
//                        chatFragment.setArguments(bundle);
//                        transaction.replace(R.id.frame_layout, chatFragment).commitAllowingStateLoss();
//                        break;
                    case R.id.navigation_setting:
                        bundle = new Bundle(7);
                        bundle.putInt("구분", 2);
                        bundle.putInt("프로젝트 번호", P_NUM);
                        bundle.putInt("팀 번호", teamNum);
                        bundle.putString("code", PCODE);
                        bundle.putString("name", PNAME);
                        bundle.putString("프로젝트 이름", P_NAME);
                        bundle.putString("팀 이름", teamName);
                        bundle.putString("id", userId);
                        bundle.putString("phone", userPhone);
                        bundle.putString("dept", userDept);
                        Log.e("proMain", String.valueOf(P_NAME));
                        Log.e("proMain", String.valueOf(P_NUM));
                        Log.e("proMain", String.valueOf(PCODE));
                        Log.e("proMain", String.valueOf(PNAME));
                        Log.e("proMain", String.valueOf(userId));
                        Log.e("proMain", String.valueOf(userPhone));
                        Log.e("proMain", String.valueOf(userDept));
//                        bundle.putString("userId", userId);
//                        bundle.putString("userName", userName);
//                        bundle.putString("userPhone", userPhone);
//                        bundle.putString("userDept", userDept);
//                        bundle.putString("userSno", userSno);

                        settingFragment.setArguments(bundle);
                        transaction.replace(R.id.frame_layout, settingFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
    }
    public class ChatListViewAdapter extends BaseAdapter {

        private Context context;
        private List<ChatDto> chatList;

        public ChatListViewAdapter(Context context, List<ChatDto> chatList) {
            this.context = context;
            this.chatList = chatList;
        }

        //출력할 총갯수를 설정하는 메소드
        @Override
        public int getCount() {
            return chatList.size();
        }

        //특정한 유저를 반환하는 메소드
        @Override
        public Object getItem(int i) {
            return chatList.get(i);
        }

        //아이템별 아이디를 반환하는 메소드
        @Override
        public long getItemId(int i) {
            return i;
        }

        //가장 중요한 부분
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(context, R.layout.chat_row_from_other, null);

            //뷰에 다음 컴포넌트들을 연결시켜줌
            TextView writer = (TextView) v.findViewById(R.id.writer);
            TextView message = (TextView) v.findViewById(R.id.message);

            writer.setText(chatList.get(i).getWriter());
            message.setText(chatList.get(i).getMessage());

            //만든뷰를 반환함
            return v;
        }
    }
}
