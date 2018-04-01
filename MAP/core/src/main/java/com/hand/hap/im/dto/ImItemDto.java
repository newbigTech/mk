package com.hand.hap.im.dto;

import com.hand.common.util.ExcelVOAttribute;

/**
 * @author baihua
 * @version 0.1
 * @name ImItemDto$
 * @description $END$ 物料數據導出接口
 * @date 2017/12/27$
 */
public class ImItemDto {

    @ExcelVOAttribute(name = "客戶端", column = "A", isExport = true)
    private String mandt;

    @ExcelVOAttribute(name = "ECN号", column = "B", isExport = true)
    private String ecnnumber;

    @ExcelVOAttribute(name = "发布号", column = "C", isExport = true)
    private String publishno;

    @ExcelVOAttribute(name = "项目号", column = "D", isExport = true)
    private String item;

    @ExcelVOAttribute(name = "状态", column = "E", isExport = true)
    private String state;

    @ExcelVOAttribute(name = "物料号", column = "F", isExport = true)
    private String matnr;

    @ExcelVOAttribute(name = "物料类型", column = "G", isExport = true)
    private String mtart;

    @ExcelVOAttribute(name = "删除标识符", column = "H", isExport = true)
    private String lvorm;

    @ExcelVOAttribute(name = "工厂", column = "I", isExport = true)
    private String weeks;

    @ExcelVOAttribute(name = "物料描述（短文本）", column = "J", isExport = true)
    private String cmaktx;

    @ExcelVOAttribute(name = "物料描述（短文本）", column = "K", isExport = true)
    private String emaktx;

    @ExcelVOAttribute(name = "基本计量单位", column = "L", isExport = true)
    private String meins;

    @ExcelVOAttribute(name = "旧物料号", column = "M", isExport = true)
    private String bismt;

    @ExcelVOAttribute(name = "物料组", column = "N", isExport = true)
    private String matkl;

    @ExcelVOAttribute(name = "产品组", column = "O", isExport = true)
    private String spart;

    @ExcelVOAttribute(name = "跨工厂物料状态", column = "P", isExport = true)
    private String mstae;

    @ExcelVOAttribute(name = "外部物料组", column = "Q", isExport = true)
    private String extwg;

    @ExcelVOAttribute(name = "产品层次", column = "R", isExport = true)
    private String prdha;

    @ExcelVOAttribute(name = "重量单位", column = "S", isExport = true)
    private String gewei;

    @ExcelVOAttribute(name = "业务量", column = "T", isExport = true)
    private String volum;

    @ExcelVOAttribute(name = "体积单位", column = "U", isExport = true)
    private String voleh;

    @ExcelVOAttribute(name = "大小/量纲", column = "V", isExport = true)
    private String groes;

    @ExcelVOAttribute(name = "毛重", column = "W", isExport = true)
    private String brgew;

    @ExcelVOAttribute(name = "净重", column = "X", isExport = true)
    private String ntgew;

    @ExcelVOAttribute(name = "可配置的物料", column = "Y", isExport = true)
    private String kzkfg;

    @ExcelVOAttribute(name = "文档号码(无文档管理系统)", column = "Z", isExport = true)
    private String zeinr;

    @ExcelVOAttribute(name = "包装尺寸", column = "AA", isExport = true)
    private String zpacksize;

    @ExcelVOAttribute(name = "件箱转换系数", column = "AB", isExport = true)
    private String zpackquant;

    @ExcelVOAttribute(name = "写入日期", column = "AC", isExport = true)
    private String zdate;

    @ExcelVOAttribute(name = "写入时间", column = "AD", isExport = true)
    private String ztime;

    @ExcelVOAttribute(name = "旧物料号", column = "AE", isExport = true)
    private String zzmatnr;

    @ExcelVOAttribute(name = "原发布号", column = "AF", isExport = true)
    private String zpublishno;

    @ExcelVOAttribute(name = "是否重复发送 是：X", column = "AG", isExport = true)
    private String zflag;

    @ExcelVOAttribute(name = "面料材质", column = "AH", isExport = true)
    private String zconstituent;

    @ExcelVOAttribute(name = "交接尺寸", column = "AI", isExport = true)
    private String zconnsize;

    @ExcelVOAttribute(name = "虚拟物料 是：X", column = "AJ", isExport = true)
    private String zxuni;

    @ExcelVOAttribute(name = "材质", column = "AK", isExport = true)
    private String zmaterial;

    @ExcelVOAttribute(name = "包装材积", column = "AL", isExport = true)
    private String zvolume;

    @ExcelVOAttribute(name = "拼版尺寸(长 *宽*厚)", column = "AM", isExport = true)
    private String zpanlsize;

    @ExcelVOAttribute(name = "净料尺寸(长 *宽*厚)", column = "AN", isExport = true)
    private String znetmasize;

