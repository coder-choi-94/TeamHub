package com.example.choi.teamhub.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.choi.teamhub.DTO.ChatDto;
import com.example.choi.teamhub.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_chat_fragment, container, false);

        sendButton = (Button)view.findViewById(R.id.sendButton);
        message = (EditText)view.findViewById(R.id.message);

        chatListView = view.findViewById(R.id.chatList);
        chatList = new ArrayList<ChatDto>();

        adapter = new ChatListViewAdapter(getActivity(), chatList);
        chatListView.setAdapter(adapter);

        userId = getArguments().getString("userId");
        userName = getArguments().getString("userName");
        userPhone = getArguments().getString("userPhone");
        userDept = getArguments().getString("userDept");
        userSno = getArguments().getString("userSno");
        projectNum = getArguments().getInt("프로젝트 번호");
        teamNum = getArguments().getInt("팀 번호");



        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(projectNum+"/"+teamNum);  //프로젝트번호/팀번호 레퍼런스를 가져옴(식별자용도) -> 내 팀의 채팅db만 가져옴





        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatDto chatDto = dataSnapshot.getValue(ChatDto.class);

                chatList.add(chatDto);

                adapter.notifyDataSetChanged();

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
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.getText().toString() == null ||message.getText().toString().equals("")) {
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
//                String today = (new SimpleDateFormat("yyyyMMddHHmmss")).format(date);
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
                myRef.push().setValue(chatDto);
                message.setText("");
            }
        });
        return view;
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


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View v;
            if(chatList.get(i).getUserId().equals(userId)) {
                v = View.inflate(context, R.layout.chat_row_from_me, null);
            } else {
                v = View.inflate(context, R.layout.chat_row_from_other, null);
            }

            //뷰에 다음 컴포넌트들을 연결시켜줌
            TextView writer = (TextView) v.findViewById(R.id.writer);
            TextView message = (TextView) v.findViewById(R.id.message);
            TextView time = (TextView) v.findViewById(R.id.time);



            writer.setText(chatList.get(i).getWriter());
            message.setText(chatList.get(i).getMessage());
            time.setText(chatList.get(i).getTime());

//            if(chatList.get(i).getUserId().equals(userId)) {
////                //내가 보낸 메세지라면
////                //이름부분을 그래비티 오른쪽으로
////                v.findViewById(R.id.writer).setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
////
////                //메세지부분을 그래비티 오른쪽으로
////
////                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
////                DisplayMetrics dm = getResources().getDisplayMetrics();
////                params.rightMargin = Math.round(70 * dm.density);
////                params.leftMargin = Math.round(70 * dm.density);
////                params.gravity = Gravity.END;
////                v.findViewById(R.id.message).setLayoutParams(params);
//            }

            // 가장 아래로 스크롤
            chatListView.setSelection(this.getCount()-1);
            //만든뷰를 반환함
            return v;
        }
    }
}
