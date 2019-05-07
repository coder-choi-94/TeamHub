package com.example.choi.teamhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ProfessorRegisterActivity extends AppCompatActivity {
    ArrayAdapter adapter;
    EditText textId;
    EditText textPwd;
    EditText textName;
    EditText textPhone;

    Button btnValidate;
    Button btnRegister;

    AlertDialog dialog;
    Boolean validate = false;
    String validatedId = "";
    Spinner textDept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_register);

        textId = (EditText)findViewById(R.id.textId);
        textPwd = (EditText)findViewById(R.id.textPassword);
        textName = (EditText)findViewById(R.id.textName);
        textPhone = (EditText)findViewById(R.id.textPhone);
        textDept = (Spinner)findViewById(R.id.textDept);

        btnValidate = (Button)findViewById(R.id.btnValidate);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        adapter = ArrayAdapter.createFromResource(this, R.array.depts, android.R.layout.simple_dropdown_item_1line);
        textDept.setAdapter(adapter);
        btnValidate.setOnClickListener(btnListener);
        btnRegister.setOnClickListener(btnListener);
        textDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView)parent.getChildAt(0)).setTextColor(Color.rgb(25, 169, 188));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        String id, pwd, name, phone, dept;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnRegister : // 회원가입

                    id = textId.getText().toString();
                    pwd = textPwd.getText().toString();
                    name = textName.getText().toString();
                    phone = textPhone.getText().toString();
                    dept = textDept.getSelectedItem().toString();

                    if(id == null || id.equals("") ||
                            pwd == null || pwd.equals("") ||
                            name == null || name.equals("") ||
                            phone == null || phone.equals("") ||
                            dept == null || dept.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorRegisterActivity.this);
                        dialog = builder
                                .setMessage("먼저 모든 항목을 입력해주세요.")
                                .setNegativeButton("학인", null)
                                .create();
                        dialog.show();
                        return;
                    }

                    if(!validate) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorRegisterActivity.this);
                        dialog = builder
                                .setMessage("아이디 중복 확인을 해주세요.")
                                .setNegativeButton("학인", null)
                                .create();
                        dialog.show();
                        return;
                    }

                    if(!id.equals(validatedId)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorRegisterActivity.this);
                        dialog = builder
                                .setMessage("중복 확인된 아이디를 사용해주세요.")
                                .setNegativeButton("학인", null)
                                .create();
                        dialog.show();
                        return;
                    }

                    try {
                        String result  = new RegisterTask().execute(id,pwd,name,phone,dept).get();
                        if(result.equals("id")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorRegisterActivity.this);
                            dialog = builder
                                    .setMessage("이미 존재하는 아이디 입니다.")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                            return;
                        } else if(result.equals("ok")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorRegisterActivity.this);
                            dialog = builder
                                    .setMessage("회원 등록에 성공했습니다!")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }
                    }catch (Exception e) {

                    }
                    break;
                case R.id.btnValidate:
                    id = textId.getText().toString();
                    if(id == null || id.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorRegisterActivity.this);
                        dialog = builder
                                .setMessage("먼저 아이디를 입력해 주세요.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                    try {
                        String result  = new ValidateTask().execute(id).get();
                        if(result.equals("error")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorRegisterActivity.this);
                            dialog = builder
                                    .setMessage("이미 존재하는 아이디입니다.")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                            return;
                        } else if(result.equals("success")) {
                            validate = true;
                            validatedId = id;
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorRegisterActivity.this);
                            dialog = builder
                                    .setMessage("사용할 수 있는 아이디입니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                            return;
                        }
                    }catch (Exception e) {

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
                URL url = new URL("http://teamhub.cafe24.com/professor_register.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pw="+strings[1]+"&name="+strings[2]+"&phone="+strings[3]+"&dept="+strings[4];
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

    class ValidateTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://teamhub.cafe24.com/professor_validate.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0];
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
                    System.out.println(receiveMsg);

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