    @ExcelVOAttribute(name = "毛坯件（X件）", column = "AO", isExport = true)
    private String zisrough;

    @ExcelVOAttribute(name = "要做备件", column = "AP", isExport = true)
    private String zisspare;

    @ExcelVOAttribute(name = "MC", column = "AQ", isExport = true)
    private String zmcPanel;

    @ExcelVOAttribute(name = "备货点", column = "AR", isExport = true)
    private String zmcPanel2;

    @ExcelVOAttribute(name = "材型", column = "AS", isExport = true)
    private String zwoodtype;

    @ExcelVOAttribute(name = "零件毛坯组件", column = "AT", isExport = true)
    private String zpartcomp;

    @ExcelVOAttribute(name = "区分通用件、专用件", column = "AU", isExport = true)
    private String zhrdwrtype;

    @ExcelVOAttribute(name = "组装模式（区分KD或组死）", column = "AV", isExport = true)
    private String zassblsize;

    @ExcelVOAttribute(name = "拼花", column = "AW", isExport = true)
    private String zparquet;

    @ExcelVOAttribute(name = "交接用法", column = "AX", isExport = true)
    private String jjuse;

    @ExcelVOAttribute(name = "拼版用法", column = "AY", isExport = true)
    private String pbuse;

    @ExcelVOAttribute(name = "是否外购", column = "AZ", isExport = true)
    private String zsfwg;

    @ExcelVOAttribute(name = "是否委外", column = "BA", isExport = true)
    private String zsfww;

    @ExcelVOAttribute(name = "是否散装物料", column = "BB", isExport = true)
    private String zsfsz;

    @ExcelVOAttribute(name = "领用工厂", column = "BC", isExport = true)
    private String zlygc;

    @ExcelVOAttribute(name = "配方号", column = "BD", isExport = true)
    private String formulaid;

    @ExcelVOAttribute(name = "树脂木芯", column = "BE", isExport = true)
    private String reswc;

    @ExcelVOAttribute(name = "滚涂颜色", column = "BF", isExport = true)
    private String zrollcolor;

    @ExcelVOAttribute(name = "虚拟调色漆", column = "BG", isExport = true)
    private String zpainting;

    @ExcelVOAttribute(name = "删除标记", column = "BH", isExport = true)
    private String deleteFlag;

    @ExcelVOAttribute(name = "品牌", column = "BI", isExport = true)
    private String brand;

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

    public String getMtart() {
        return mtart;
    }

    public void setMtart(String mtart) {
        this.mtart = mtart;
    }

    public String getLvorm() {
        return lvorm;
    }

