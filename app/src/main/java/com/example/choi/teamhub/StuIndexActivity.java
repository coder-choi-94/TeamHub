package com.example.choi.teamhub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class StuIndexActivity extends AppCompatActivity {
    FloatingActionButton fab;
    View formLayout;
    private String ID;

    private ListView listView;
    private ProjectListAdapter adapter;
    private List<ProjectDto> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_index);



        ID = getIntent().getStringExtra("id");

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                search();
            }
        });


        listView = findViewById(R.id.projectListView);
        projectList = new ArrayList<ProjectDto>();

        adapter = new ProjectListAdapter(getApplicationContext(), projectList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StuIndexActivity.this, StuProjectActivity.class);
                intent.putExtra("아이디", ID);
                intent.putExtra("교수 코드", projectList.get(position).getProfessor_code());
                intent.putExtra("프로젝트 이름", projectList.get(position).getName());
                intent.putExtra("비밀번호", projectList.get(position).getPw());
                intent.putExtra("프로젝트 번호", projectList.get(position).getNum());
                startActivity(intent);
            }
        });

        getProjects();
    }
    public void search(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        formLayout = inflater.inflate(R.layout.search_project, null);
        builder.setView(formLayout);

        final EditText professorCode = (EditText)formLayout.findViewById(R.id.professorCode);
        final EditText roomName = (EditText)formLayout.findViewById(R.id.roomName);
        final EditText roomPassword = (EditText)formLayout.findViewById(R.id.roomPassword);

        builder.setPositiveButton("참여하기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String code = professorCode.getText().toString();
                        final String name = roomName.getText().toString();
                        final String pwd = roomPassword.getText().toString();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StuIndexActivity.this);
                        android.app.AlertDialog d;

                        if (name == null || name.equals("") || pwd == null || pwd.equals("") || code == null || code == "") {
                            d = builder
                                    .setMessage("모든 항목을 입력해주세요.!")
                                    .setNegativeButton("학인", null)
                                    .create();
                            d.show();
                            return;
                        }

                        try {
                            final String result = new validateProjectTask().execute(code, name, pwd).get();
                            if (!result.contains("exists")) {// 존재하지않는 방이라면
                                d = builder
                                        .setMessage("해당 프로젝트 방을 찾을 수 없습니다.")
                                        .setPositiveButton("학인", null)
                                        .create();
                                d.show();
                                return;
                            } else {
                                d = builder
                                        .setMessage(name + " 프로젝트에 참여하시겠습니까?")
                                        .setPositiveButton("학인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String[] resultArr = result.split(":");
                                                String projectNum = resultArr[1];    //프로젝트 기본키 얻어옴

                                                try {
                                                    String rst = new joinProjectTask().execute(projectNum, ID).get();
                                                    if(rst.equals("success")) {
                                                        getProjects();  //참여중인 프로젝트 리스트뷰를 동기화 한번 해주고 액티비티 이동하기
                                                        Intent intent = new Intent(StuIndexActivity.this, StuProjectActivity.class);
                                                        intent.putExtra("아이디", ID);
                                                        intent.putExtra("교수 코드", Integer.parseInt(code));
                                                        intent.putExtra("프로젝트 이름", name);
                                                        intent.putExtra("비밀번호", pwd);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(StuIndexActivity.this, "오류 발생.. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                                    }

                                                }catch(Exception e) {

                                                }
                                            }
                                        })
                                        .setNegativeButton("취소",null)
                                        .create();
                                d.show();

                            }

                        } catch(Exception e) {
                            e.printStackTrace();
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
    class validateProjectTask extends AsyncTask<String, Void, String> {
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
                sendMsg = "professorcode=" + strings[0] + "&name=" + strings[1] + "&pw=" + strings[2];
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

    class getProjectsTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/student_get_projects.jsp";

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

    public void getProjects() {
        projectList.clear();
        try {
            String result = new getProjectsTask().execute(ID).get();

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

    class joinProjectTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/student_join_project.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "projectnum=" + strings[0] + "&id=" + strings[1];
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
