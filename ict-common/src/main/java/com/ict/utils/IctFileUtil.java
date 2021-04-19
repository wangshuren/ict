package com.ict.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/19
 * @Version 1.0
 */
public class IctFileUtil {
    /*文件上传下载状态 成功*/
    public static final int FILE_UPLOAD_DOWNLOAD_STATUS_SUCCESS = 0;
    /*文件上传下载状态 失败*/
    public static final int FILE_UPLOAD_DOWNLOAD_STATUS_ERROR = 1;

    /*客户端发起请求服务端返回的状态描述*/
    public static final String SERVICE_RESPONSE_STATUS_MSG = "0：成功，1：客户端时间大于有效时长，2：客户端时间小于有效时长，3：token验证失败，4: 请求数据格式错误，-1：服务器异常";

    /*token验证异常*/
    public static final int CHECK_TOKEN_STATUS_ERROR = -1;
    /*token验证成功*/
    public static final int CHECK_TOKEN_STATUS_SUCCESS = 0;
    /*客户端时间 大于 服务器时间  CT : client time  ST : server time  LE : 小于*/
    public static final int CHECK_TOKEN_STATUS_CT_LE_ST = 1;
    /*客户端时间 小于 服务器时间  CT : client time  ST : server time  GE : 大于*/
    public static final int CHECK_TOKEN_STATUS_CT_GE_ST = 2;
    /*客户端 token 与服务端 token 不一致*/
    public static final int CHECK_TOKEN_STATUS_DIFFERENT = 3;
    /*客户端 客户端请求数据格式错误 */
    public static final int CHECK_TOKEN_STATUS_REQUEST_ERROR = 4;


    /**
     * 日期格式化yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String getDateFormat2(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

    //拆分 list 每组 num条数据
    public static <T> List<List<T>> groupList(List<T> list, Integer num) {
        List<List<T>> listGroup = new ArrayList<>();
        int listSize = list.size();
        //子集合的长度，比如 500T
        int toIndex = num;
        for (int i = 0; i < list.size(); i += num) {
            if (i + num > listSize) {
                toIndex = listSize - i;
            }
            List<T> newList = list.subList(i, i + toIndex);
            listGroup.add(newList);
        }
        return listGroup;
    }
}