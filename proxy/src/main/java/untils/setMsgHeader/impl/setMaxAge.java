package untils.setMsgHeader.impl;

import untils.setMsgHeader.SetResponseHeaders;

import java.util.Arrays;
import java.util.List;

public class setMaxAge implements SetResponseHeaders {
    /**
     * 设置Cache-Control: max-age > 0
     *
     * @param msg 待设置报文
     * @return 返回处理后的报文
     */
    @Override
    public String setHeader(String msg) {
        List<String> msgLine = Arrays.asList(msg.split("\r\n"));
        int len = msgLine.size();
        boolean has = false;
        for(int i = 0;i < len;i++ ) {
            if (msgLine.get(i).contains("Cache-Control")) {
                msgLine.set(i,"Cache-Control: max-age=100");
                has = true;
            }
        }
        if (!has) {
            msgLine.add(2,"Cache-Control: max-age=100");
        }
        System.out.println(String.join("\r\n",msgLine));
        return String.join("\r\n",msgLine);
    }

}
