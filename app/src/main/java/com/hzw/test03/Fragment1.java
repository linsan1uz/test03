package com.hzw.test03;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Create by fqf17 on 2024/10/19 21:24
 */
public class Fragment1 extends Fragment {
    private Button btn_1;
    public Fragment1() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        btn_1 = view.findViewById(R.id.btn_turn_1);
        Intent broadIntent = new Intent(getActivity(), UDPBroadCastService.class);
        Intent receiveIntent = new Intent(getActivity(), UDPReceiver.class);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(broadIntent);
                Toast.makeText(getActivity(), "发送广播", Toast.LENGTH_SHORT).show();
            }
        });
        getActivity().startService(receiveIntent);
        return view;
    }

}