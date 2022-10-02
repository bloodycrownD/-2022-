package filer.fishingFilter.impl;

import filer.fishingFilter.FishingFilter;
import untils.ForceJumpUntils.imp.Jump404;
import untils.ForceJumpUntils.imp.JumpFishing;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class SimpleFishingFilter implements FishingFilter {
    private Socket socket;

    public SimpleFishingFilter(Socket socket) {
        this.socket = socket;
    }

    /**
     * 将用户对某个网站的访问引导至一个模拟网站（钓鱼）。
     * @param uri 要被判定是否被过滤的uri
     * @return
     */
    @Override
    public boolean isFiltered(String uri) {
        Properties prop = new Properties();
        boolean flag = false;
        try {
            prop.load(new FileReader("src/main/resources/filterConf/fishingFilter/conf.properties"));
            Collection<Object> values = prop.values();
            for (Object o:values){
                if (uri.contains(o.toString())) {
                    flag = true;
                    new JumpFishing().forceJump(socket);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return flag;
    }
}
