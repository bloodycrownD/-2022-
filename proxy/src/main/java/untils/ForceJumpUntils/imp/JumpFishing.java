package untils.ForceJumpUntils.imp;

import untils.ForceJumpUntils.ForceJump;

import java.io.*;
import java.net.Socket;

public class JumpFishing implements ForceJump {
    /**
     * 跳转至钓鱼网站
     * @param socket 客户端套接字
     */
    @Override
    public void forceJump(Socket socket) {
        try {
            FileInputStream inputStream = new FileInputStream("src/main/resources/filterConf/fishingFilter/webPage/fishing.html");
            OutputStream outputStream = socket.getOutputStream();
            String responseLine = "HTTP/1.1 " + "200 OK" + "\r\n";
            byte[]content = new byte[65524];
            int len = inputStream.read(content,0,65524);
            String body = new String(content,0,len);
            String responseHeader = "Connection:close" + "\r\n" +
                    //这个长度我好像算的不准...
//                    "Content-Length:" + body.length() + "\r\n" +
                    "Content-Type:" + "text/html" + "\r\n" +
                    "\r\n\r";
            String response = responseLine + responseHeader + body;
            outputStream.write(response.getBytes());
            inputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
