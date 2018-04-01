package com.hand.promotion.pojo;


import com.hand.promotion.pojo.enums.MsgMenu;

/**
 * @author zhuweifeng
 * @Title:
 * @Description: 方法返回信息汇总pojo
 * @date 2017年03月29日 12:13
 */
public class SimpleMessagePojo {

    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 异常信息
     */
    private MsgMenu message;

    private String checkMsg;
    /**
     * 目标返回对象
     */
    private Object obj;

    public SimpleMessagePojo(boolean success, MsgMenu message, Object obj) {
        this.success = success;
        this.message = message;
        if (null != message) {
            this.checkMsg = message.getMsg();
        }
        this.obj = obj;
    }

    /**
     * 初始化，默认成功
     */
    public SimpleMessagePojo() {
        this.success = true;
        this.message = MsgMenu.SUCCESS;
        this.checkMsg = MsgMenu.SUCCESS.getMsg();
        this.obj = null;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public MsgMenu getMessage() {
        return message;
    }

    public void setMessage(MsgMenu message) {
        this.message = message;
        checkMsg = message.getMsg();
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getCheckMsg() {
        return checkMsg;
    }

    public SimpleMessagePojo setCheckMsg(String msg) {
        this.checkMsg = msg;
        return this;
    }

    public void setFalse(MsgMenu message) {
        this.success = false;
        this.message = message;
        if(null!=message){
            checkMsg = message.getMsg();

        }
    }

    public void setFalse(String message) {
        this.success = false;
        this.checkMsg = message;
    }


}
