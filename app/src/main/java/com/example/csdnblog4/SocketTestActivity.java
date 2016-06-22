package com.example.csdnblog4;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.csdnblog4.Adapter.ChatAdapter;
import com.example.csdnblog4.Entity.ChatContent;
import com.example.csdnblog4.net.BasicProtocol;
import com.example.csdnblog4.net.ChatMsgProcotol;
import com.example.csdnblog4.net.SocketClient;
import com.example.csdnblog4.net.TCPLongConnectClient;
import com.example.csdnblog4.net.TCPRequestCallBack;
import com.example.csdnblog4.net.UDPDataInteractor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by orange on 16/6/1.
 */
public class SocketTestActivity extends BaseActivity implements
        SocketClient.SuccessCallBack,UDPDataInteractor.SuccessCallBack,
                 TCPRequestCallBack {


    @BindView(R.id.ib_send)
    ImageButton ib_send;
    @BindView(R.id.et_messageData)
    EditText et_messageData;
    
    @BindView(R.id.rg_radioGroup)
    RadioGroup rg_radioGroup;

    @BindView(R.id.rv_recycleviewAdapter)
    RecyclerView recyclerView;

    private SocketClient socketClient;
    private UDPDataInteractor udpDataInteractor;
    private TCPLongConnectClient tcpLongConnect;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ChatContent> chatContentList=new ArrayList<ChatContent>();

    public static final String[] items={"TCP短链接","UDP短链接","TCP长链接"};


    @Override
    int initContentView() {
        return R.layout.activity_sockettest;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addRadioGroup(items);
        initRecycleView();
        setTitle("聊天中");
    }


    public void initRecycleView(){
        layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addMockData(6);
        chatAdapter=new ChatAdapter(this,chatContentList);
        recyclerView.setAdapter(chatAdapter);
    }

    /**
     * MYSELF=0;
        GUEST=1;
     * @param length
     */
    private void addMockData(int length){
        for (int i=0;i<length;i++){
            boolean type=(i%2==0);
            ChatContent chatContent=new ChatContent(type?ChatContent.MYSELF:ChatContent.GUEST,type?"我是主人":"我是客人");
            chatContentList.add(chatContent);
        }
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
        addListener();

    }

    public void addListener(){
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
                    case 3:
                        tcpLongConnect=new TCPLongConnectClient(SocketTestActivity.this);
                        break;
                }
            }
        });

        ib_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=et_messageData.getText().toString().trim();
                if (tcpLongConnect==null){
                    return;
                }
                ChatMsgProcotol protocol=new ChatMsgProcotol();
                protocol.setMessage(msg);
                tcpLongConnect.addNewRequest(protocol);

                chatAdapter.addMessage(new ChatContent(ChatContent.MYSELF, msg));
                recyclerView.scrollToPosition(chatAdapter.getAdapterSize() - 1);
                et_messageData.setText("");
            }
        });
    }
    @Override
    public void reciveDataSuccess(String message) {
        //todo
    }

    @Override
    public void reciveUDPDataSuccess(String message) {
        //todo
    }

    @Override
    public void onSuccess(BasicProtocol responProcotol) {
        setTitle("连接成功");
//        Log.d("orangeRe",msg);
        if (responProcotol==null){
            return;
        }
        chatAdapter.addMessage(new ChatContent(ChatContent.GUEST, ((ChatMsgProcotol)responProcotol).getMessage()));
        recyclerView.scrollToPosition(chatAdapter.getAdapterSize()-1);
    }

    @Override
    public void onFailed(int errorCode, String msg) {
        if (errorCode==0)
        setTitle(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tcpLongConnect!=null)
        tcpLongConnect.closeConnect();
    }
}
