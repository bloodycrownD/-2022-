import HTTPserver.impl.ServerImpl;

import java.io.*;
import java.net.Socket;

public class proxy {
    public static void main(String[] args) throws IOException {

        ServerImpl serverImpl = new ServerImpl(7890);
        serverImpl.serverStart();
        //ss.close(); 服务器流 通常都是不关闭的
    }

}
