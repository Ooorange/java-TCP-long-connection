package com.example.csdnblog4.net;

import com.example.csdnblog4.net.protocol.ChatMsgProcotol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 长链接:问题描述
 * Q1:中服务端与客户端socket的输入输出流都不会关闭,所以需要考虑资源得释放长链接时机
 *
 * Q2何时关闭链接,客户端和服务器端,不能关闭任一socket的输入流或者输出流,否则socket通信会关闭;
 *  由于都不关闭连接,而read方法又是阻塞的,会一直读取数据,不知道何时读取结束(何时知道本次数据读取结束);
 * *
 *
 * Q3如果有多个业务需要使用链接,如何兼容其他的业务,使得解析流程一致
 *
 *
 * Q1解决方案:服务器端都是会设定超时时间的，也就是timeout，如果超过timeout服务器没有接收到任何数据，
 * 那么该服务器就会关闭该连接，从而使得服务器资源得到有效地使用。
 *
 * Q2解决方案:约定通信协议,如特定字符,或者使用包头+包体的方式,传递数据,包头固定长度,
 * 里面保存包体长度等信息,这样服务端就知道读取到何时结束了.(本文使用此种方式)以下是代码:
 *
 * Q3解决方案:不同业务定义不同的协议,比如心跳协议,业务协议;  另外一种方案就是实用json数据格式进行传输
 * @Link(http://blog.csdn.net/ljl157011/article/details/19291611)
 * 短链接:建立完一次通信后将被释放,下次发送得重新链接建立,浪费资源

 * Created by orange on 16/6/8.
 */
public class TCPLongConnectClient {
    ExecutorService executorService= Executors.newCachedThreadPool();
    RequestTask requestTask;
    public TCPLongConnectClient(TCPRequestCallBack tcpRequestCallBack){
        requestTask=new RequestTask(tcpRequestCallBack);
        executorService.execute(requestTask);
    }

    public void addNewRequest(ChatMsgProcotol data){
        if (requestTask!=null)
        requestTask.addRequest(data);
    }

    public void closeConnect() {
        requestTask.stop();
    }
}
