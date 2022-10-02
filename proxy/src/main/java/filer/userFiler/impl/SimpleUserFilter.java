package filer.userFiler.impl;

import filer.Filter;
import filer.userFiler.UserFilter;
import untils.ForceJumpUntils.imp.Jump404;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class SimpleUserFilter implements UserFilter {
    private Socket socket;

    public SimpleUserFilter(Socket socket) {
        this.socket = socket;
    }

    /**
     * 用户过滤器
     * @param uri 要被判定是否被过滤的uri
     * @return
     */
    @Override
    public boolean isFiltered(String uri) {
        InetAddress inetAddress = socket.getInetAddress();
        String host = inetAddress.getHostAddress();
        Properties prop = new Properties();
        boolean flag = false;
        try {
            prop.load(new FileReader("src/main/resources/filterConf/UserFilter/conf.properties"));
            Collection<Object> values = prop.values();
            for (Object o:values){
                if (host.contains(o.toString())) {
                    flag = true;
                    new Jump404().forceJump(socket);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return flag;
    }
}
