package com.example.csdnblog4.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 长链接:问题描述
 * Q1:中服务端与客户端socket的输入输出流都不会关闭,所以需要考虑资源得释放长链接时机
 *
 * Q2何时关闭链接,客户端和服务器端,不能关闭任一socket的输入流或者输出流,否则socket通信会关闭;
 *  由于都不关闭连接,而read方法又是阻塞的,会一直读取数据,不知道何时读取结束(何时知道本次数据读取结束);
 * *
 * Q1解决方案:服务器端都是会设定超时时间的，也就是timeout，如果超过timeout服务器没有接收到任何数据，
 * 那么该服务器就会关闭该连接，从而使得服务器资源得到有效地使用。
 *
 * Q2解决方案:约定通信协议,如特定字符,或者使用包头+包体的方式,传递数据,包头固定长度,
 * 里面保存包体长度等信息,这样服务端就知道读取到何时结束了.(本文使用此种方式)以下是代码:
 * @Link(http://blog.csdn.net/ljl157011/article/details/19291611)
 * 短链接:建立完一次通信后将被释放,下次发送得重新链接建立,浪费资源

 * Created by orange on 16/6/8.
 */
public class TCPLongConnectClient {
    ExecutorService executorService= Executors.newCachedThreadPool();
    public TCPLongConnectClient(){
        executorService.execute(new RequestTask());
    }

}
