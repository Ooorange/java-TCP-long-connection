package com.orange.blog;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.orange.blog.Adapter.ChatAdapter;
import com.orange.blog.Entity.ChatContent;
import com.orange.blog.View.AlertVIew.AlertView;
import com.orange.blog.View.AlertVIew.OnItemClickListener;
import com.orange.blog.common.JsonUtil;
import com.orange.blog.database.bean.UserFriends;
import com.orange.blog.net.SocketClient;
import com.orange.blog.net.TCPLongConnectClient;
import com.orange.blog.net.TCPRequestCallBack;
import com.orange.blog.net.UDPDataInteractor;
import com.orange.blog.net.protocol.BasicProtocol;
import com.orange.blog.net.protocol.ChatMsgProcotol;
import com.orange.blog.net.protocol.UserFriendReuqestProcotol;
import com.orange.blog.storage.UserFriendManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by orange on 16/6/1.
 */
public class SocketTestActivity extends BaseActivity implements
        SocketClient.SuccessCallBack,UDPDataInteractor.SuccessCallBack,
                 TCPRequestCallBack, OnItemClickListener {


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
    private AlertView alert;
    public static final String[] items={"TCP短链接","UDP短链接","TCP长链接"};
    private String [] friendsName;
    private String targetMsgUUID;
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
        setActionBarMenuIcon(R.drawable.menu);
    }

    @Override
    protected void actoinBarIconClick() {
        alert=new AlertView("好友列表",null,"取消",null,friendsName,this,AlertView.Style.ActionSheet, this);

        if (!alert.isShowing()){
            alert.show();
        }else {
            alert.dismiss();
        }
    }

    public void initRecycleView(){
        layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addMockData(4);
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
                        tcpLongConnect=null;
                        socketClient = new SocketClient(SocketTestActivity.this);
                        break;
                    case 2:
                        tcpLongConnect=null;
                        udpDataInteractor = new UDPDataInteractor(SocketTestActivity.this);
                        break;
                    case 3:
                        tcpLongConnect=null;
                        tcpLongConnect = new TCPLongConnectClient(SocketTestActivity.this);
                        break;
                }
            }
        });

        ib_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_messageData.getText().toString().trim();
                if (tcpLongConnect == null) {
                    return;
                }
                ChatMsgProcotol protocol = new ChatMsgProcotol();
                protocol.setMessage(msg);
                if (friendsName.length>0) {
                    protocol.setMsgTargetUUID(targetMsgUUID);
                    tcpLongConnect.addNewRequest(protocol);
                    chatAdapter.addMessage(new ChatContent(ChatContent.MYSELF, msg));
                } else {
                    Toast.makeText(SocketTestActivity.this, "先去加个好友吧", Toast.LENGTH_SHORT).show();
                }

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
        if (responProcotol==null){
            return;
        }
        if (responProcotol instanceof ChatMsgProcotol) {
            chatAdapter.addMessage(new ChatContent(ChatContent.GUEST, ((ChatMsgProcotol) responProcotol).getMessage()));
            recyclerView.scrollToPosition(chatAdapter.getAdapterSize() - 1);
        }else if (responProcotol instanceof UserFriendReuqestProcotol){
            String json=((UserFriendReuqestProcotol)responProcotol).getUsersJson();
            Log.d("orangeJson",json);
            List<UserFriends> usess=JsonUtil.fromJsonArr(json, UserFriends.class);
            if (usess!=null&&usess.size()>0){
                targetMsgUUID=usess.get(0).getFriendUUID();
                friendsName=new String[usess.size()];
                for (int i=0,size=usess.size();i<size;i++){
                    friendsName[i]=usess.get(i).getFriendNickName();
                }
                UserFriendManager.getInstance().insertUserFriends(usess);
            }
        }
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

    @Override
    public void onItemClick(Object o, int position) {
        if (position!=-1)
        targetMsgUUID=friendsName[position];
    }
}
