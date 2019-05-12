package com.example.choi.teamhub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import java.util.concurrent.ExecutionException;

public class StuProjectActivity extends AppCompatActivity implements View.OnClickListener{
    private Animation fab_open, fab_close;
    private FloatingActionButton fab, fab1, fab2;
    private Boolean isFabOpen =false;
    private View formLayout;
    private String ID;
    private int PCODE;
    private String PNAME;
    private String PW;
    private int P_NUM;
    private String s_num;

    private ListView listView;
    private TeamListAdapter adapter;
    private List<TeamDto> teamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_in_project);
        Intent intent = getIntent();
        ID = intent.getStringExtra("아이디");
        PCODE = intent.getIntExtra("교수 코드", 0);
        PNAME = intent.getStringExtra("프로젝트 이름");
        PW = intent.getStringExtra("비밀번호");
        P_NUM = intent.getIntExtra("프로젝트 번호", 0);
        s_num = String.valueOf(P_NUM);
        String message = "학생 아이디 : " + ID + " / 이 프로젝트의 교수 코드 : " + PCODE + " / 프로젝트 이름 : " + PNAME + " / 이 프로젝트 비밀번호 : " + PW + " / 프로젝트 번호 : " + P_NUM;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//------------------------------------------------------------------------
        Context context = this;
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        formLayout = inflater.inflate(R.layout.make_team, null, false);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        listView = findViewById(R.id.teamListView);
        teamList = new ArrayList<TeamDto>();
        adapter = new TeamListAdapter(getApplicationContext(), teamList);
        listView.setAdapter(adapter);

        // 자료가 없을떄
        getTeams();
        // 입장 dialog띄우기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String r = new getCheckTask().execute(teamList.get(position).getStringNum(), ID, s_num).get();
                    Log.e("onItemClick", r);
                    if(r.equals("next")){
                        Log.e("onItemClick", "6");
                        // activity 전환
                        mainIntent(position);
                        Log.e("onItemClick", "7");
                    } else if(r.equals("success")){
                        Log.e("onItemClick", "8");
                        joinDialog(position);
                        Log.e("onItemClick", "9");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        // 자료가 있을때
        //바로 엑티비티 전환하기
    }
    public void mainIntent(int position){
        try{
            Intent intent = new Intent(StuProjectActivity.this, StuMainActivity.class);// 다음클래스 만들면 변경하기
            intent.putExtra("아이디", ID);
            intent.putExtra("교수 코드", PCODE);
            intent.putExtra("프로젝트이름", PNAME);
            intent.putExtra("프로젝트 번호", P_NUM);
            intent.putExtra("팀 이름", teamList.get(position).getName());
            intent.putExtra("팀 번호", teamList.get(position).getNum());
            //intent.putExtra("비밀번호", teamList.get(position).getPw());

            startActivity(intent);
        }catch (Exception e) {
            Log.e("intent", e.getMessage());
        }

    }
    class getCheckTask extends AsyncTask<String, Void, String>{
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";
                urlValue = "http://teamhub.cafe24.com/student_team_check.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "team_num=" + strings[0] + "&student_id=" + strings[1] + "&project_num=" + strings[2];
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
                    Log.e("check", receiveMsg);
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

    public void getTeams() {
        teamList.clear();
        try {
            String result = new getTeamsTask().execute(s_num, ID).get();
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
                urlValue = "http://teamhub.cafe24.com/student_get_teams.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "project=" + strings[0] + "&id=" + strings[1];
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
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.fab:
                animation();
                break;
            case R.id.fab1:
                break;
            case R.id.fab2:
                makeDialog();
                break;
        }
    }
    public void animation(){
        if(isFabOpen){
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;

        }
    }

    public void joinDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        formLayout = inflater.inflate(R.layout.join_team, null);
        builder.setView(formLayout);
        final EditText teamPassword = (EditText)formLayout.findViewById(R.id.teamPassword);
        // 형식 teamList.get(position).getNum()
        String pwd = teamPassword.getText().toString();
        final int p = position;
        builder.setPositiveButton("만들기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = teamPassword.getText().toString();
                        String num = String.valueOf(teamList.get(p).getNum());
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StuProjectActivity.this);
                        android.app.AlertDialog d;

                        try {
                            Log.e("a", "pwd = " + pwd + ", getPwd = " + teamList.get(p).getPw());
                            if(pwd.equals(teamList.get(p).getPw())){
                                Log.e("b", "pwd == getPw()");
                                Log.e("b2", "s_num = " + s_num + "ID = " + ID + "num = " + num);
                                String result = new StuProjectActivity.joinTeamTask().execute(s_num, ID, num).get();
                                Log.e("c", "result = " + result);
                                if(result.contains("success")) {
                                    Log.e("d", "success");
                                    d = builder
                                            .setMessage("팀에 가입했습니다.")
                                            .setPositiveButton("학인", null)
                                            .create();
                                    d.show();
                                    // 결과 : 성공일때 > 자동 방 입장
                                    //        실패일때 > 직접 방 입장
                                    //getTeams();

                                } else {
                                    Log.e("e", "else");
                                    d = builder
                                            .setMessage("실패")
                                            .setPositiveButton("학인", null)
                                            .create();
                                    d.show();
                                }
                            } else{
                                Log.e("f", "else2");
                                d = builder
                                        .setMessage("실패")
                                        .setPositiveButton("학인", null)
                                        .create();
                            }
                        }catch (Exception e) {

                        }
                    }
                })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .show();
        //mainIntent(position);
    }

    class joinTeamTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/student_join_team.jsp";
                Log.e("joinTeamTask", "start");
                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "project_Num=" + strings[0] + "&student_id=" + strings[1] + "&team_Num=" + strings[2];
                Log.e("joinTeamTask", "p_num = " + strings[0] + ", student_id = " + strings[1] + ", team_Num = " + strings[2]);
                osw.write(sendMsg);
                Log.e("joinTeamTask", "send");
                osw.flush();
                Log.e("joinTeamTask", "flush");
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.e("joinTeamTask", "recvMsg : " + receiveMsg);

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                    Log.e("joinTeamTask", "error");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }
    public void makeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        formLayout = inflater.inflate(R.layout.make_team, null);
        builder.setView(formLayout);
        final EditText teamName = (EditText)formLayout.findViewById(R.id.teamName);
        final EditText teamPassword = (EditText)formLayout.findViewById(R.id.teamPassword);

        builder.setPositiveButton("만들기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = teamName.getText().toString();
                        String pwd = teamPassword.getText().toString();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StuProjectActivity.this);
                        android.app.AlertDialog d;

                        if(name == null || name.equals("") || pwd == null || pwd.equals("")) {
                            d = builder
                                    .setMessage("모든 항목을 입력해주세요.!")
                                    .setNegativeButton("학인", null)
                                    .create();
                            d.show();
                            return;
                        }

                        try {
                            String result = new StuProjectActivity.validateTeam().execute(s_num, ID).get();
                            if(result.contains("exists")) {
                                d = builder
                                        .setMessage("이미 팀을 만들었습니다.")
                                        .setPositiveButton("학인", null)
                                        .create();
                                d.show();
                                return;
                            }


                            result = new StuProjectActivity.makeTeamTask().execute(s_num, ID, name, pwd).get();

                            if(result.contains("success")) {
                                d = builder
                                        .setMessage(name + " 프로젝트를 만들었습니다.")
                                        .setPositiveButton("학인", null)
                                        .create();
                                d.show();
                                // 결과 : 성공일때 > 자동 방 입장
                                //        실패일때 > 직접 방 입장
                                getTeams();
                            } else {
                                d = builder
                                        .setMessage("에러 발생")
                                        .setPositiveButton("학인", null)
                                        .create();
                                d.show();
                            }

                        }catch (Exception e) {

                        }

                    }
                })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }
    class validateTeam extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/student_validate_team.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "project_Num=" + strings[0] + "&name=" + strings[1];
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
    class makeTeamTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/student_make_team.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "project_Num=" + strings[0] + "&student_id=" + strings[1] + "&name=" + strings[2] + "&pw=" + strings[3];
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
