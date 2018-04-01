package com.hand.promotion.pojo.activity;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/25
 * @description 促销模板描述信息
 */
public class SaleTemplateDesp {

    /**
     *主键
     */

    private String id;

    /**
     * 编码
     */
    private String templateId;


    /**
     * 版本id
     */
    private String releaseId;


    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板描述
     */
    private String templateDes;

    /**
     * 分组
     */
    private String group;

    /**
     * 前台展示信息
     */
    private String pageShowMes;

    /**
     * 创建时间
     */
    private Long creationTime;

    /**
     * 最近更新时间
     */
    private Long lastCreationTime;


    /**
     * 是否使用
     */
    private String isUsing;

    public String getTemplateId() {

        return templateId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDes() {
        return templateDes;
    }

    public void setTemplateDes(String templateDes) {
        this.templateDes = templateDes;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPageShowMes() {
        return pageShowMes;
    }

    public void setPageShowMes(String pageShowMes) {
        this.pageShowMes = pageShowMes;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getLastCreationTime() {
        return lastCreationTime;
    }

    public void setLastCreationTime(Long lastCreationTime) {
        this.lastCreationTime = lastCreationTime;
    }

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing;
    }

    @Override
    public String toString() {
        return "{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"templateId\":\"" + templateId + "\""
            + ",                         \"releaseId\":\"" + releaseId + "\""
            + ",                         \"templateName\":\"" + templateName + "\""
            + ",                         \"templateDes\":\"" + templateDes + "\""
            + ",                         \"group\":\"" + group + "\""
            + ",                         \"pageShowMes\":\"" + pageShowMes + "\""
            + ",                         \"creationTime\":\"" + creationTime + "\""
            + ",                         \"lastCreationTime\":\"" + lastCreationTime + "\""
            + ",                         \"isUsing\":\"" + isUsing + "\""
            + "}";
    }
}
