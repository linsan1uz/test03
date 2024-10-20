package com.hzw.test03;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
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
public class Fragment1 extends Fragment implements View.OnClickListener {
    private Button btn_1;
    private Button btn_2;
    private Intent broadIntent;
    public Fragment1() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        broadIntent = new Intent(getActivity(), UDPBroadCastService.class);
        Intent receiveIntent = new Intent(getActivity(), UDPReceiver.class);
        btn_1 = view.findViewById(R.id.btn_turn_1);
        btn_2 = view.findViewById(R.id.btn_turn_2);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        getActivity().startService(receiveIntent);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_turn_1:
                       broadIntent.putExtra("value", "1");
                       getActivity().startService(broadIntent);
                break;
            case R.id.btn_turn_2:
                        broadIntent.putExtra("value", "2");
                        getActivity().startService(broadIntent);
                break;
            default:
                break;
        }
    }
}