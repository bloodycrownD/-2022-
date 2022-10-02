package untils.setMsgHeader;

/**
 * 设置响应报文响应头
 */
@FunctionalInterface
public interface SetResponseHeaders {
    /**
     * 设置头信息，设置什么头留给子类实现
     *
     * @param msg 待设置报文
     * @return 设置完成报文
     */
    String setHeader(String msg);

    /**
     * 设置一系列的报文头
     * @param msg 待设置的报文
     * @param args 处理的头
     * @return 处理好的报文
     */
    static String setHeaders(String msg,SetResponseHeaders ...args) {
        for (SetResponseHeaders s:args) {
            msg = s.setHeader(msg);
        }
        return msg;
    }
}
