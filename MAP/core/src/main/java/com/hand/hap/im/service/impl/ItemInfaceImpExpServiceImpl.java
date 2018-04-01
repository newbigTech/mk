package com.hand.hap.im.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImItemInface;
import com.hand.hap.im.service.IImItemInfaceService;
import com.hand.hap.im.service.IItemInfaceImpExpService;
import com.hand.hap.im.service.ItemExcel;
import com.hand.hap.im.service.ItemExcelUtils;
import com.hand.hap.mdm.item.dto.MdmItemValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author alaowan
 *         Created at 2017/12/26 23:43
 * @description
 */
@Service
public class ItemInfaceImpExpServiceImpl implements IItemInfaceImpExpService {

    @Autowired
    private IImItemInfaceService imItemInfaceService;

    @Override
    public Map<String, Object> importItemExcel(IRequest iRequest, InputStream inputStream, String fileName) throws Exception {
        ItemExcelUtils reader = new ItemExcelUtils(fileName, inputStream);
        // 从第1行开始读
        reader.setCurrPosition(1);
        Map<String, Object> res = reader.readExcel();
        boolean flag = (boolean) res.get("success");
        if (flag) { // 没有错误
            List<ImItemInface> imItemInfaces = new ArrayList<ImItemInface>();
            @SuppressWarnings("unchecked")
            List<List<?>> excels = (List<List<?>>) res.get("data");
            if (excels == null || excels.size() == 0) {
                throw new RuntimeException(fileName + "是一个空文件!");
            }
            //读取字段set到对象中
            for (List exc : excels) {
                ImItemInface attr = new ImItemInface();

                String mandt = exc.get(0).toString().trim();
                attr.setMandt(mandt);

                String ecnnumber = exc.get(1).toString().trim();
                attr.setEcnnumber(ecnnumber);

                String publishno = exc.get(2).toString().trim();
                attr.setPublishno(publishno);

                String item = exc.get(3).toString().trim();
                attr.setItem(item);

                String state = exc.get(4).toString().trim();
                attr.setState(state);

                String matnr = exc.get(5).toString().trim();
                attr.setMatnr(matnr);

                String mtart = exc.get(6).toString().trim();
                attr.setMtart(mtart);

                String lvorm = exc.get(7).toString().trim();
                attr.setLvorm(lvorm);

                String weeks = exc.get(8).toString().trim();
                attr.setWeeks(weeks);

                String cmaktx = exc.get(9).toString().trim();
                attr.setCmaktx(cmaktx);

                String emaktx = exc.get(10).toString().trim();
                attr.setEmaktx(emaktx);

                String meins = exc.get(11).toString().trim();
                attr.setMeins(meins);

                String bismt = exc.get(12).toString().trim();
                attr.setBismt(bismt);

                String matkl = exc.get(13).toString().trim();
                attr.setMatkl(matkl);

                String spart = exc.get(14).toString().trim();
                attr.setSpart(spart);

                String mstae = exc.get(15).toString().trim();
                attr.setMstae(mstae);

                String extwg = exc.get(16).toString().trim();
                attr.setExtwg(extwg);

                String prdha = exc.get(17).toString().trim();
                attr.setPrdha(prdha);

                String gewei = exc.get(18).toString().trim();
                attr.setGewei(gewei);

                String volum = exc.get(19).toString().trim();
                attr.setVolum(volum);

                String voleh = exc.get(20).toString().trim();
                attr.setVoleh(voleh);

                String groes = exc.get(21).toString().trim();
                attr.setGroes(groes);

                String brgew = exc.get(22).toString().trim();
                attr.setBrgew(brgew);

                String ntgew = exc.get(23).toString().trim();
                attr.setNtgew(ntgew);

                String kzkfg = exc.get(24).toString().trim();
                attr.setKzkfg(kzkfg);

                String zeinr = exc.get(25).toString().trim();
                attr.setZeinr(zeinr);

                String zpacksize = exc.get(26).toString().trim();
                attr.setZpacksize(zpacksize);

                String zpackquant = exc.get(27).toString().trim();
                attr.setZpackquant(zpackquant);

                String zdate = exc.get(28).toString().trim();
                attr.setZdate(zdate);

                String ztime = exc.get(29).toString().trim();
                attr.setZtime(ztime);

                String zzmatnr = exc.get(30).toString().trim();
                attr.setZzmatnr(zzmatnr);

                String zpublishno = exc.get(31).toString().trim();
                attr.setZpublishno(zpublishno);

                String zflag = exc.get(32).toString().trim();
                attr.setZflag(zflag);

                String zconstituent = exc.get(33).toString().trim();
                attr.setZconnsize(zconstituent);

                String zconnsize = exc.get(34).toString().trim();
                attr.setZconnsize(zconnsize);

                String zxuni = exc.get(35).toString().trim();
                attr.setZxuni(zxuni);

                String zmaterial = exc.get(36).toString().trim();
                attr.setZmaterial(zmaterial);

                String zpanlsize = exc.get(37).toString().trim();
                attr.setZpanlsize(zpanlsize);

                String zvolume = exc.get(38).toString().trim();
                attr.setZvolume(zvolume);

                String znetmasize = exc.get(39).toString().trim();
                attr.setZnetmasize(znetmasize);

                String zisrough = exc.get(40).toString().trim();
                attr.setZisrough(zisrough);

                String zisspare = exc.get(41).toString().trim();
                attr.setZisspare(zisspare);

                String zmcPanel = exc.get(42).toString().trim();
                attr.setZmcPanel(zmcPanel);

                String zmcPanel2 = exc.get(43).toString().trim();
                attr.setZmcPanel2(zmcPanel2);

                String zwoodtype = exc.get(44).toString().trim();
                attr.setZwoodtype(zwoodtype);

                String zpartcomp = exc.get(45).toString().trim();
                attr.setZpartcomp(zpartcomp);

                String zhrdwrtype = exc.get(46).toString().trim();
                attr.setZhrdwrtype(zhrdwrtype);

                String zassblsize = exc.get(47).toString().trim();
                attr.setZassblsize(zassblsize);

                String zparquet = exc.get(48).toString().trim();
                attr.setZparquet(zparquet);

                String jjuse = exc.get(49).toString().trim();
                attr.setJjuse(jjuse);

                String pbuse = exc.get(50).toString().trim();
                attr.setPbuse(pbuse);

                String zsfwg = exc.get(51).toString().trim();
                attr.setZsfwg(zsfwg);

                String zsfww = exc.get(52).toString().trim();
                attr.setZsfww(zsfww);

                String zsfsz = exc.get(53).toString().trim();
                attr.setZsfsz(zsfsz);

                String zlygc = exc.get(54).toString().trim();
                attr.setZlygc(zlygc);

                String formulaid = exc.get(55).toString().trim();
                attr.setFormulaid(formulaid);

                String reswc = exc.get(56).toString().trim();
                attr.setReswc(reswc);

                String zrollcolor = exc.get(57).toString().trim();
                attr.setZrollcolor(zrollcolor);

                String zpainting = exc.get(58).toString().trim();
                attr.setZpainting(zpainting);

                String length = exc.get(59).toString().trim();
                attr.setLength(length);

                String width = exc.get(60).toString().trim();
                attr.setWidth(width);

                String higth = exc.get(61).toString().trim();
                attr.setHeight(higth);

                String backupItemType = exc.get(62).toString().trim();
                attr.setStocking(backupItemType);

                String setFlag = exc.get(63).toString().trim();
                attr.setSetflag(setFlag);
                imItemInfaces.add(attr);

                //新增品牌字段    add by zhangyanan 2018-02-05 begin
                String brand = exc.get(64).toString().trim();
                attr.setBrand(brand);
                //新增品牌字段    add by zhangyanan 2018-02-05 end
            }
            imItemInfaceService.importItemExcel(iRequest, imItemInfaces);
        }
        return res;
    }

