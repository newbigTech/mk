package com.hand.hmall.pojo;

/**
 * @author 马君
 * @version 0.1
 * @name: PosReceiveResponseData
 * @Description: 与retail对接返回的数据格式
 * @date 2017/5/26 17:34
 */
public class PosReceiveResponseData {
    private String TYPE;
    private String LOGID;
    private String MASSAGE;

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getLOGID() {
        return LOGID;
    }

    public void setLOGID(String LOGID) {
        this.LOGID = LOGID;
    }

    public String getMASSAGE() {
        return MASSAGE;
    }

    public void setMASSAGE(String MASSAGE) {
        this.MASSAGE = MASSAGE;
    }
}
