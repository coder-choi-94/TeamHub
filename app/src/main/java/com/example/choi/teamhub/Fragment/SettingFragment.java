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
    public SettingFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting_fragment, container, false);

        final String userId = getArguments().getString("userId");
        final String userName = getArguments().getString("userName");
        final String userPhone = getArguments().getString("userPhone");
        final String userDept = getArguments().getString("userDept");
        final String userSno = getArguments().getString("userSno");
        final int projectNum = getArguments().getInt("프로젝트 번호");
        final int teamNum = getArguments().getInt("팀 번호");

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
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    intent.putExtra("userDept", userDept);
                    intent.putExtra("userSno", userSno);
                    intent.putExtra("프로젝트 번호", projectNum);
                    intent.putExtra("팀 번호", teamNum);
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
        return settingItem;

    }
}