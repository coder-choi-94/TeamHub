package com.example.choi.teamhub.Fragment;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.teamhub.BuildConfig;
import com.example.choi.teamhub.DTO.TeamDto;
import com.example.choi.teamhub.DTO.TodoDto;
import com.example.choi.teamhub.R;
import com.example.choi.teamhub.StuIndexActivity;
import com.example.choi.teamhub.StuMainActivity;
import com.example.choi.teamhub.StuProjectActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;

public class TodoFragment extends Fragment {

    private String userId;
    private String userName;
    private String userPhone;
    private String userDept;
    private String userSno;
    private int projectNum;
    private int teamNum;

    String File_Name;
    String File_extend;

    String fileURL; // URL
    String Save_Path;
    String Save_folder;

    ProgressDialog progress;
    DownloadThread dThread;

    FloatingActionButton fab;
    View formLayout;
    EditText todoTitle;
    EditText todoText;
    Button todoUploadBtn;
    Button todoGetFileBtn;
    TextView uploadedFileName;
    ImageView imageView;
    TextView todoFilaName;
    TextView todoContent;


    //파일 업로드 관련
    private String img_path = new String();
    private String file_path = new String();
    private String serverURL = "http://teamhub.cafe24.com/uploadImage.jsp";  //<<서버주소
//    private String serverURL = "http://localhost:8181/TeamHub/uploadImage.jsp";  //<<서버주소
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private String imageName = null;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    private ListView listView;
    private TodoListViewAdapter adapter;
    private List<TodoDto> todoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        userId = getArguments().getString("userId");
        userName = getArguments().getString("userName");
        userPhone = getArguments().getString("userPhone");
        userDept = getArguments().getString("userDept");
        userSno = getArguments().getString("userSno");
        projectNum = getArguments().getInt("프로젝트 번호");
        teamNum = getArguments().getInt("팀 번호");



        Log.v("@RECV", "스트릭트모드직전");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        Log.v("@RECV", "스트릭트모드직후");


        View view = inflater.inflate(R.layout.activity_todo_fragment, container, false);

        listView = view.findViewById(R.id.todoListView);
        todoList = new ArrayList<TodoDto>();

        adapter = new TodoListViewAdapter(getActivity().getApplicationContext(), todoList);
        listView.setAdapter(adapter);

        Log.v("@RECV", "투두가져오기직전");
        getTodos();
        Log.v("@RECV", "투두가져오기직후");
        listView.setSelection(todoList.size()-1);





        imageView = (ImageView)view.findViewById(R.id.imageView);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                LayoutInflater inflater = getLayoutInflater();
                formLayout = inflater.inflate(R.layout.activity_todo_upload_file, null);
                builder.setView(formLayout);

                todoText = (EditText)formLayout.findViewById(R.id.todoText);
                todoTitle = (EditText)formLayout.findViewById(R.id.todoTitle);
                todoUploadBtn = (Button)formLayout.findViewById(R.id.todoUploadBtn);
                uploadedFileName = (TextView)formLayout.findViewById(R.id.uploadedFileName);
                todoUploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("@RECV", "파일가져오기버트누름");
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        Log.v("@upload", "TYPE:" + MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        Log.v("@RECV", "파일가져오기 인테트실행직전");
                        startActivityForResult(intent, 1);
                        Log.v("@RECV", "파일가져오기 인테트실행직후");
                    }
                });

