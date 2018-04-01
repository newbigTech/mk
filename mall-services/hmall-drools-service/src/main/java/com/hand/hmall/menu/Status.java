package com.hand.hmall.menu;

/**
 * Created by shanks on 2017/3/8.
 */
public enum Status {
    ACTIVITY("活动中","ACTIVITY"),
    INACTIVE("已停用","INACTIVE"),
    FAILURE("已失效","FAILURE"),
    EXPR("删除","EXPR"),
    DELAY("待生效","DELAY"),
    ALL("全部","ALL");

    private String name;
    private String value;
    Status(String name,String value){
        this.name=name;
        this.value=value;
    }

    /**
     * 通用状态 YES NO  是否
     * @return
     */
    public static final String YES = "1";

    public static final String NO = "0";

    /**
     * 事后促销在资格范围内初始状态
     */
    public static final String NEW = "NEW";
    /**
     * 事后促销在资格范围外后补初始状态
     */
    public static final String WATE_NEW = "WAIT_NEW";

    /**
     * 事后促销在完成
     */
    public static final String FINISH = "FINISH";


    /**
     * 事后促销后补在完成
     */
    public static final String WAIT_FINI = "WAIT_FINI";

    /**
     * 事后促销名额内 失去资格
     */
    public static final String CANCEL = "CANCEL";


    /**
     * 事后促销后补 失去资格
     */
    public static final String WAIT_CANCEL = "WAIT_CANCEL";


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
