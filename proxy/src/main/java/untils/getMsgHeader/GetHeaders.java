package untils.getMsgHeader;

public interface GetHeaders {
    /**
     * 获取报文的头信息
     * @param msg 报文
     * @return 具体的头信息，有则返回，无则返回空串
     */
    String getHeader(String msg);
}
