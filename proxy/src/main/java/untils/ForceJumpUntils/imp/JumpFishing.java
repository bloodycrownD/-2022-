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
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/filterConf/fishingFilter/webPage/fishing.html"));
            OutputStream outputStream = socket.getOutputStream();
            String responseLine = "HTTP/1.1 " + "200 OK" + "\r\n";
            StringBuilder body = new StringBuilder();
            String temp;
            while ((temp = reader.readLine())!= null) {
                body.append(temp);
            }
            String responseHeader = "Connection:close" + "\r\n" +
                    "Content-Length:" + body.length() + "\r\n" +
                    "Content-Type:" + "text/html" + "\r\n" +
                    "\r\n\r";
            String response = responseLine + responseHeader + body;
            outputStream.write(response.getBytes());
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
