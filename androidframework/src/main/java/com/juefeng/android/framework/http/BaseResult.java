package com.juefeng.android.framework.http;

import com.juefeng.android.framework.common.base.BaseEntity;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/25
 * Time: 10:11
 * Description:http responce base result
 */
public class BaseResult<T> extends BaseEntity{

    public static final int SUCCESS = 0;

    public static final int FAILED = 1;

    /**
     * code type
     * default:0 success
     * default:1 failed
     */
    private int code;

    /**
     * when failed,show message to customer
     */
    private String message;

    /**
     * when success,return responce data
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * whether success
     * @return
     */
    public boolean hasSuccess(){
        if (code==SUCCESS){
            return true;
        }else {
            return false;
        }
    }
}
