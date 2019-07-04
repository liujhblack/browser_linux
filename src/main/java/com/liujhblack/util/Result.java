package com.liujhblack.util;/**
 * Created by liujunhui on 2019/6/26.
 */


/**
 * @Author liujunhui
 * @Date 2019/6/26 15:37
 * @Description TODO
 */
public class Result {

    private static Integer defaultCode=200;
    private static String success="success";
    private static String fail="fail";

    private Integer code;
    private String message;
    private String data;

    public Result(Integer code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result isSuccess(){
        return new Result(defaultCode,success,"");
    }

    public static Result isSuccess(String date){
        return new Result(defaultCode,success,date);
    }

    public static Result isSuccess(String message, String date){
        return new Result(defaultCode,message,date);
    }

    public static Result isError(String message){
        return new Result(740,message,"");
    }

    public static Result getResult(Integer code, String message, String date){
        return new Result(code,message,date);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
