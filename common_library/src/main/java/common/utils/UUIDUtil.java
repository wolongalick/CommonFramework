package common.utils;

import java.util.UUID;

/**
 * Created by Admin on 2015/10/22.
 */
public class UUIDUtil {
    public static String getUUid(){
        UUID uuid = UUID.randomUUID();
        return  uuid.toString();
    }
}
