package untils.setMsgHeader.impl;

import untils.setMsgHeader.SetResponseHeaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class setModified implements SetResponseHeaders {
    private final String time;

    public setModified(String time) {
        this.time = time;
    }

    @Override
    public String setHeader(String msg) {
        /**
         * list线程不安全，导致每次add的时候直接卡死。
         */
//        List<String> msgLines = Arrays.asList(msg.split("\r\n"));
//        msgLines.add(2,"If-Modified-Since: " + time);
        //        msgLines.set(2,"If-Modified-Since: " + time);
        Vector<String> msgline = new Vector<>(Arrays.asList(msg.split("\r\n")));
        msgline.add(2,"If-Modified-Since: " + time);
        //报文在分隔的时候"\r\n\"被削掉了
        return String.join("\r\n",msgline) + "\r\n\r\n\r";
    }
}
