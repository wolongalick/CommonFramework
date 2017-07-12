package common.utils;

import java.util.Collection;
import java.util.List;

/**
 * 数据工具类
 * Created by cxw on 2017/3/1.
 */
public class DataUtils {
    public interface FilterCallback<E>{
        /**
         * 是否需要移除
         * @param model
         * @return
         */
        boolean isNeedRemove(E model);
    }

    /**
     * 过滤集合中的数据
     * @param list              待处理的数据集合
     * @param filterCallback
     * @param <E>
     */
    public static <E> void filterList(List<E> list,FilterCallback<E> filterCallback){
        for (int i = 0; i < list.size(); i++) {
            if(filterCallback.isNeedRemove(list.get(i))){
                list.remove(i);
                i--;
            }
        }
    }

    /**
     * 判断集合数据是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection collection){
        return collection==null || collection.isEmpty();
    }

}
