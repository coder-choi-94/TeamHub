package com.example.choi.teamhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ProfessorTeamProjectJoin extends AppCompatActivity {
    EditText textName;
    EditText textPassword;
    EditText textCode;
    Button btnJoin;
    AlertDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_pro);

        textName = (EditText) findViewById(R.id.textName);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textCode = (EditText) findViewById(R.id.textCode);

        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(btnListener);
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        String p_name, pwd, code;

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnJoin: // 교수님이 만든 프로젝트 방 입장
                    p_name = textName.getText().toString();
                    pwd = textPassword.getText().toString();
                    code = textCode.getText().toString();

                    if (p_name == null || p_name.equals("") ||
                            pwd == null || pwd.equals("") ||
                            code == null || code.equals("") ) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorTeamProjectJoin.this);
                        dialog = builder
                                .setMessage("먼저 모든 항목을 입력해주세요.")
                                .setNegativeButton("학인", null)
                                .create();
                        dialog.show();
                        return;
                    }

                    try {
                        String result = new RegisterTask().execute( p_name, pwd, code).get();
                        if (!result.equals("p_name")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorTeamProjectJoin.this);
                            dialog = builder
                                    .setMessage("존재하지 않는 프로젝트 이름입니다.")
                                    .setNegativeButton("확인", null)
                                    .create();

                            dialog.show();
                            return;
                        }
                            else if (!result.equals("pwd")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorTeamProjectJoin.this);
                            dialog = builder
                                    .setMessage("비밀번호가 맞지 않습니다.")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                            return;
                        }
                            else if (!result.equals("pwd")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorTeamProjectJoin.this);
                            dialog = builder
                                    .setMessage("존재하지 않는 코드 입니다.")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                            return;
                        }
                            else if (result.equals("ok")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorTeamProjectJoin.this);
                            dialog = builder
                                    .setMessage("입장에 성공했습니다!")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(ProfessorTeamProjectJoin.this, StuIndexActivity.class); /*StuIndexActivity를 넘겨갈 페이지로 */
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }
                    } catch (Exception e) {
                    }
                    break;
            }
        }
    };

    class RegisterTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://teamhub.cafe24.com/professor_teamjoin.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "p_name="+strings[0]+"&pw="+strings[1]+"&code="+strings[2];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
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