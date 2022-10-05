package untils.getMsgHeader.impl;

import untils.getMsgHeader.GetHeaders;

import java.util.Arrays;

public class GetLastModified implements GetHeaders {
    /**
     *
     * @param msg 报文
     * @return 获取Last-Modified:报文信息
     */
    @Override
    public String getHeader(String msg) {
        String info = "";
        String[] msgLines = msg.split("\r\n");
        Object[] array = Arrays.stream(msgLines).filter(s -> s.contains("Last-Modified:")).toArray();
        if (array.length != 0) {
            info = array[0].toString().replace("Last-Modified:","").trim();
        }
        return info;
    }
}
