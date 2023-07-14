package com.example.blog_system_ssm.common;

import lombok.Data;

/**
 * 统一返回格式类
 *
 * @Date 2023/5/18 9:09
 */
@Data
public class AjaxResult {
    // 状态码
    private Integer code;
    // 状态码描述信息
    private String msg;
    // 返回数据类型
    private Object data;

    /**
     * 操作成功返回的结果
     *
     * @return
     */
    public static AjaxResult success(Object data) {
        AjaxResult result = new AjaxResult();
        result.setCode(200);
        result.setMsg("");
        result.setData(data);
        return result;
    }

    public static AjaxResult success(int code, Object data) {
        AjaxResult result = new AjaxResult();
        result.setCode(code);
        result.setMsg("");
        result.setData(data);
        return result;
    }

    public static AjaxResult success(int code, String msg, Object data) {
        AjaxResult result = new AjaxResult();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    //... 可以重载很多方法...
    public static AjaxResult fail(int code, String msg) {
        AjaxResult result = new AjaxResult();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}
