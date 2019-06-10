package com.example.choi.teamhub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.choi.teamhub.DTO.ChatDto;
import com.example.choi.teamhub.Fragment.ChatFragment;
import com.example.choi.teamhub.Fragment.NoticeFrament;
import com.example.choi.teamhub.Fragment.SettingFragment;
import com.example.choi.teamhub.Fragment.TodoFragment;

import java.util.List;

public class StuMainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private ListView chatListView;
    private ChatListViewAdapter adapter;
    private List<ChatDto> chatList;

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 3개의 메뉴에 들어갈 Fragment들
    private TodoFragment todoFragment = new TodoFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private SettingFragment settingFragment = new SettingFragment();


    private int projectNum;
    private int teamNum;
    private String userId;
    private String userName;
    private String userPhone;
    private String userDept;
    private String userSno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main);

        Intent intent = getIntent();
        projectNum = intent.getIntExtra("프로젝트 번호", -1);
        teamNum = intent.getIntExtra("팀 번호", -1);
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userPhone = intent.getStringExtra("userPhone");
        userDept = intent.getStringExtra("userDept");
        userSno = intent.getStringExtra("userSno");


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        // 첫 화면 지정
        Bundle bundle = new Bundle(7);
        bundle.putInt("구분", 1);
        bundle.putInt("프로젝트 번호", projectNum);
        bundle.putInt("팀 번호", teamNum);
        bundle.putString("userId", userId);
        bundle.putString("userName", userName);
        bundle.putString("userPhone", userPhone);
        bundle.putString("userDept", userDept);
        bundle.putString("userSno", userSno);

        todoFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, todoFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Bundle bundle;
                switch (item.getItemId()) {
                    case R.id.navigation_todo:
                        bundle = new Bundle(7);
                        bundle.putInt("구분", 1);
                        bundle.putInt("프로젝트 번호", projectNum);
                        bundle.putInt("팀 번호", teamNum);
                        bundle.putString("userId", userId);
                        bundle.putString("userName", userName);
                        bundle.putString("userPhone", userPhone);
                        bundle.putString("userDept", userDept);
                        bundle.putString("userSno", userSno);

                        todoFragment.setArguments(bundle);
                        transaction.replace(R.id.frame_layout, todoFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_chat:
                        bundle = new Bundle(7);
                        bundle.putInt("프로젝트 번호", projectNum);
                        bundle.putInt("팀 번호", teamNum);
                        bundle.putString("userId", userId);
                        bundle.putString("userName", userName);
                        bundle.putString("userPhone", userPhone);
                        bundle.putString("userDept", userDept);
                        bundle.putString("userSno", userSno);

                        chatFragment.setArguments(bundle);
                        transaction.replace(R.id.frame_layout, chatFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_setting:
                        bundle = new Bundle(7);
                        bundle.putInt("구분", 1);
                        bundle.putInt("프로젝트 번호", projectNum);
                        bundle.putInt("팀 번호", teamNum);
                        bundle.putString("userId", userId);
                        bundle.putString("userName", userName);
                        bundle.putString("userPhone", userPhone);
                        bundle.putString("userDept", userDept);
                        bundle.putString("userSno", userSno);

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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.v("#@$#@$", "onActivityResult in activity");
//        super.onActivityResult(requestCode, resultCode, data);
//        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//            Log.v("#@$#@$", "fragment : " + fragment);
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}





