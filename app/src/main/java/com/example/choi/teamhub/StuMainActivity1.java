package com.example.choi.teamhub;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.choi.teamhub.DTO.ChatDto;
import com.example.choi.teamhub.DTO.ProjectDto;
import com.example.choi.teamhub.Fragment.ChatFragment;
import com.example.choi.teamhub.Fragment.NoticeFrament;
import com.example.choi.teamhub.Fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class StuMainActivity1 extends AppCompatActivity {

    private TextView mTextMessage;

    private ListView chatListView;
    private ChatListViewAdapter adapter;
    private List<ChatDto> chatList;

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 3개의 메뉴에 들어갈 Fragment들
    private ChatFragment chatFragment = new ChatFragment();
    private NoticeFrament noticeFrament = new NoticeFrament();
    private SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main1);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, noticeFrament).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_notice:
                        transaction.replace(R.id.frame_layout, noticeFrament).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_chat:
                        transaction.replace(R.id.frame_layout, chatFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_setting:
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
            View v = View.inflate(context, R.layout.chat_row, null);

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





