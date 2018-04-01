package com.hand.hap.im.dto;

import com.hand.common.util.ExcelVOAttribute;

/**
 * @Author:chenzhigang
 * @Description:BOM接口实体类
 * @Date:Crated in 15:28 2017/12/27
 * @Modified By:
 */
public class ImBomInfaceDto {

    @ExcelVOAttribute(name = "客户端", column = "A")
    private String mandt;

    @ExcelVOAttribute(name = "ECN号", column = "B")
    private String ecnnumber;

    @ExcelVOAttribute(name = "发布号", column = "C")
    private String publishno;

    @ExcelVOAttribute(name = "项目号", column = "D")
    private String item;

    @ExcelVOAttribute(name = "状态", column = "E")
    private String state;

    @ExcelVOAttribute(name = "物料号", column = "F")
    private String matnr;

    @ExcelVOAttribute(name = "工厂", column = "G")
    private String werks;

    @ExcelVOAttribute(name = "BOM用途", column = "H")
    private String stlan;

    @ExcelVOAttribute(name = "可选的BOM", column = "I")
    private String stlal;

    @ExcelVOAttribute(name = "基本数量", column = "J")
    private String bmeng;

    @ExcelVOAttribute(name = "BOM状态", column = "K")
    private String stlst;

    @ExcelVOAttribute(name = "项目类别（物料单）", column = "L")
    private String postp;

    @ExcelVOAttribute(name = "项目类别（物料单）", column = "M")
    private String posnr;

    @ExcelVOAttribute(name = "BOM 组件", column = "N")
    private String idnrk;

    @ExcelVOAttribute(name = "组件数量", column = "O")
    private String menge;

    @ExcelVOAttribute(name = "组件计量单位", column = "P")
    private String meins;

    @ExcelVOAttribute(name = "BOM 项目文本（行1）", column = "Q")
    private String potx1;

    @ExcelVOAttribute(name = "BOM 项目文本（行2）", column = "R")
    private String potx2;

    @ExcelVOAttribute(name = "部件废品百分数", column = "S")
    private String ausch;

    @ExcelVOAttribute(name = "部件废品百分数", column = "T")
    private String alpgr;

    @ExcelVOAttribute(name = "部件废品百分数", column = "U")
    private String alprf;

    @ExcelVOAttribute(name = "可选项目", column = "V")
    private String ewahr;

    @ExcelVOAttribute(name = "尺寸1", column = "W")
    private String roms1;

    @ExcelVOAttribute(name = "尺寸2", column = "X")
    private String roms2;

    @ExcelVOAttribute(name = "尺寸3", column = "Y")
    private String roms3;

    @ExcelVOAttribute(name = "生产订单的发货地点", column = "Z")
    private String lgort;

    @ExcelVOAttribute(name = "排序字符串", column = "AA")
    private String sortf;

    @ExcelVOAttribute(name = "OD,相关性", column = "AB")
    private String objdp;

    @ExcelVOAttribute(name = "毛料SIZE", column = "AC")
    private String mlsize;

    @ExcelVOAttribute(name = "写入日期", column = "AD")
    private String zdate;

    @ExcelVOAttribute(name = "写入时间", column = "AE")
    private String ztime;

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEcnnumber() {
        return ecnnumber;
    }

    public void setEcnnumber(String ecnnumber) {
        this.ecnnumber = ecnnumber;
    }

    public String getPublishno() {
        return publishno;
    }

    public void setPublishno(String publishno) {
        this.publishno = publishno;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getStlan() {
        return stlan;
    }

    public void setStlan(String stlan) {
        this.stlan = stlan;
    }

    public String getStlal() {
        return stlal;
    }

    public void setStlal(String stlal) {
        this.stlal = stlal;
    }

    public String getBmeng() {
        return bmeng;
    }

    public void setBmeng(String bmeng) {
        this.bmeng = bmeng;
    }

    public String getStlst() {
        return stlst;
    }

    public void setStlst(String stlst) {
        this.stlst = stlst;
    }

    public String getPostp() {
        return postp;
    }

    public void setPostp(String postp) {
        this.postp = postp;
    }

    public String getPosnr() {
        return posnr;
    }

    public void setPosnr(String posnr) {
        this.posnr = posnr;
    }

    public String getIdnrk() {
        return idnrk;
    }

    public void setIdnrk(String idnrk) {
        this.idnrk = idnrk;
    }

    public String getMenge() {
        return menge;
    }

    public void setMenge(String menge) {
        this.menge = menge;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public String getPotx1() {
        return potx1;
    }

    public void setPotx1(String potx1) {
        this.potx1 = potx1;
    }

    public String getPotx2() {
        return potx2;
    }

    public void setPotx2(String potx2) {
        this.potx2 = potx2;
    }

    public String getAusch() {
        return ausch;
    }

    public void setAusch(String ausch) {
        this.ausch = ausch;
    }

    public String getAlpgr() {
        return alpgr;
    }

    public void setAlpgr(String alpgr) {
        this.alpgr = alpgr;
    }

    public String getAlprf() {
        return alprf;
    }

    public void setAlprf(String alprf) {
        this.alprf = alprf;
    }

    public String getEwahr() {
        return ewahr;
    }

    public void setEwahr(String ewahr) {
        this.ewahr = ewahr;
    }

    public String getRoms1() {
        return roms1;
    }

    public void setRoms1(String roms1) {
        this.roms1 = roms1;
    }

    public String getRoms2() {
        return roms2;
    }

    public void setRoms2(String roms2) {
        this.roms2 = roms2;
    }

    public String getRoms3() {
        return roms3;
    }

    public void setRoms3(String roms3) {
        this.roms3 = roms3;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    public String getSortf() {
        return sortf;
    }

    public void setSortf(String sortf) {
        this.sortf = sortf;
    }

    public String getObjdp() {
        return objdp;
    }

    public void setObjdp(String objdp) {
        this.objdp = objdp;
    }

    public String getMlsize() {
        return mlsize;
    }

    public void setMlsize(String mlsize) {
        this.mlsize = mlsize;
    }

    public String getZdate() {
        return zdate;
    }

    public void setZdate(String zdate) {
        this.zdate = zdate;
    }

    public String getZtime() {
        return ztime;
    }

    public void setZtime(String ztime) {
        this.ztime = ztime;
    }
}
