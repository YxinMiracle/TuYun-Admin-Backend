package com.yxinmiracle.utils.common;
import org.apache.commons.lang3.StringUtils;


public class ImageUrlUtil {
    public static String repalceImageUrl(String imageUrl,String HOST){
        if (imageUrl!=null && StringUtils.isNotBlank(imageUrl) && imageUrl.startsWith(HOST)){
            return imageUrl.startsWith(HOST)?imageUrl.replace(HOST,""):null;
        } else {
            return null;
        }
    }

}
