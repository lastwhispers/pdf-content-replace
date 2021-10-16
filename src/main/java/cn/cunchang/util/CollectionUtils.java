package cn.cunchang.util;


import java.util.Collection;
import java.util.Map;

/**
 * @author cunchang
 * @date 2020/9/6 下午12:11
 */
public class CollectionUtils {
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
