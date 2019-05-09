package com.example.choi.teamhub;

import android.app.Dialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.teamhub.DTO.ProjectDto;

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

public class ProIndexActivity extends AppCompatActivity  implements View.OnClickListener {
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab;
    private View formLayout;
    private Intent intent;
    private String code;    ///인텐트로 받은 교수코드
    private String professor_name;// 인텐트로받은 교수이름
    private ListView listView;
    private ProjectListAdapter adapter;
    private List<ProjectDto> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_index);


        intent = getIntent();
        code = intent.getStringExtra("code");
        professor_name = intent.getStringExtra("professor_name");

        Context context = this;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        formLayout = inflater.inflate(R.layout.make_project, null, false);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(this);

        listView = findViewById(R.id.projectListView);
        projectList = new ArrayList<ProjectDto>();

        adapter = new ProjectListAdapter(getApplicationContext(), projectList);
        listView.setAdapter(adapter);

        getProjects();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProIndexActivity.this, "code : " + code + ", projectList: " + projectList.get(position).getName(), Toast.LENGTH_LONG).show();
                // 이부분에 클릭시 팀선택화면으로 넘어가도록 만들기.
                projectIntent(position);
            }
        });

    }


    public void projectIntent(int position){
        try{
            Intent intent = new Intent(ProIndexActivity.this, ProProjectActivity.class);
            intent.putExtra("프로젝트 번호", projectList.get(position).getNum());
            intent.putExtra("프로젝트 이름", projectList.get(position).getName());
            intent.putExtra("교수 이름", professor_name);
            intent.putExtra("교수 코드", code);
            startActivity(intent);
        }catch (Exception e) {
            Log.e("ProjectActivity.Intent", e.getMessage());
        }
    }


    public void getProjects() {
        projectList.clear();
        try {
            String result = new getProjectsTask().execute(code).get();

            JSONObject resultJsonObj = new JSONObject(result);
            JSONArray resultJsonData = resultJsonObj.getJSONArray("projects");
            JSONObject jsonObj;
            for(int i=0 ; i<resultJsonData.length() ; i++) {
                jsonObj = resultJsonData.getJSONObject(i);
                ProjectDto projectDto = new ProjectDto();
                projectDto.setNum(jsonObj.getInt("num"));
                projectDto.setProfessor_code(jsonObj.getInt("professor_code"));
                projectDto.setName(jsonObj.getString("name"));
                projectDto.setPw(jsonObj.getString("pw"));
                projectDto.setProfessor_name(jsonObj.getString("professor_name"));
                projectList.add(projectDto);    //리스트뷰에 한개씩 추가
            }



        }catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        Dialog();
    }
    public void Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        formLayout = inflater.inflate(R.layout.make_project, null);
        builder.setView(formLayout);
        final EditText roomName = (EditText)formLayout.findViewById(R.id.roomName);
        final EditText roomPassword = (EditText)formLayout.findViewById(R.id.roomPassword);

                builder.setPositiveButton("만들기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = roomName.getText().toString();
                        String pwd = roomPassword.getText().toString();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProIndexActivity.this);
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
                            String result = new validateProject().execute(code, name).get();
                            if(result.contains("exists")) {
                                d = builder
                                        .setMessage("프로젝트 이름이 이미 존재합니다.")
                                        .setPositiveButton("학인", null)
                                        .create();
                                d.show();
                                return;
                            }


                            result = new makeProjectTask().execute(code, name, pwd, professor_name).get();
                            if(result.contains("success")) {
                                d = builder
                                        .setMessage(name + " 프로젝트를 만들었습니다.")
                                        .setPositiveButton("학인", null)
                                        .create();
                                d.show();
                                getProjects();
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
    class getProjectsTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/professor_get_projects.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "code=" + strings[0];
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

    class validateProject extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/professor_validate_project.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "professorcode=" + strings[0] + "&name=" + strings[1];
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

    class makeProjectTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/professor_make_project.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "code=" + strings[0] + "&name=" + strings[1] + "&pw=" + strings[2] + "&pname=" + strings[3];
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

    public class ProjectListAdapter extends BaseAdapter {

        private Context context;
        private List<ProjectDto> projectList;

        public ProjectListAdapter(Context context, List<ProjectDto> projectList){
            this.context = context;
            this.projectList = projectList;
        }

        //출력할 총갯수를 설정하는 메소드
        @Override
        public int getCount() {
            return projectList.size();
        }

        //특정한 유저를 반환하는 메소드
        @Override
        public Object getItem(int i) {
            return projectList.get(i);
        }

        //아이템별 아이디를 반환하는 메소드
        @Override
        public long getItemId(int i) {
            return i;
        }

        //가장 중요한 부분
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(context, R.layout.project_row, null);

            //뷰에 다음 컴포넌트들을 연결시켜줌
            TextView professorName = (TextView)v.findViewById(R.id.professorName);
            TextView projectName = (TextView)v.findViewById(R.id.projectName);

            professorName.setText(projectList.get(i).getProfessor_name());
            projectName.setText(projectList.get(i).getName());

            //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
            v.setTag(projectList.get(i).getProfessor_name());

            //만든뷰를 반환함
            return v;
        }
    }


}
