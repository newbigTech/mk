package com.hand.promotion.pojo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/25
 * @description mongoDb 分页dto
 */

public class Pagination implements Serializable, Pageable {

    /**
     * 当前页码
     */
    private int pageNumber = 1;

    /**
     * 页面条数
     */
    private int pageSize = 10;

    /**
     * 排序条件
     */
    private Sort sort;


    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "{\"Pagination\":{"
            + "                        \"pageNumber\":\"" + pageNumber + "\""
            + ",                         \"pageSize\":\"" + pageSize + "\""
            + ",                         \"sort\":" + sort
            + "}}";
    }
}
