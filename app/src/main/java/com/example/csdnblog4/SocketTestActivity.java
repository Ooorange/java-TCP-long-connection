package com.example.csdnblog4;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.csdnblog4.net.SocketClient;
import com.example.csdnblog4.net.UDPDataInteractor;

import butterknife.BindView;

/**
 * Created by orange on 16/6/1.
 */
public class SocketTestActivity extends BaseActivity implements SocketClient.SuccessCallBack, UDPDataInteractor.SuccessCallBack {

    @BindView(R.id.tv_revicerData)
    TextView tv_revicerData;
    @BindView(R.id.tv_sendData)
    TextView tv_sendData;
    @BindView(R.id.rg_radioGroup)
    RadioGroup rg_radioGroup;

    SocketClient socketClient;
    UDPDataInteractor udpDataInteractor;
    public static final String[] items={"TCP传输","UDP传输"};
    @Override
    int initContentView() {
        return R.layout.activity_main;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addRadioGroup(items);
        socketClient = new SocketClient(SocketTestActivity.this);
        rg_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case 1:
                        socketClient = new SocketClient(SocketTestActivity.this);
                        break;
                    case 2:
                        udpDataInteractor = new UDPDataInteractor(SocketTestActivity.this);
                        break;
                }
            }
        });
    }

    public void addRadioGroup(String[] itemName) {
        rg_radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        for (int i=0,size=itemName.length;i<size;i++) {
            RadioButton radioButton=new RadioButton(this);
            radioButton.setText(itemName[i]);
            rg_radioGroup.addView(radioButton);
            if (i==0){
                rg_radioGroup.check(radioButton.getId());
            }
        }
    }

    @Override
    public void reciveDataSuccess(String message) {
        tv_revicerData.setText(socketClient.reciverData);
    }

    @Override
    public void reciveUDPDataSuccess(String message) {
        Log.d("orangeRE",message);
        tv_revicerData.setText(message);
    }
}