//                final EditText professorCode = (EditText)formLayout.findViewById(R.id.professorCode);
//                final EditText roomName = (EditText)formLayout.findViewById(R.id.roomName);
//                final EditText roomPassword = (EditText)formLayout.findViewById(R.id.roomPassword);

                builder.setPositiveButton("파일 올리기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v("!@#!@#", todoText.getText().toString());
                                Log.v("!@#!@#", todoTitle.getText().toString());
                                if(todoText.getText().toString().equals("") || todoText.getText().toString() == null ||
                                        todoTitle.getText().toString().equals("") || todoTitle.getText().toString() == null ||
                                        uploadedFileName.getText().toString().equals("파일 없음")) {
                                    Toast.makeText(getActivity().getApplicationContext(), "모든 항목을 입력해 주세요", Toast.LENGTH_SHORT).show();
                                    return;
//                                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                                    alert.setMessage("모든 항목을 입력해 주세요");
//                                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            });
//                                    alert.show();
                                } else {
                                    try {
                                        DoFileUpload(serverURL, file_path);
                                        Log.v("send", "upload SUCCESS");
                                        getTodos();
                                        adapter.notifyDataSetChanged();
                                        listView.setSelection(todoList.size()-1);
                                    } catch (Exception e) {
                                        Log.v("send", "upload Failure");
                                        e.printStackTrace();
                                    }
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
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //내가 올린게 아니라면 리턴

                if(!todoList.get(position).getUploaderId().equals(userId)) {
                    Toast.makeText(getActivity().getApplicationContext(), "내가 올린 파일만 삭제 가능합니다.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage(todoList.get(position).getTitle() + " 파일을 지우시겠습니까?");
                final int index = position;
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTodo(index);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("@TEST", "클릭이벤트시작");
                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                LayoutInflater inflater = getLayoutInflater();
                formLayout = inflater.inflate(R.layout.activity_todo_file_view, null);
                builder.setView(formLayout);
                Log.v("@TEST", "빌더 뷰 입힘");

                todoFilaName = (TextView)formLayout.findViewById(R.id.todoFileName);
                todoFilaName.setText(todoList.get(position).getTitle());
                todoContent = (TextView)formLayout.findViewById(R.id.todoContent);
                todoContent.setText(todoList.get(position).getContent());
                todoGetFileBtn = (Button)formLayout.findViewById(R.id.todoGetFileBtn);

                Log.v("@TEST", "각 폼 값 가져옴");
                final int index = position;
                todoGetFileBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setMessage(todoList.get(index).getTitle() + " 파일을 열어 보시겠습니까?");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File_Name =
                                        todoList.get(index).getFilePath().substring(
                                                (todoList.get(index).getFilePath().lastIndexOf("/")+1),
                                                todoList.get(index).getFilePath().length()
                                        );
                                Log.v("@RECV", "FILE_NAME = "+File_Name );
                                File_extend =
                                        todoList.get(index).getFilePath().substring(
                                                (todoList.get(index).getFilePath().lastIndexOf(".")+1),
                                                todoList.get(index).getFilePath().length()
                                        );
                                Log.v("@RECV", "파일명 : " + File_Name + " / 확장자 : " + File_extend);
                                try {
                                    fileURL = "http://teamhub.cafe24.com/downloadImage.jsp?fileName=" + URLEncoder.encode(File_Name, "UTF-8"); // URL
                                } catch(Exception e) {
                                    Log.v("@RECV", "파라미터인코딩 실패");
                                    e.printStackTrace();
                                }

                                Save_folder = "/teamhub";

                                // 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.
                                String ext = Environment.getExternalStorageState();

                                if (ext.equals(Environment.MEDIA_MOUNTED)) {
                                    Log.v("@RECV", "마운티드?");
                                    Save_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
                                    Log.v("@RECV", "저장폴더 : " + Save_Path);
                                }
                                File dir = new File(Save_Path);

                                // 폴더가 존재하지 않을 경우 폴더를 만듬
                                if (!dir.exists()) {
                                    dir.mkdir();
                                    Log.v("@RECV", "폴더없어서 만듦");
                                }

                                File_Name = todoList.get(index).getTitle() + todoList.get(index).getFilePath().substring(
                                        todoList.get(index).getFilePath().lastIndexOf("."),
                                        todoList.get(index).getFilePath().length()
                                );
                                // 다운로드 폴더에 동일한 파일명이 존재하는지 확인해서 없으면 다운받고 있으면 해당 파일 실행시킴.
                                if (new File(Save_Path + "/" + File_Name).exists() == false) {
                                    Log.v("@RECV", "파일 없어서 다운받겠음! : " + Save_Path + "/" + File_Name);
                                    progress = ProgressDialog.show(getActivity(), "", "파일 다운로드중..");
                                    dThread = new DownloadThread(fileURL, Save_Path + "/" + File_Name);
                                    dThread.start();
                                } else {
                                    Log.v("@RECV", "파일 있어서 바로 보여주겠음!");
                                    showDownloadFile();
                                }
                            }
                        });
                        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();     //닫기
                            }
                        });
                        alert.show();
                    }
                });
                builder.show();
            }
        });
        return view;
    }

    public void deleteTodo(int position) {
        try {
            Log.v("@RECV", "num@ => " + todoList.get(position).getNum());
            String result = new delTodoTask().execute(todoList.get(position).getNum()+"").get();
            Log.v("@RECV", result.length()+"/"+result);
            if(result.equals("SUCCESS")) {
                Toast.makeText(getActivity().getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                getTodos();
                adapter.notifyDataSetChanged();
                listView.setSelection(todoList.size()-1);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e) {
            Log.v("@GET TODOS", e.getMessage());
            e.printStackTrace();
        }
    }

    public void getTodos() {
        todoList.clear();
        Log.v("@GET TODOS", "todoListSIze" + todoList.size());
        try {
            Log.v("@RECV", "투두가져오기 테스크 직전");
            String result = new getTodosTask().execute(teamNum+"").get();
            Log.v("@RECV", "투두가져오기 테스크 직후");
            Log.v("@GET TODOS", "테스크 끝");
            Log.v("@GET TODOS", result);
            Log.v("@GET TODOS", result.length()+"");
            JSONObject resultJsonObj = new JSONObject(result);
            JSONArray resultJsonData = resultJsonObj.getJSONArray("todos");
            JSONObject jsonObj;
            for(int i=0 ; i<resultJsonData.length() ; i++) {
                jsonObj = resultJsonData.getJSONObject(i);
                TodoDto todoDto = new TodoDto();
                todoDto.setNum(jsonObj.getInt("num"));
                todoDto.setTeamNum(jsonObj.getInt("teamNum"));
                todoDto.setUploaderId(jsonObj.getString("uploaderId"));
                todoDto.setUploader(jsonObj.getString("uploader"));
                todoDto.setTitle(jsonObj.getString("title"));
                todoDto.setContent(jsonObj.getString("content"));
                todoDto.setFilePath(jsonObj.getString("filePath"));
                todoList.add(todoDto);    //리스트뷰에 한개씩 추가
                Log.v("@GET TODOS", todoDto.getTitle());
            }

        }catch (Exception e) {
            Log.v("@GET TODOS", e.getMessage());
            e.printStackTrace();
        }
    }
    class getTodosTask extends AsyncTask<String, Void, String> {

        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";
                urlValue = "http://teamhub.cafe24.com/student_get_todos.jsp";

                URL url = new URL(urlValue);
                Log.v("@RECV", "투두가져오기 커넥트직전");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.v("@RECV", "투두가져오기 커넥트직후");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "teamNum=" + strings[0];
                Log.v("@GET TODOS","전송전");
                osw.write(sendMsg);
                osw.flush();
                Log.v("@RECV", "투두가져오기 데이터보냄");
                Log.v("@RECV","전송후");
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    Log.v("@RECV","응답옴");
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                        Log.v("@RECV res=>", str);
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

    class delTodoTask extends AsyncTask<String, Void, String> {

        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";
                urlValue = "http://teamhub.cafe24.com/student_del_todo.jsp";
                URL url = new URL(urlValue);
                Log.v("@RECV", "삭제 커넥트직전");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.v("@RECV", "삭제 커넥트직후");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "num=" + strings[0];
                Log.v("@GET TODOS","전송전");
                osw.write(sendMsg);
                osw.flush();
                Log.v("@RECV", "삭제 데이터보냄");
                Log.v("@RECV","전송후");
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    Log.v("@RECV","응답옴");
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                        Log.v("@RECV res=>", str);
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
    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(data, proj, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        this.imageName = imgName;

        return imgPath;
    }

    public void DoFileUpload(String apiUrl, String absolutePath) {
        HttpFileUpload(apiUrl, "", absolutePath);
    }

    public void HttpFileUpload(String urlString, String params, String fileName) {
        try {
            Log.v("@RECV", "HttpFileUpload 실행시작");
            //파일명 날짜로
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date nowdate = new Date();
            String dateString = formatter.format(nowdate);
            Log.v("@RECV", "dateString => "+dateString);
            //파일명 날짜.확장자 여기서 겹치는건 jsp defaultFileRenamePolicy가 처리
            String parsedFileName = dateString +File_Name.substring(File_Name.lastIndexOf("."), File_Name.length());
            Log.v("@RECV", "parsedFileName => "+parsedFileName);

            urlString += "?seq=" + teamNum + "&id=" + userId + "&content=" +
                    URLEncoder.encode(todoText.getText().toString(), "UTF-8") + "&title=" + URLEncoder.encode(todoTitle.getText().toString(), "UTF-8");
            Log.v("@RECV : url", urlString);
            Log.v("@RECV : fileName", fileName);
            FileInputStream mFileInputStream = new FileInputStream(fileName);
            Log.v("@RECV", "1 / " + mFileInputStream);
            URL connectUrl = new URL(urlString);
            Log.v("@RECV", "2 / " + connectUrl);
            Log.d("@RECV", "mFileInputStream  is " + mFileInputStream);
            Log.v("@RECV", "3 / " + mFileInputStream);

            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            Log.v("@RECV", "3.1 / ");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            Log.v("@RECV", "3.2 / ");
            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            Log.v("@RECV", "3.3 / ");
            dos.writeBytes(twoHyphens + boundary + lineEnd);



            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + URLEncoder.encode(parsedFileName, "UTF-8") + "\"" + lineEnd);
//            dos.writeBytes("Content-Disposition: form-data; seq=\""+teamNum+"\";id=\""+userId+"\";content=\""+todoText+"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.v("@RECV", "4 / ");
            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            Log.v("@RECV", "5 / ");
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            Log.v("@RECV", "6 / ");
            Log.d("@RECV", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("@RECV", "File is written");
            mFileInputStream.close();
            dos.flush();
            // finish upload...

            // get response
            InputStream is = conn.getInputStream();

            StringBuffer b = new StringBuffer();
            for (int ch = 0; (ch = is.read()) != -1; ) {
                b.append((char) ch);
            }
            is.close();
            Log.e("@RECV", b.toString() + " / " + todoText.getText().toString());


        } catch (Exception e) {
            Log.d("@RECV", "exception " + e.getMessage());
            e.printStackTrace();
            // TODO: handle exception
        }
    } // end of HttpFileUpload()

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    Log.v("@RECV", " data.getData.getpath => " + data.getData().getPath());
                    File_Name = data.getData().getPath().substring(data.getData().getPath().lastIndexOf("/")+1, data.getData().getPath().length());
                    uploadedFileName.setText(File_Name);
                    file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + File_Name;
//                    file_path = data.getData().getPath();


//                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
//                    // 선택한 이미지에서 비트맵 생성
//                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();

//                    //이곳에 이미지 회전 현상 오류 해결 필요..
//
//
//                    ((ImageView)formLayout.findViewById(R.id.imageView)).setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class TodoListViewAdapter extends BaseAdapter {

        private Context context;
        private List<TodoDto> todoList;

        public TodoListViewAdapter(Context context, List<TodoDto> todoList) {
            this.context = context;
            this.todoList = todoList;
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
            View v = View.inflate(context, R.layout.todo_fragment_row, null);

            //뷰에 다음 컴포넌트들을 연결시켜줌
            TextView uploader= (TextView)v.findViewById(R.id.uploader);
            TextView fileName = (TextView)v.findViewById(R.id.fileName);

            uploader.setText(todoList.get(i).getUploader() + " 학우가 파일을 올렸습니다");
            fileName.setText(todoList.get(i).getTitle());

            //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
            v.setTag(todoList.get(i).getTitle());

            //만든뷰를 반환함
            return v;
        }
    }

    class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
        }

        @Override
        public void run() {
            URL imgurl;
            int Read;


            try {
                Log.v("@DOWNLOAD", "다운쓰레드 러닝안  / serverUrl = " + ServerUrl + " / LocalPath" + LocalPath);
                imgurl = new URL(ServerUrl);
                Log.v("@DOWNLOAD", "url커넥트전");
                HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
                conn.connect();
                Log.v("@DOWNLOAD", "url커넥트성공");
                int len = conn.getContentLength();
                Log.v("@DOWNLOAD", "conn.getContentLength() : " + conn.getContentLength());
                byte[] tmpByte = new byte[len];

                InputStream is = conn.getInputStream();
                File file = new File(LocalPath);
                FileOutputStream fos = new FileOutputStream(file);

                for (;;) {
                    Read = is.read(tmpByte);
                    Log.v("@DOWNLOAD", "읽기!");
                    if (Read <= 0) {
                        break;
                    }
                    Log.v("@DOWNLOAD", "쓰기!");
                    fos.write(tmpByte, 0, Read);
                }

                is.close();
                fos.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                Log.e("ERROR1", e.getMessage());
            } catch (IOException e) {
                Log.e("ERROR2", e.getMessage());
                e.printStackTrace();
            }
            mAfterDown.sendEmptyMessage(0);
        }
    }

    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            progress.dismiss();
            Log.v("@DOWNLOAD", "끝?");
            // 파일 다운로드 종료 후 다운받은 파일을 실행시킨다.
            showDownloadFile();
        }
    };

    private void showDownloadFile() {
        Log.v("@RECV", "다운 후파일 실행!");



        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        File path = getActivity().getFilesDir();
        File file = new File(Save_Path+"/"+File_Name);
        Uri cameraImageUri;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// API 24 이상 일경우..
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            String strpa = getActivity().getApplicationContext().getPackageName();
            cameraImageUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                    BuildConfig.APPLICATION_ID+".fileprovider", file);
        } else {// API 24 미만 일경우..
            cameraImageUri = Uri.fromFile(file);
        }


        //파일 확장자 별로 mime type 지정해 준다.
        if (File_extend.equals("pdf")) {
            intent.setDataAndType(cameraImageUri, "application/pdf");
        } else if(File_extend.equals("mp3")) {
            intent.setDataAndType(cameraImageUri, "audio/*");
        } else if(File_extend.equals("mp4")) {
            intent.setDataAndType(cameraImageUri, "video/*");
        } else if(File_extend.equals("jpg") || File_extend.equals("jpeg") ||
                File_extend.equals("JPG") || File_extend.equals("gif") ||
                File_extend.equals("png") || File_extend.equals("bmp")) {
            intent.setDataAndType(cameraImageUri, "image/*");
        } else if(File_extend.equals("txt")) {
            intent.setDataAndType(cameraImageUri, "text/*");
        } else if(File_extend.equals("doc") || File_extend.equals("docx")) {
            intent.setDataAndType(cameraImageUri, "application/msword");
        } else if(File_extend.equals("xls") || File_extend.equals("xlsx")) {
            intent.setDataAndType(cameraImageUri, "application/vnd.ms-excel");
        } else if(File_extend.equals("ppt") || File_extend.equals("pptx")) {
            intent.setDataAndType(cameraImageUri, "application/vnd.ms-powerpoint");
        }

        try{
            startActivity(Intent.createChooser(intent, "실행할 앱을 선택해주세요"));
        } catch(ActivityNotFoundException e){
            Toast.makeText(getActivity(), "해당파일을 실항할 수 있는 어플리케이션이 없습니다.\n파일을 열 수 없습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}