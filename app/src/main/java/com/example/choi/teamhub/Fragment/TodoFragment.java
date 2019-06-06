package com.example.choi.teamhub.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.choi.teamhub.DTO.TodoDto;
import com.example.choi.teamhub.R;

import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment {

    private ListView chatListView;
    // private TodoFragment.TodoListViewAdapter adapter;
    private List<TodoDto> todoList;

    private String userId;
    private String userName;
    private String userPhone;
    private String userDept;
    private String userSno;
    private int projectNum;
    private int teamNum;

    String student_id;
    String title;
    String content;
    String filePath;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_todo_fragment, container, false);
/*
        chatListView = view.findViewById(R.id.chatList);
        todoList = new ArrayList<TodoDto>();

        adapter = new TodoFragment.TodoListViewAdapter(getActivity(), todoList);
        chatListView.setAdapter(adapter);

        userId = getArguments().getString("userId");
        userName = getArguments().getString("userName");
        userPhone = getArguments().getString("userPhone");
        userDept = getArguments().getString("userDept");
        userSno = getArguments().getString("userSno");
        projectNum = getArguments().getInt("프로젝트 번호");
        teamNum = getArguments().getInt("팀 번호");



*/
        return inflater.inflate(R.layout.activity_todo_fragment, container, false);
    }


    public class TodoListViewAdapter extends BaseAdapter {

        private Context context;
        private List<TodoDto> todoList;

        public TodoListViewAdapter(Context context, List<TodoDto> chatList) {
            this.context = context;
            this.todoList = chatList;
        }


        //출력할 총갯수를 설정하는 메소드
        @Override
        public int getCount() {
            return todoList.size();
        }

        //특정한 유저를 반환하는 메소드
        @Override
        public Object getItem(int i) {
            return todoList.get(i);
        }

        //아이템별 아이디를 반환하는 메소드
        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View v = null;
            /*
            if(todoList.get(i).getUserId().equals(userId)) {
                v = View.inflate(context, R.layout.chat_row_from_me, null);
            } else {
                v = View.inflate(context, R.layout.chat_row_from_other, null);
            }

            //뷰에 다음 컴포넌트들을 연결시켜줌
            TextView writer = (TextView) v.findViewById(R.id.writer);
            TextView message = (TextView) v.findViewById(R.id.message);
            TextView time = (TextView) v.findViewById(R.id.time);



            writer.setText(todoList.get(i).getWriter());
            message.setText(todoList.get(i).getMessage());
            time.setText(todoList.get(i).getTime());

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
            */
            return v;
        }
    }

}