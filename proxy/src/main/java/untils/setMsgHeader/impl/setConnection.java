package untils.setMsgHeader.impl;

import untils.setMsgHeader.SetResponseHeaders;

import java.util.Arrays;
import java.util.List;

public class setConnection implements SetResponseHeaders {

    /**
     * 设置Connection头
     *
     * @param msg 设置前的报文
     * @return 设置好的报文
     */
    public String setHeader(String msg) {
        List<String> msgLine = Arrays.asList(msg.split("\r\n"));
        int len = msgLine.size();
        boolean has = false;
        for(int i = 0;i < len;i++ ) {
            if (msgLine.get(i).contains("Connection")) {
                msgLine.set(i,"Connection: close");
                has = true;
            }
        }
        if (!has) {
            msgLine.add(2,"Connection: close");
        }
        return String.join("\r\n",msgLine) + "\r\n\r\n\r";
    }
}
