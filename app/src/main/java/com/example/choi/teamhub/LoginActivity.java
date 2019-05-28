package com.example.choi.teamhub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText textId;
    EditText textPwd;

    AlertDialog dialog;

    RadioGroup rg;
    int id;
    RadioButton checkedType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textId = (EditText)findViewById(R.id.editText_id);
        textPwd = (EditText)findViewById(R.id.editText_pw);
    }

    public void Click_join_student(View view) {
        Intent intent = new Intent(LoginActivity.this, StudentRegisterActivity.class);
        startActivity(intent);
    }

    public void Click_join_professor(View view) {
        Intent intent = new Intent(LoginActivity.this, ProfessorRegisterActivity.class);
        startActivity(intent);
    }

    public void Click_login(View view) {
        String id = textId.getText().toString();
        String pwd = textPwd.getText().toString();

        if(id == null || id.equals("") || pwd == null || pwd.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            dialog = builder
                    .setMessage("모든 항목을 입력해주세요.")
                    .setNegativeButton("학인", null)
                    .create();
            dialog.show();
            return;
        }
        try {
            final String result  = new LoginTask().execute(id,pwd).get();
            final JSONObject resultJsonObj = new JSONObject(result);

            Toast.makeText(this, resultJsonObj.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, resultJsonObj.getString("result"), Toast.LENGTH_SHORT).show();

            if(resultJsonObj.getString("result").equals("FAILURE")) { //로그인 실패시
                if(resultJsonObj.getString("content").equals("id")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder
                            .setMessage("존재하지 않는 아이디입니다!")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                } else if(resultJsonObj.getString("content").equals("pw")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder
                            .setMessage("비밀번호를 잘못 입력하셨습니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
            } else {    //로그인 성공시
                final String userId;
                final String userName;
                final String userPhone;
                final String userDept;
                final String userSno;
                final String professorCode;
                final String professorName;
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                final Intent intent;

                if (checkedType.getText().toString().equals("학생")) { //학생 로그인의 경우 학생 로그인 jsp페이지로부터 온 데이터를 받는다.
                    userId = resultJsonObj.getString("id");
                    userName = resultJsonObj.getString("name");
                    userPhone = resultJsonObj.getString("phone");
                    userDept = resultJsonObj.getString("dept");
                    userSno = resultJsonObj.getString("sno");

                    intent = new Intent(LoginActivity.this, StuIndexActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    intent.putExtra("userDept", userDept);
                    intent.putExtra("userSno", userSno);
                } else {                                                 //교수 로그인의 경우 교수 로그인 jsp페이지로부터 온 데이터를 받는다.
                    professorCode = resultJsonObj.getString("code");
                    professorName = resultJsonObj.getString("name");

                    intent = new Intent(LoginActivity.this, ProIndexActivity.class);
                    intent.putExtra("code", professorCode);
                    //intent.putExtra("professor_name", professorName);
                }

                dialog = builder
                        .setMessage("접속 성공!")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }
                        })
                        .create();
                dialog.show();
            }
        }catch (Exception e) {

        }
    }

    class LoginTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                rg = (RadioGroup)findViewById(R.id.typeGroup);
                id = rg.getCheckedRadioButtonId();
                checkedType = (RadioButton)findViewById(id);

                @SuppressLint("WrongThread") int id = rg.getCheckedRadioButtonId();
                RadioButton checkedType = (RadioButton)findViewById(id);


                if(checkedType.getText().toString().equals("학생"))
                    urlValue = "http://teamhub.cafe24.com/student_login.jsp";
                else
                    urlValue = "http://teamhub.cafe24.com/professor_login.jsp";

                Log.v("요청 url", urlValue);

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pw="+strings[1];
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
