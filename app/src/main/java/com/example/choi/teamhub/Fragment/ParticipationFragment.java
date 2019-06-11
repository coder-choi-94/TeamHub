package com.example.choi.teamhub.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.choi.teamhub.DTO.ParticipationDto;
import com.example.choi.teamhub.DTO.ProjectDto;
import com.example.choi.teamhub.ProIndexActivity;
import com.example.choi.teamhub.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.animation.Easing.EasingFunction;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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

public class ParticipationFragment extends Fragment {
    PieChart chart;
    View fragmentView;
    private String userId;
    private String userName;
    private String userPhone;
    private String userDept;
    private int projectNum;
    private int teamNum;
    private String professorNum;
    private String strTeamNum;
    private List<ParticipationDto> list;
    private int total;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //fragmentView = onCreateView(inflater, container, savedInstanceState);
        projectNum = getArguments().getInt("프로젝트 번호");
        teamNum = getArguments().getInt("팀 번호");
        professorNum = getArguments().getString("code");
        userName = getArguments().getString("name");
        userId = getArguments().getString("id");
        userPhone = getArguments().getString("phone");
        userDept = getArguments().getString("dept");
        strTeamNum = String.valueOf(teamNum);

        list = new ArrayList<ParticipationDto>();
        list.clear(); total = 0;
        getFiles();
        for(int i =0; i< list.size(); i++){
            total += list.get(i).getCount();
            Log.e("total", String.valueOf(total));
        }

        fragmentView = inflater.inflate(R.layout.activity_notice_fragment, container, false);

        chart = (PieChart) fragmentView.findViewById(R.id.chart);
        chart.invalidate();
        chart.setNoDataText("");  // 차트에 데이터가 없을 때 차트 대신 뜰 값
        chart.setCenterText("참여도");
        chart.setCenterTextSize(20f);  // 중앙에 참여도 글자 크기
        chart.setEntryLabelTextSize(15f);  // 차트에 숫자 크기
        chart.setEntryLabelColor(R.color.colorChat);

        chart.animateXY(1200, 1200, Easing.EaseInOutCubic);
//        chart.setUsePercentValues(true);
//        chart.getDescription().setEnabled(false);
//        chart.setExtraOffsets(5,10,5,5);
//        chart.setDragDecelerationFrictionCoef(0.95f);
//        chart.setDrawHoleEnabled(false);
//        chart.setHoleColor(Color.WHITE);
//        chart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> value = new ArrayList<PieEntry>();
        Log.e("list size : ", String.valueOf(list.size()));
        for(int i = 0; i < list.size(); i++){
            float val = 0F;
            int count = list.get(i).getCount();
            val = ((float)count / (float)total) * 100f;
            value.add(new PieEntry(val, list.get(i).getName()));
//            Log.e("count", String.valueOf(count));
//            Log.e("val", String.valueOf(val));
//            Log.e("total", String.valueOf(total));
        }

        PieDataSet set = new PieDataSet(value, "Name");
        set.setSliceSpace(3f);
        set.setSelectionShift(5f);
        set.setColors(ColorTemplate.JOYFUL_COLORS);


        PieData data = new PieData(set);
        data.setValueTextSize(15f);  // 차트 String 크기

        chart.setData(data);
        chart.invalidate();

        return fragmentView;
    }
    public void getFiles() {
        try {
            String result = new ParticipationFragment.getTask().execute(strTeamNum).get();
            Log.e("string result", result);
            JSONObject resultJsonObj = new JSONObject(result);
            JSONArray resultJsonData = resultJsonObj.getJSONArray("files");
            JSONObject jsonObj;
            for(int i=0 ; i<resultJsonData.length() ; i++) {
                jsonObj = resultJsonData.getJSONObject(i);
                ParticipationDto participationDto = new ParticipationDto();
                participationDto.setId(jsonObj.getString("student_id"));
                participationDto.setName(jsonObj.getString("student_name"));
                participationDto.setCount(jsonObj.getInt("count"));
                list.add(participationDto);    //리스트뷰에 한개씩 추가
            }

        }catch (Exception e) {}
    }
    class getTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlValue = "";

                urlValue = "http://teamhub.cafe24.com/professor_get_files.jsp";

                URL url = new URL(urlValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "makeTeam_num=" + strings[0];
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
                    Log.e("Participation", receiveMsg);

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
