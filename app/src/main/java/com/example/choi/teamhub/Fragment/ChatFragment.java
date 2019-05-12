package com.example.choi.teamhub.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.teamhub.DTO.ChatDto;
import com.example.choi.teamhub.R;
import com.example.choi.teamhub.StuIndexActivity;
import com.example.choi.teamhub.StuProjectActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private ListView chatListView;
    private ChatListViewAdapter adapter;
    private List<ChatDto> chatList;

    private EditText message;
    private Button sendButton;

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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatDto chatDto = new ChatDto();
                chatDto.setName("최종명");
                chatDto.setMessage(message.getText().toString());

                chatList.add(chatDto);
                adapter.notifyDataSetChanged();
//                try {
//                    final String result = new sendChatTask().execute().get();
//                    if (!result.contains("exists")) {// 존재하지않는 방이라면
//                        d = builder
//                                .setMessage("해당 프로젝트 방을 찾을 수 없습니다.")
//                                .setPositiveButton("학인", null)
//                                .create();
//                        d.show();
//                        return;
//                    } else {
//                        d = builder
//                                .setMessage(name + " 프로젝트에 참여하시겠습니까?")
//                                .setPositiveButton("학인", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        String[] resultArr = result.split(":");
//                                        String projectNum = resultArr[1];    //프로젝트 기본키 얻어옴
//
//                                        try {
//                                            String rst = new StuIndexActivity.joinProjectTask().execute(projectNum, ID).get();
//                                            if(rst.equals("success")) {
//                                                getProjects();  //참여중인 프로젝트 리스트뷰를 동기화 한번 해주고 액티비티 이동하기
//                                                Intent intent = new Intent(StuIndexActivity.this, StuProjectActivity.class);
//                                                intent.putExtra("아이디", ID);
//                                                intent.putExtra("교수 코드", Integer.parseInt(code));
//                                                intent.putExtra("프로젝트 이름", name);
//                                                intent.putExtra("비밀번호", pwd);
//                                                startActivity(intent);
//                                            } else {
//                                                Toast.makeText(StuIndexActivity.this, "오류 발생.. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                                            }
//
//                                        }catch(Exception e) {
//
//                                        }
//                                    }
//                                })
//                                .setNegativeButton("취소",null)
//                                .create();
//                        d.show();
//
//                    }
//
//                } catch(Exception e) {
//                    e.printStackTrace();
//                }
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

    class sendChatTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/student_send_chat.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "studentId=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }
}