    @Override
    public ByteArrayOutputStream exportItemExcel(IRequest request, String fileName) throws Exception {
        ItemExcel ItemExcel = new ItemExcel();
        //调用导出excel工具类
        return ItemExcel.excelWriter();
    }

    @Override
    public ByteArrayOutputStream exportItemValueExcel(IRequest request, String fileName) throws Exception {
        ItemExcel ItemExcel = new ItemExcel();
        //调用导出excel工具类
        return ItemExcel.excelValueWriter();
    }

    public Map<String, Object> importItemValueExcel(IRequest iRequest, InputStream inputStream, String fileName) throws Exception {
        ItemExcelUtils reader = new ItemExcelUtils(fileName, inputStream);
        // 从第1行开始读
        reader.setCurrPosition(1);
        //调用工具类方法读取Excel的内容
        Map<String, Object> res = reader.readItemValueExcel();
        boolean flag = (boolean) res.get("success");
        if (flag) {// 没有错误
            List<MdmItemValue> imItemValues = new ArrayList<MdmItemValue>();
            @SuppressWarnings("unchecked")
            List<List<?>> excels = (List<List<?>>) res.get("data");
            if (excels == null || excels.size() == 0) {
                throw new RuntimeException(fileName + "是一个空文件!");
            }
            //读取字段set到对象中
            for (List exc : excels) {
                MdmItemValue attr = new MdmItemValue();

                String itemCode = exc.get(0).toString().trim();
                attr.setItemCode(itemCode);

                String chooseValueFlag = exc.get(1).toString().trim();
                attr.setChooseValueFlag(chooseValueFlag);

                String chooseItemFlag = exc.get(2).toString().trim();
                attr.setChooseItemFlag(chooseItemFlag);

                String attr6 = exc.get(3).toString().trim();
                attr.setAttr6(attr6);

                String attr7 = exc.get(4).toString().trim();
                attr.setAttr7(attr7);

                String a0001 = exc.get(5).toString().trim();
                attr.setA0001(a0001);

                String a0002 = exc.get(6).toString().trim();
                attr.setA0002(a0002);

                String a0003 = exc.get(7).toString().trim();
                attr.setA0003(a0003);

                String a0004 = exc.get(8).toString().trim();
                attr.setA0004(a0004);

                String a0005 = exc.get(9).toString().trim();
                attr.setA0005(a0005);

                String a0006 = exc.get(10).toString().trim();
                attr.setA0006(a0006);

                String a0007 = exc.get(11).toString().trim();
                attr.setA0007(a0007);

                String a0008 = exc.get(12).toString().trim();
                attr.setA0008(a0008);

                String a0009 = exc.get(13).toString().trim();
                attr.setA0009(a0009);

                String a0010 = exc.get(14).toString().trim();
                attr.setA0010(a0010);

                String a0011 = exc.get(15).toString().trim();
                attr.setA0011(a0011);

                String a0012 = exc.get(16).toString().trim();
                attr.setA0012(a0012);

                String a0013 = exc.get(17).toString().trim();
                attr.setA0013(a0013);

                String a0014 = exc.get(18).toString().trim();
                attr.setA0014(a0014);

                String a0015 = exc.get(19).toString().trim();
                attr.setA0015(a0015);

                String a0016 = exc.get(20).toString().trim();
                attr.setA0016(a0016);

                String a0017 = exc.get(21).toString().trim();
                attr.setA0017(a0017);

                String a0018 = exc.get(22).toString().trim();
                attr.setA0018(a0018);

                String a0019 = exc.get(23).toString().trim();
                attr.setA0019(a0019);

                String a0020 = exc.get(24).toString().trim();
                attr.setA0020(a0020);

                String a0021 = exc.get(25).toString().trim();
                attr.setA0021(a0021);

                String a0022 = exc.get(26).toString().trim();
                attr.setA0022(a0022);

                String a0023 = exc.get(27).toString().trim();
                attr.setA0023(a0023);

                String a0024 = exc.get(28).toString().trim();
                attr.setA0024(a0024);

                String a0025 = exc.get(29).toString().trim();
                attr.setA0025(a0025);

                imItemValues.add(attr);
            }
            imItemInfaceService.importItemValueExcel(iRequest, imItemValues);
        }
        return res;
    }
}
