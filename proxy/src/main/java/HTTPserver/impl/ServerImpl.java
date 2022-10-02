package HTTPserver.impl;

import HTTPserver.Server;
import untils.ResolveReq;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements Server {
    private final ServerSocket serverSocket;
    private ExecutorService pool ;
    private final int port;
    public ServerImpl() {
        this.port = 1650;
        try {
            this.serverSocket = new ServerSocket(this.port);
            //Runtime.getRuntime().availableProcessors() * 2 = 这是CPU核数*2
            this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 指定代理服务器端口号
     * @param port 端口号
     */
    public ServerImpl(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return 返回监听套接字
     */
    private Socket accept(){
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void serverStart()  {
        System.out.println("开始进行代理！");
        System.out.println("监听接口" + this.port);
        while (true) {
//            System.err.println("--------------------------------------------------------------");
            pool.submit(new reqThread(this.accept(),new ResolveReq()));
        }
    }
}

class reqThread implements Runnable{
    private final Socket socket;
    private final ResolveReq resolveReq;

    public reqThread(Socket socket, ResolveReq resolveReq) {
        this.socket = socket;
        this.resolveReq = resolveReq;
    }

    @Override
    public void run() {
        try {
            resolveReq.resolveRequest(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
