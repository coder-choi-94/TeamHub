package com.example.choi.teamhub.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.teamhub.DTO.ChatDto;
import com.example.choi.teamhub.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ListView chatListView;
    private ChatListViewAdapter adapter;
    private List<ChatDto> chatList;

    private EditText message;
    private Button sendButton;

    private String userId;
    private String userName;
    private String userPhone;
    private String userDept;
    private String userSno;
    private int projectNum;
    private int teamNum;
    private int countInTeam;

    private ViewGroup groupLayout;

    ProgressDialog progress;


    ChildEventListener myRefEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ChatDto chatDto = dataSnapshot.getValue(ChatDto.class);
            Log.v("#@!#@!", "디비에서 가져온 직후 ChatDto.getUserId => " + chatDto.getUserId());
            chatList.add(chatDto);
            Log.v("##@@ add!!", chatList.size()+"");
            adapter.notifyDataSetChanged();
            chatListView.setSelection(chatList.size()-1);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("##@@ OnCreate()", "");
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_chat_fragment, container, false);

        sendButton = (Button)view.findViewById(R.id.sendButton);
        message = (EditText)view.findViewById(R.id.message);

        chatListView = view.findViewById(R.id.chatList);
        chatList = new ArrayList<ChatDto>();

        adapter = new ChatListViewAdapter(getActivity().getApplicationContext(), chatList);
        chatListView.setAdapter(adapter);

        userId = getArguments().getString("userId");
        userName = getArguments().getString("userName");
        userPhone = getArguments().getString("userPhone");
        userDept = getArguments().getString("userDept");
        userSno = getArguments().getString("userSno");
        projectNum = getArguments().getInt("프로젝트 번호");
        teamNum = getArguments().getInt("팀 번호");
        countInTeam = Integer.parseInt(getArguments().getString("countInTeam"));
        ((TextView)view.findViewById(R.id.countInTeam)).setText(countInTeam + "");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(projectNum+"/"+teamNum);  //프로젝트번호/팀번호 레퍼런스를 가져옴(식별자용도) -> 내 팀의 채팅db만 가져옴
        myRef.addChildEventListener(myRefEvent);

//        groupLayout = (ViewGroup)view.findViewById(R.id.groupLayout);
//                groupLayout.setOnClickListener(new ViewGroup.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                        alert.setMessage("최종명, 조현비, 장우성");
//                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        alert.show();
//                    }
//                });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("##@@ buttonCLicked", "@");
                if(message.getText().toString() == null || message.getText().toString().equals("")) {
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String time = (new SimpleDateFormat("HH:mm")).format(date);
                String hour, minute;
                String parsedTime;

                if(Integer.parseInt(time.toString().split(":")[0]) > 12) {
                    hour = Integer.parseInt(time.toString().split(":")[0]) - 12 + "";
                    minute = time.toString().split(":")[1];
                    parsedTime = "오후 " + hour + ":" + minute;
                } else {
                    hour = Integer.parseInt(time.toString().split(":")[0]) + "";
                    minute = time.toString().split(":")[1];
                    parsedTime = "오전 " + hour + ":" + minute;
                }
                ChatDto chatDto = new ChatDto();
                chatDto.setUserId(userId);
                chatDto.setWriter(userName);
                chatDto.setMessage(message.getText().toString());
                chatDto.setTime(parsedTime);
                Log.v("#@!#@!", "보내기 전 ChatDto.getUserId => " + chatDto.getUserId());
                myRef.push().setValue(chatDto);
                message.setText("");
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("##@@ OnPause()", "");
        myRef.removeEventListener(myRefEvent);
    }

    public class ChatListViewAdapter extends BaseAdapter {

        private Context my_context;
        private List<ChatDto> chatList;

        public ChatListViewAdapter(Context context, List<ChatDto> chatList) {
            this.my_context = context;
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


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;
            if (chatList.get(i).getUserId().equals(userId)) {
                v = View.inflate(getActivity().getApplicationContext(), R.layout.chat_row_from_me, null);
            } else {
                v = View.inflate(getActivity().getApplicationContext(), R.layout.chat_row_from_other, null);
            }
            TextView writer = (TextView) v.findViewById(R.id.writer);
            TextView message = (TextView) v.findViewById(R.id.message);
            TextView time = (TextView) v.findViewById(R.id.time);

            writer.setText(chatList.get(i).getWriter());
            message.setText(chatList.get(i).getMessage());
            time.setText(chatList.get(i).getTime());

            // 가장 아래로 스크롤
            chatListView.setSelection(this.getCount() - 1);
            //만든뷰를 반환함
            return v;
        }
        private class ListViewHolder {
            TextView writer;
            TextView message;
            TextView time;

        }

    }
}
