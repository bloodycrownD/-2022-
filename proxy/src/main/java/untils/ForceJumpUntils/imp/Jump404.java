package untils.ForceJumpUntils.imp;

import untils.ForceJumpUntils.ForceJump;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Jump404 implements ForceJump {
    /**
     * 跳转到404
     * @param socket 客户端套接字
     */
    @Override
    public void forceJump(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            String responseLine = "HTTP/1.1 " + "404 NOTFOUND" + "\n";
            String body = "<h1>404 NOT FOUND</h1>";
            StringBuilder responseHeader = new StringBuilder().append("Connection:close").append("\n")
                    .append("Content-Length:").append(body.length()).append('\n')
                    .append("Content-Type:").append("text/html").append('\n')
                    .append('\n');
            String response = responseLine + responseHeader + body;
            outputStream.write(response.getBytes());
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
