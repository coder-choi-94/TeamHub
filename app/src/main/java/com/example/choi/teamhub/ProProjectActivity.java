package com.example.choi.teamhub;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.teamhub.DTO.TeamDto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProProjectActivity extends AppCompatActivity {
    private Animation fab_open, fab_close;
    private FloatingActionButton fab, fab1, fab2;
    private Boolean isFabOpen =false;
    private View formLayout;
    private String PNAME;
    private int PCODE;
    private int P_NUM;
    private String P_NAME;
    private String s_num;

    private ListView listView;
    private TeamListAdapter adapter;
    private List<TeamDto> teamList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_in_project);

        Intent intent = getIntent();
        PCODE = intent.getIntExtra("교수 코드", 0);
        PNAME = intent.getStringExtra("교수 이름");
        P_NAME = intent.getStringExtra("프로젝트 이름");
        P_NUM = intent.getIntExtra("프로젝트 번호", 0);
        s_num = String.valueOf(P_NUM);
        String message = "교수 코드 : " + PCODE + " / 교수 이름 : " + PNAME + " / 프로젝트 이름 : " + P_NAME + " / 프로젝트 번호 : " + P_NUM;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        Context context = this;
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        formLayout = inflater.inflate(R.layout.make_team, null, false);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);

        /*
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        */

        listView = findViewById(R.id.teamListView);
        teamList = new ArrayList<TeamDto>();
        adapter = new TeamListAdapter(getApplicationContext(), teamList);
        listView.setAdapter(adapter);

        getTeams();
    }
    public void getTeams() {
        teamList.clear();
        try {
            String result = new ProProjectActivity.getTeamsTask().execute(s_num).get();
            JSONObject resultJsonObj = new JSONObject(result);
            JSONArray resultJsonData = resultJsonObj.getJSONArray("teams");
            JSONObject jsonObj;
            for(int i=0 ; i<resultJsonData.length() ; i++) {
                jsonObj = resultJsonData.getJSONObject(i);
                TeamDto teamDto = new TeamDto();
                teamDto.setNum(jsonObj.getInt("num"));
                teamDto.setMakeProject_num(jsonObj.getInt("makeProject_num"));
                teamDto.setStudent_id(jsonObj.getString("student_id"));
                teamDto.setName(jsonObj.getString("name"));
                teamDto.setPw(jsonObj.getString("pw"));
                teamDto.setStudent_name(jsonObj.getString("student_name"));
                Log.e("getTeams", "Team1 : " + teamDto.getNum() + ", " + teamDto.getStudent_name());
                teamList.add(teamDto);    //리스트뷰에 한개씩 추가
            }

        }catch (Exception e) {

        }
    }
    class getTeamsTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/professor_get_teams.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "num=" + strings[0];
                Log.e("getTeamTaskSend", sendMsg);
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
                    Log.e("getTeamTask", receiveMsg);

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

    public class TeamListAdapter extends BaseAdapter {

        private Context context;
        private List<TeamDto> teamList;

        public TeamListAdapter(Context context, List<TeamDto> teamList){
            this.context = context;
            this.teamList = teamList;
        }

        //출력할 총갯수를 설정하는 메소드
        @Override
        public int getCount() {
            return teamList.size();
        }

        //특정한 유저를 반환하는 메소드
        @Override
        public Object getItem(int i) {
            return teamList.get(i);
        }

        //아이템별 아이디를 반환하는 메소드
        @Override
        public long getItemId(int i) {
            return i;
        }

        //가장 중요한 부분
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(context, R.layout.team_row, null);

            //뷰에 다음 컴포넌트들을 연결시켜줌
            TextView stuName= (TextView)v.findViewById(R.id.stuName);
            TextView teamName = (TextView)v.findViewById(R.id.teamName);

            stuName.setText(teamList.get(i).getStudent_name());
            teamName.setText(teamList.get(i).getName());

            //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
            v.setTag(teamList.get(i).getName());

            //만든뷰를 반환함
            return v;
        }
    }
}
