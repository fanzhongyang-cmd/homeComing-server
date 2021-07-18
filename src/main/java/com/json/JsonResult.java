package com.json;


public class JsonResult<T> {
    private T data;
    private int code;
    private String msg;

    /**
     * 若没有数据返回，默认状态码为 0，提示信息为“操作成功！”
     */
    public JsonResult() {
        this.code = 0;
        this.msg = "操作成功！";
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     *
     * @param code
     * @param msg
     */
    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     *
     * @param data
     */
    public JsonResult(T data) {
        this.data = data;
        this.code = 0;
        this.msg = "操作成功！";
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     *
     * @param data
     * @param msg
     */
//    public JsonResult(T data, String msg) {
//        this.data = data;
//        this.code = "0";
//        this.msg = msg;
//    }
    public JsonResult(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    //    // 省略 get 和 set 方法
//————————————————
//    版权声明：本文为CSDN博主「taojin12」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/taojin12/article/details/88244907
}
