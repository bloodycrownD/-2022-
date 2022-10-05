package untils;

import Client.Client;
import Client.impl.clientImpl;
import filer.Filter;
import filer.fishingFilter.impl.SimpleFishingFilter;
import filer.userFiler.impl.SimpleUserFilter;
import filer.webFilter.impl.SimpleWebFilter;
import untils.constant.ReqMethod;
import untils.getMsgHeader.GetHeaders;
import untils.getMsgHeader.impl.GetLastModified;
import untils.setMsgHeader.SetResponseHeaders;
import untils.setMsgHeader.impl.setModified;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 处理请求的工具类
 */
public class ResolveReq {
    private final int MAX_SIZE = 65507;
    public ResolveReq() {
    }


    /**
     * 处理请求
     * @param socket 监听套接字
     */
    public void resolveRequest(Socket socket) throws IOException {
        //获取报文的字节
        byte[] msgBytes = new byte[MAX_SIZE];
        //报文长度
        int msgLen;
        //完整报文
        String msg;
        InputStream stream = socket.getInputStream();
        //获取报文
        msgLen = stream.read(msgBytes,0,MAX_SIZE);
        msg = new String(msgBytes,0,msgLen);
        //处理报文，并进行回应
        resolveMsg(msg,socket);
        stream.close();
        socket.close();
    }

    /**
     * 处理报文，并进行回应
     * @param msg 报文
     */
    private void resolveMsg(String msg,Socket socket) throws IOException {
        if (msg.contains(ReqMethod.CONNECT.toString())) {
            System.err.println("无法处理HTTPS协议");
        }
        if (msg.contains(ReqMethod.GET.toString())) {
            doGet(msg,socket);
        }
        if (msg.contains(ReqMethod.POST.toString())) {
            doPost(msg,socket);
        }
    }


    /**
     *
     * @return 获取GMT时间戳
     */
    private String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Calendar calendar = Calendar.getInstance();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        return sdf.format(calendar.getTime());
    }

    /**
     * 处理get请求
     * @param msg 请求报文
     * @param socket 监听接口
     */
    private void doGet(String msg,Socket socket) throws IOException {
        String[] lines = msg.split("\r\n");
        String uri = lines[0].split(" ")[1];
        String host = lines[1].replace("Host:","");
        //过滤
        if (Filter.multiFunctionalFiler(uri,
                new SimpleWebFilter(socket),
                new SimpleFishingFilter(socket),
                new SimpleUserFilter(socket))
        ) return;
        //记录LastModified
        recordLastModified(msg,uri);
        //判断是否有cache且未过期
        if (isCached(uri) && !isModified(msg, host)){
          doResponse(socket,loadFromCache(uri));
//            System.out.println("-------------------------------");
          return;
        }
        String finalMsg = doRequest(msg, host);
        //写缓存
        saveCache(finalMsg,uri);
        //响应客户端
        doResponse(socket,finalMsg);
    }

    /**
     * 模拟客户端向服务器请求
     *
     * @param msg  请求报文
     * @param host 服务器地址
     * @return 响应报文
     */
    private String doRequest(String msg, String host) {
        Client client;
        String temp;
        if (host.contains(":")) {
            String[] temps = host.split(":");
            int port = Integer.parseInt(temps[1]);
            client = new clientImpl(port,temps[0], msg);
            temp = client.clientStart();
        }
        else {
            client = new clientImpl(host, msg);
            temp = client.clientStart();
//            System.out.println(temp);
            //处理最终的响应报文
//            temp = SetResponseHeaders.setHeaders(temp,new setConnection());
        }
        client.close();
        return temp;
    }

    /**
     * 处理post请求
     * @param msg 请求报文
     * @param socket 监听接口
     */
    private void doPost(String msg,Socket socket) {
//        System.out.println("TODO!!!!!!!!");
        try {
            doGet(msg,socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向被代理的客户端发送报文
     * @param socket 与客户端连接的套接字
     * @param msg 响应报文
     */
    private void doResponse(Socket socket,String msg){
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(msg.getBytes());
//            System.out.println(msg.length());
//            System.out.println(msg);
            socket.shutdownOutput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断该网站是否被缓存
     * @param uri 网站uri
     * @return 是true,否false
     */
    private boolean isCached(String uri) throws IOException {
        boolean flag = false;
        Properties prop = new Properties();
        prop.load(new FileReader("src/main/resources/cache/conf/cacheList.properties"));
        if (prop.get(uri)!=null){
            flag = true;
        }
        return flag;
    }

    /**
     * 该文件是否被修改
     *
     * @param msg  请求报文
     * @param host 服务器地址
     * @return 更改了返回ture, 否则返回false
     */
    private boolean isModified(String msg, String host){
        String time = getTime();
        Properties prop = new Properties();
        try {
            prop.load(new FileReader("src/main/resources/cache/conf/Last-Modified.properties"));
            if (prop.get(host)!=null) {
                time = prop.get(host).toString();
            }
            msg = SetResponseHeaders.setHeaders(msg,new setModified(time));
            String respMsg = doRequest(msg, host);
            boolean has = true;
            String[] respMsgLines = respMsg.split("\r\n");
            if (respMsgLines[0].contains("304")) {
                has = false;
            }
            return has;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从缓存中读
     * @param uri uri
     * @return 返回读出的内容
     */
    private String loadFromCache(String uri){

        try {
            Properties prop = new Properties();
            prop.load(new FileReader("src/main/resources/cache/conf/cacheList.properties"));
            String fileName = (String) prop.get(uri);
            InputStream inputStream =new FileInputStream("src/main/resources/cache/content/" + fileName + ".cache");
            byte [] msg = new byte[65524];
            int len = inputStream.read(msg,0,65524);
            return new String(msg,0,len);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 缓存网页
     * @param msg 响应报文
     * @param uri uri
     */
    private void saveCache(String msg,String uri) {
        Properties prop = new Properties();
        try {
            String fileName = String.valueOf(new Date().getTime());
            prop.load(new FileReader("src/main/resources/cache/conf/cacheList.properties"));
            prop.setProperty(uri,fileName);
            prop.store(new FileWriter("src/main/resources/cache/conf/cacheList.properties"),"cache");
            OutputStream outputStream = new FileOutputStream("src/main/resources/cache/content/" + fileName + ".cache");
            outputStream.write(msg.getBytes());
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 记录Last-Modified:，有就写dao配置文件，没有就不写
     */
    private void recordLastModified(String msg,String uri) {
        GetHeaders getHeaders = new GetLastModified();
        String header = getHeaders.getHeader(msg);
        if (!header.isEmpty()) {
            Properties prop = new Properties();
            try {
                prop.load(new FileReader("src/main/resources/cache/conf/Last-Modified.properties"));
                prop.setProperty(uri,header);
                prop.store(new FileWriter("src/main/resources/cache/conf/Last-Modified.properties"),"LastModified");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
