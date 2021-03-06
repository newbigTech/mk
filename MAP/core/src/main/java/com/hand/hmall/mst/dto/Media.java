package com.hand.hmall.mst.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 多媒体对象dto
 * @date 2017/7/10 14:37
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_MEDIA")
public class Media extends BaseDTO {
    /**
     *
     */
    private static final long serialVersionUID = 1663256609667913321L;

    @Id
    @GeneratedValue
    private Long mediaId;

    @ExcelVOAttribute(name = "图片编码", column = "B")
    private String code;

    @ExcelVOAttribute(name = "图片名称", column = "D")
    private String name;

    @ExcelVOAttribute(name = "图片描述", column = "E")
    private String imageDescribe;

    @ExcelVOAttribute(name = "图片链接", column = "G")
    private String url;

    private String mediaSize;

    private String syncflag;

    private Long catalogversionId;

    private String sort;

    @ExcelVOAttribute(name = "图片类型", column = "H")
    private String imageGroup;

    private Long productId;

    private Date imageLastUpdate;

    private String md5Content;

    @ExcelVOAttribute(name = "图片路径", column = "F")
    private String path;

    private String updateFlag;

    /**
     * 是否启用
     */
    private String isHandle;

    //组合版本目录
    @Transient
    @ExcelVOAttribute(name = "目录版本", column = "C")
    private String catalog;

    //图片导入时的商品编码
    @Transient
    @ExcelVOAttribute(name = "商品编码", column = "A")
    private String productCode;

    public String getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public Long getCatalogversionId() {
        return catalogversionId;
    }

    public void setCatalogversionId(Long catalogversionId) {
        this.catalogversionId = catalogversionId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImageDescribe(String imageDescribe) {
        this.imageDescribe = imageDescribe;
    }

    public String getImageDescribe() {
        return imageDescribe;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setMediaSize(String mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getMediaSize() {
        return mediaSize;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(String imageGroup) {
        this.imageGroup = imageGroup;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Date getImageLastUpdate() {
        return imageLastUpdate;
    }

    public void setImageLastUpdate(Date imageLastUpdate) {
        this.imageLastUpdate = imageLastUpdate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getMd5Content() {
        return md5Content;
    }

    public void setMd5Content(String md5Content) {
        this.md5Content = md5Content;
    }
}