    public void setLvorm(String lvorm) {
        this.lvorm = lvorm;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getCmaktx() {
        return cmaktx;
    }

    public void setCmaktx(String cmaktx) {
        this.cmaktx = cmaktx;
    }

    public String getEmaktx() {
        return emaktx;
    }

    public void setEmaktx(String emaktx) {
        this.emaktx = emaktx;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public String getBismt() {
        return bismt;
    }

    public void setBismt(String bismt) {
        this.bismt = bismt;
    }

    public String getMatkl() {
        return matkl;
    }

    public void setMatkl(String matkl) {
        this.matkl = matkl;
    }

    public String getSpart() {
        return spart;
    }

    public void setSpart(String spart) {
        this.spart = spart;
    }

    public String getMstae() {
        return mstae;
    }

    public void setMstae(String mstae) {
        this.mstae = mstae;
    }

    public String getExtwg() {
        return extwg;
    }

    public void setExtwg(String extwg) {
        this.extwg = extwg;
    }

    public String getPrdha() {
        return prdha;
    }

    public void setPrdha(String prdha) {
        this.prdha = prdha;
    }

    public String getGewei() {
        return gewei;
    }

    public void setGewei(String gewei) {
        this.gewei = gewei;
    }

    public String getVolum() {
        return volum;
    }

    public void setVolum(String volum) {
        this.volum = volum;
    }

    public String getVoleh() {
        return voleh;
    }

    public void setVoleh(String voleh) {
        this.voleh = voleh;
    }

    public String getGroes() {
        return groes;
    }

    public void setGroes(String groes) {
        this.groes = groes;
    }

    public String getBrgew() {
        return brgew;
    }

    public void setBrgew(String brgew) {
        this.brgew = brgew;
    }

    public String getNtgew() {
        return ntgew;
    }

    public void setNtgew(String ntgew) {
        this.ntgew = ntgew;
    }

    public String getKzkfg() {
        return kzkfg;
    }

    public void setKzkfg(String kzkfg) {
        this.kzkfg = kzkfg;
    }

    public String getZeinr() {
        return zeinr;
    }

    public void setZeinr(String zeinr) {
        this.zeinr = zeinr;
    }

    public String getZpacksize() {
        return zpacksize;
    }

    public void setZpacksize(String zpacksize) {
        this.zpacksize = zpacksize;
    }

    public String getZpackquant() {
        return zpackquant;
    }

    public void setZpackquant(String zpackquant) {
        this.zpackquant = zpackquant;
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

    public String getZzmatnr() {
        return zzmatnr;
    }

    public void setZzmatnr(String zzmatnr) {
        this.zzmatnr = zzmatnr;
    }

    public String getZpublishno() {
        return zpublishno;
    }

    public void setZpublishno(String zpublishno) {
        this.zpublishno = zpublishno;
    }

    public String getZflag() {
        return zflag;
    }

    public void setZflag(String zflag) {
        this.zflag = zflag;
    }

    public String getZconstituent() {
        return zconstituent;
    }

    public void setZconstituent(String zconstituent) {
        this.zconstituent = zconstituent;
    }

    public String getZconnsize() {
        return zconnsize;
    }

    public void setZconnsize(String zconnsize) {
        this.zconnsize = zconnsize;
    }

    public String getZxuni() {
        return zxuni;
    }

    public void setZxuni(String zxuni) {
        this.zxuni = zxuni;
    }

    public String getZmaterial() {
        return zmaterial;
    }

    public void setZmaterial(String zmaterial) {
        this.zmaterial = zmaterial;
    }

    public String getZvolume() {
        return zvolume;
    }

    public void setZvolume(String zvolume) {
        this.zvolume = zvolume;
    }

    public String getZpanlsize() {
        return zpanlsize;
    }

    public void setZpanlsize(String zpanlsize) {
        this.zpanlsize = zpanlsize;
    }

    public String getZnetmasize() {
        return znetmasize;
    }

    public void setZnetmasize(String znetmasize) {
        this.znetmasize = znetmasize;
    }

    public String getZisrough() {
        return zisrough;
    }

    public void setZisrough(String zisrough) {
        this.zisrough = zisrough;
    }

    public String getZisspare() {
        return zisspare;
    }

    public void setZisspare(String zisspare) {
        this.zisspare = zisspare;
    }

    public String getZmcPanel() {
        return zmcPanel;
    }

    public void setZmcPanel(String zmcPanel) {
        this.zmcPanel = zmcPanel;
    }

    public String getZmcPanel2() {
        return zmcPanel2;
    }

    public void setZmcPanel2(String zmcPanel2) {
        this.zmcPanel2 = zmcPanel2;
    }

    public String getZwoodtype() {
        return zwoodtype;
    }

    public void setZwoodtype(String zwoodtype) {
        this.zwoodtype = zwoodtype;
    }

    public String getZpartcomp() {
        return zpartcomp;
    }

    public void setZpartcomp(String zpartcomp) {
        this.zpartcomp = zpartcomp;
    }

    public String getZhrdwrtype() {
        return zhrdwrtype;
    }

    public void setZhrdwrtype(String zhrdwrtype) {
        this.zhrdwrtype = zhrdwrtype;
    }

    public String getZassblsize() {
        return zassblsize;
    }

    public void setZassblsize(String zassblsize) {
        this.zassblsize = zassblsize;
    }

    public String getZparquet() {
        return zparquet;
    }

    public void setZparquet(String zparquet) {
        this.zparquet = zparquet;
    }

    public String getJjuse() {
        return jjuse;
    }

    public void setJjuse(String jjuse) {
        this.jjuse = jjuse;
    }

    public String getPbuse() {
        return pbuse;
    }

    public void setPbuse(String pbuse) {
        this.pbuse = pbuse;
    }

    public String getZsfwg() {
        return zsfwg;
    }

    public void setZsfwg(String zsfwg) {
        this.zsfwg = zsfwg;
    }

    public String getZsfww() {
        return zsfww;
    }

    public void setZsfww(String zsfww) {
        this.zsfww = zsfww;
    }

    public String getZsfsz() {
        return zsfsz;
    }

    public void setZsfsz(String zsfsz) {
        this.zsfsz = zsfsz;
    }

    public String getZlygc() {
        return zlygc;
    }

    public void setZlygc(String zlygc) {
        this.zlygc = zlygc;
    }

    public String getFormulaid() {
        return formulaid;
    }

    public void setFormulaid(String formulaid) {
        this.formulaid = formulaid;
    }

    public String getReswc() {
        return reswc;
    }

    public void setReswc(String reswc) {
        this.reswc = reswc;
    }

    public String getZrollcolor() {
        return zrollcolor;
    }

    public void setZrollcolor(String zrollcolor) {
        this.zrollcolor = zrollcolor;
    }

    public String getZpainting() {
        return zpainting;
    }

    public void setZpainting(String zpainting) {
        this.zpainting = zpainting;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
