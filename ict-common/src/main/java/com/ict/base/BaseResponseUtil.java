package com.ict.base;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/26
 * @Version 1.0
 */
public class BaseResponseUtil<T> {
    /**
     * 成功响应
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(BaseStatus.SUCCESS.getCode(), BaseStatus.SUCCESS.getMessage());
    }

    /**
     * 成功响应返回数据
     * @param t
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T t) {
        return new BaseResponse<>(BaseStatus.SUCCESS.getCode(), BaseStatus.SUCCESS.getMessage(), t);
    }

    /**
     * 成功响应返回数据
     * @param t
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T t,String meg) {
        return new BaseResponse<>(BaseStatus.SUCCESS.getCode(), meg, t);
    }

    /**
     * 成功响应返回数据
     * @param status
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(BaseStatus status) {
        return new BaseResponse<T>(status.getCode(), status.getMessage());
    }

    /**
     * 成功响应返回数据
     *
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(String code, String message) {
        return new BaseResponse<>(code, message);
    }


    /**
     * 错误响应返回数据
     * @param code
     * @param message
     * @param t
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(String code, String message, T t) {
        return new BaseResponse<>(code, message, t);
    }

    /**
     * 返回自定义错误
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(String code, String message) {
        return new BaseResponse<>(code, message);
    }

    /**
     * 是否成功
     * @param response
     * @param code
     * @param message
     * @return
     */
    public static boolean isCallSuccess(BaseResponse<?> response, String code, String message) {
        // 业务成功
        if (BaseStatus.SUCCESS.getCode().equals(code)) {
            return true;
        }

        // 业务失败，透传失败message
        response.setMsg(message);
        response.setCode(code);

        return false;
    }

    /**
     * 是否成功
     * @param response
     * @return
     */
    public static boolean isCallSuccess(BaseResponse<?> response) {
        return response != null && BaseStatus.SUCCESS.getCode().equals(response.getCode());
    }

    /**
     * 是否成功
     * @param returnResp
     * @param receivedResp
     * @return
     */
    public static boolean isCallSuccess(BaseResponse<?> returnResp, BaseResponse receivedResp) {
        String code = receivedResp.getCode();
        String msg = receivedResp.getMsg();
        return isCallSuccess(returnResp, code, msg);
    }
    /**
     * 自定义返回数据
     * @param status
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(BaseStatus status) {
        return new BaseResponse<T>(status.getCode(), status.getMessage());
    }
}