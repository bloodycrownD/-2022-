package filer;

/**
 * 过滤器祖先接口
 */
public interface Filter {
    /**
     * 判断是否被过滤
     * @param uri 要被判定是否被过滤的uri
     * @return 要被过滤则返回true，否则返回false
     */
    boolean isFiltered(String uri);

    /**
     * 多功能过滤器，传几个功能的过滤器，就安装几个过滤器
     * 一旦被过滤，则直接结束过滤
     * @param uri 待过滤的uri
     * @param filters 要被安装的过滤器
     * @return 如果uri在过滤范围内则过滤，返回true，否则返回false
     */
    static boolean multiFunctionalFiler(String uri,Filter ...filters) {
        boolean flag = false;
        for (Filter f:filters) {
            if (f.isFiltered(uri)){
                flag = true;
                break;
            }
        }
        return flag;
    }
}
