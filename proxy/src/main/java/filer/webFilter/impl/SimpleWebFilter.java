package filer.webFilter.impl;

import filer.webFilter.WebFilter;
import untils.ForceJumpUntils.ForceJump;
import untils.ForceJumpUntils.imp.Jump404;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

/**
 * 网站过滤器，需要客户端的套接字
 */
public class SimpleWebFilter implements WebFilter {
    private Socket socket;

    public SimpleWebFilter(Socket socket) {
        this.socket = socket;
    }

    /**
     * 网站过滤器
     * @param uri 要被判定是否被过滤的uri
     * @return
     */
    @Override
    public boolean isFiltered(String uri) {
        Properties prop = new Properties();
        boolean flag = false;
        try {
            prop.load(new FileReader("src/main/resources/filterConf/webFilter/conf.properties"));
            Collection<Object> values = prop.values();
            for (Object o:values){
                if (uri.contains(o.toString())) {
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
