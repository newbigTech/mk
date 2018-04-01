package com.hand.hmall.drools.dto;

import java.util.List;

/**
 * Created by shanks on 2017/2/6.
 * 地区DTO
 */
public class AreaTreeView {


    private Long id;
    //地区描述
    private String text;
    //是否展开
    private boolean expanded;
    //父地区id
    private Long parentId;
    //图标
    private String icon;
    //是否勾选
    private boolean checked;
    //地区层级
    private Long lvl;
    //子区域集合
    private List<AreaTreeView> items;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<AreaTreeView> getItems() {
        return items;
    }

    public void setItems(List<AreaTreeView> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getLvl() {
        return lvl;
    }

    public void setLvl(Long lvl) {
        this.lvl = lvl;
    }
}
