package untils.ForceJumpUntils;

import java.net.Socket;

public interface ForceJump {
    /**
     * 强制跳转到某个页
     * @param socket 客户端套接字
     */
    void forceJump(Socket socket);
}
