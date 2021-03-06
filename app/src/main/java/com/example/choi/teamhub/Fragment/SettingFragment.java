package com.example.choi.teamhub.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.choi.teamhub.DTO.ChatDto;
import com.example.choi.teamhub.PersonalData;
import com.example.choi.teamhub.R;
import com.example.choi.teamhub.SettingItem;
import com.example.choi.teamhub.StuIndexActivity;
import com.example.choi.teamhub.StuMainActivity;
import com.example.choi.teamhub.StuProjectActivity;

import java.util.List;

public class SettingFragment extends Fragment {
    ListView listView;
    private String userId;
    private String userName;
    private String userPhone;
    private String userDept;
    private String userSno;
    private String projectName;
    private String teamName;
    private int projectNum;
    private String professorNum;
    private int teamNum;
    public SettingFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting_fragment, container, false);

        final int bool = getArguments().getInt("구분");
        if (bool == 1){ // 학생일때 정보
            userId = getArguments().getString("userId");
            userName = getArguments().getString("userName");
            userPhone = getArguments().getString("userPhone");
            userDept = getArguments().getString("userDept");
            userSno = getArguments().getString("userSno");
            projectNum = getArguments().getInt("프로젝트 번호");
            teamNum = getArguments().getInt("팀 번호");
        } else if (bool == 2){ // 교수일때 정보
            projectNum = getArguments().getInt("프로젝트 번호");
            teamNum = getArguments().getInt("팀 번호");
            professorNum = getArguments().getString("code");
            userName = getArguments().getString("name");
            projectName = getArguments().getString("프로젝트 이름");
            teamName = getArguments().getString("팀 이름");
            userId = getArguments().getString("id");
            userPhone = getArguments().getString("phone");
            userDept = getArguments().getString("dept");
        }


        //final String[] ListMenu = getResources().getStringArray(R.array.setting_array);

        listView = (ListView) view.findViewById(R.id.settingList);
        SettingAdapter settingAdapter = new SettingAdapter();
        listView.setAdapter(settingAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("click ", "click :" + position);
                if(position == 0){
                    Intent intent = new Intent(getActivity(), PersonalData.class);
                    //Intent intent = new Intent(getContext(), PersonalData.class);
                    Log.e("intent : ", ""+intent);
                    if (bool == 1){ // 학생일때 정보
                        intent.putExtra("구분", 1);
                        intent.putExtra("userId", userId);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userPhone", userPhone);
                        intent.putExtra("userDept", userDept);
                        intent.putExtra("userSno", userSno);
                        intent.putExtra("프로젝트 번호", projectNum);
                        intent.putExtra("팀 번호", teamNum);
                    } else if (bool == 2){ // 교수일때 정보
                        intent.putExtra("구분", 2);
                        intent.putExtra("프로젝트 이름", projectName);
                        intent.putExtra("name", userName);
                        intent.putExtra("팀 이름", teamName);
                        intent.putExtra("code", professorNum);
                        intent.putExtra("프로젝트 번호", projectNum);
                        intent.putExtra("팀 번호", teamNum);
                        intent.putExtra("id", userId);
                        intent.putExtra("phone", userPhone);
                        intent.putExtra("dept", userDept);
                    }

                    startActivity(intent);
                } else if(position == 1){
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setType("plain/text");
                    // email setting 배열로 해놔서 복수 발송 가능
                    String[] address = {"email@address.com"};
                    email.putExtra(Intent.EXTRA_EMAIL, address);
                    email.putExtra(Intent.EXTRA_SUBJECT,"제목");
                    email.putExtra(Intent.EXTRA_TEXT,"문의사항.\n");
                    startActivity(email);

                }
//                parent.getItemAtPosition(position);

            }
        });


        return view;
    }

}
class SettingAdapter extends BaseAdapter {
    String[] arr = {"회원정보", "고객센터"};
    public int getCount(){return arr.length;}

    public Object getItem(int position){return arr[position];}

    public long getItemId(int position){return position;}

    public View getView(int position, View convertView, ViewGroup parent){
        SettingItem settingItem = new SettingItem(parent.getContext());
        settingItem.setItem(arr[position]);
        if(position == 0)
            ((ImageView)settingItem.findViewById(R.id.iconImage)).setImageResource(R.drawable.ic_person_icon);
        else
            ((ImageView)settingItem.findViewById(R.id.iconImage)).setImageResource(R.drawable.ic_menu_manage);
        return settingItem;

    }
}