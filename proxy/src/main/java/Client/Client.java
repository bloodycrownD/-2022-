package Client;

/**
 * 客户端接口,proxy用来模拟客户端向服务器请求数据
 */
public interface Client {
    /**
     *
     * @return 从服务器获取报文
     */
    String clientStart();

    /**
     * 关闭客户端
     */
    void close();
}
