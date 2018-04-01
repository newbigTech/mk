package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Pricerow;
import com.hand.hmall.mst.dto.PricerowDto;
import com.hand.hmall.mst.dto.Product;
import net.sf.json.JSONArray;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 价格行对象的service接口
 * @date 2017/7/10 14:37
 */
public interface IPricerowService extends IBaseService<Pricerow>, ProxySelf<IPricerowService> {

    /**
     * 推送价格行信息至商城
     *
     * @return
     */
    public List<PricerowDto> selectPushingPricerow();

    /**
     * @return
     * @description 推送定制商品的价格行信息到M3D
     */
    public List<PricerowDto> pushPricerowToM3D();

    /**
     * @param dto
     * @return
     * @description 通过productId来查找是否含有该商品的促销价的价格行信息
     */
    public List<PricerowDto> selectDiscountPricerow(PricerowDto dto);

    /**
     * @param dto
     * @description 更新价格行接口推送标志
     */
    public void updatePricerowSyncflag(List<PricerowDto> dto);


    /**
     * 商品详情页查询价格详情
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    public List<Pricerow> queryInfo(Pricerow dto, int page, int pageSize);

    /**
     * 商品详情页查询价格详情
     *
     * @param dto
     * @return
     */
    public List<Pricerow> queryInfo(Pricerow dto);

    /**
     * 商品详情信息中 删除价格行关联
     *
     * @param dto
     * @return
     */
    public int updatePricerow(Pricerow dto);

    /**
     * 删除商品时，同时删除价格行记录
     *
     * @param dto
     * @return
     */
    public int deletePricerowByProductId(Product dto);

    /**
     * 删除商品版本目录订单行
     *
     * @param dto
     * @return
     */
    int deletePricerowByProductAndVersion(Product dto);

    /**
     * 价格维护界面查询功能
     *
     * @param dto
     * @return
     */
    public List<Pricerow> selectPricerow(IRequest request, Pricerow dto, int page, int pageSize);

    /**
     * @param iRequest
     * @param dto
     * @return
     * @description 导入价格行时，若是否覆盖原纪录为Y时，则删除与本条记录商品编号、价格类型、框架等级、用户、销售单位相同的价格行
     */
    public int coverPricerow(IRequest iRequest, Pricerow dto);

    /**
     * @param iRequest
     * @param list
     * @return
     * @description 导入价格行时，验证价格行的数据的正确性
     */
    public Map<String,Object> checkPricerow(IRequest iRequest, List<Pricerow> list) throws ParseException;

    /**
     * 查询待同步的价格行
     * @param catalogversionId 目录版本
     * @return List<Pricerow>
     */
    List<Pricerow> selectUnsyncPricerows(Long catalogversionId);

    /**
     * 将价格行推送到M3D
     * @param jsonArray jsonArray
     * @param pricerowList jsonArray对应的价格行
     * @param URL 请求地址
     * @param programDesc 程序描述
     */
    void postToM3D(JSONArray jsonArray, List<Pricerow> pricerowList, String URL, String programDesc);

    /**
     * 将价格行同步到M3D
     * @param pricerowList 价格行
     */
    void postToM3D(List<Pricerow> pricerowList);
}