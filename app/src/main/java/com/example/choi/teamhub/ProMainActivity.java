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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.teamhub.DTO.ChatDto;
import com.example.choi.teamhub.Fragment.ChatFragment;
import com.example.choi.teamhub.Fragment.NoticeFrament;
import com.example.choi.teamhub.Fragment.SettingFragment;
import com.example.choi.teamhub.Fragment.TodoFragment;

import java.util.List;

public class ProMainActivity extends AppCompatActivity {
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 3개의 메뉴에 들어갈 Fragment들
    private ChatFragment chatFragment = new ChatFragment();
    private NoticeFrament noticeFrament = new NoticeFrament();
    private SettingFragment settingFragment = new SettingFragment();
    private TodoFragment todoFragment = new TodoFragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_main);

        Intent intent = getIntent();
        final int PCODE = intent.getIntExtra("교수 코드", 0);
        final String PNAME = intent.getStringExtra("교수 이름");
        final String P_NAME = intent.getStringExtra("프로젝트 이름");
        final String teamName = intent.getStringExtra("팀 이름");
        final int P_NUM = intent.getIntExtra("프로젝트 번호", 0);
        final int teamNum = intent.getIntExtra("팀 번호", 0);



        String s_num = String.valueOf(P_NUM);
        String message = "교수 코드 : " + PCODE + " / 교수 이름 : " + PNAME + " / 프로젝트 이름 : " + P_NAME + " / 프로젝트 번호 : " + P_NUM;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, noticeFrament).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Bundle bundle;
                switch (item.getItemId()) {
//                    case R.id.navigation_notice:
//                        bundle = new Bundle(7);
//                        bundle.putInt("프로젝트 번호", projectNum);
//                        bundle.putInt("팀 번호", teamNum);
//                        bundle.putString("userId", userId);
//                        bundle.putString("userName", userName);
//                        bundle.putString("userPhone", userPhone);
//                        bundle.putString("userDept", userDept);
//                        bundle.putString("userSno", userSno);
//
//                        noticeFrament.setArguments(bundle);
//                        transaction.replace(R.id.frame_layout, noticeFrament).commitAllowingStateLoss();
//                        break;
                    case R.id.navigation_todo:
                        bundle = new Bundle(7);
                        bundle.putInt("프로젝트 번호", P_NUM);
                        bundle.putInt("팀 번호", teamNum);
//                        bundle.putString("userId", userId);
//                        bundle.putString("userName", userName);
//                        bundle.putString("userPhone", userPhone);
//                        bundle.putString("userDept", userDept);
//                        bundle.putString("userSno", userSno);

                        noticeFrament.setArguments(bundle);
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
                        bundle.putInt("교수 코드", PCODE);
                        bundle.putString("교수 이름", PNAME);
                        bundle.putString("프로젝트 이름", P_NAME);
                        bundle.putString("팀 이름", teamName);
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
