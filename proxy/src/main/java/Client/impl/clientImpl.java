package Client.impl;

import Client.Client;
import HTTPserver.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端实现类
 */
public class clientImpl implements Client {
    private final int serverPort;
    private final String serverHost;
    private final String reaClientMsg;
    private final Socket realServer;
    private final int MAX_SIZE = 65507;

    public clientImpl(int serverPort, String serverHost, String reaClientMsg) {
        this.serverPort = serverPort;
        this.serverHost = serverHost;
        this.reaClientMsg = reaClientMsg;
        try {
            this.realServer = new Socket(this.serverHost,this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public clientImpl(String serverHost, String reaClientMsg) {
        this.serverHost = serverHost.trim();
        this.reaClientMsg = reaClientMsg;
        this.serverPort = 80;
        try {
            this.realServer = new Socket(this.serverHost,this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String clientStart() {
        req(this.reaClientMsg);
        return getResponse();
    }

    /**
     * 向真正的服务器请求
     * @param msg 处理后的报文
     */
    private void req(String msg){
        try {
            OutputStream outputStream = this.realServer.getOutputStream();
            outputStream.write(msg.getBytes());
            this.realServer.shutdownOutput();
//            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取回应
     * @return 响应报文
     */
    private String getResponse() {
        try {
            byte[] temp = new byte[MAX_SIZE];
            InputStream inputStream = this.realServer.getInputStream();
            int len = inputStream.read(temp,0,MAX_SIZE);
//            System.out.println(len);
            return new String(temp,0,len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            this.realServer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
