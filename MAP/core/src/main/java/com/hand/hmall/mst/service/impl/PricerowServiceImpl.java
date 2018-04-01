package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.markor.map.framework.restclient.RestClient;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.*;
import com.hand.hmall.mst.mapper.PricerowMapper;
import com.hand.hmall.mst.service.*;
import com.hand.hmall.util.Constants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 价格行的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class PricerowServiceImpl extends BaseServiceImpl<Pricerow> implements IPricerowService {

    private static final String PRICE_URL = "/modules/strawberry/webservice/priceAccept";

    @Autowired
    private PricerowMapper pricerowMapper;

    @Autowired
    private ICodeService codeService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IMstUserService userService;

    @Autowired
    private ICatalogversionService catalogversionService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private RestClient restClient;

    /**
     * 推送价格行信息至商城
     * @return
     */
    @Override
    public List<PricerowDto> selectPushingPricerow() {
        return pricerowMapper.selectPushingPricerow();
    }

    /**
     *
     * @return
     */
    @Override
    public List<PricerowDto> pushPricerowToM3D() {
        return pricerowMapper.pushPricerowToM3D();
    }

    @Override
    public List<PricerowDto> selectDiscountPricerow(PricerowDto dto) {
        return pricerowMapper.selectDiscountPricerow(dto);
    }

    @Override
    public void updatePricerowSyncflag(List<PricerowDto> dto) {
        pricerowMapper.updatePricerowSyncflag(dto);
    }

    @Override
    public List<Pricerow> queryInfo(Pricerow dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return pricerowMapper.queryInfo(dto);
    }

    @Override
    public int updatePricerow(Pricerow dto) {
        return pricerowMapper.updatePricerow(dto);
    }

    @Override
    public int deletePricerowByProductId(Product dto) {
        return pricerowMapper.deletePricerowByProductId(dto);
    }

    @Override
    public List<Pricerow> selectPricerow(IRequest request, Pricerow dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return pricerowMapper.selectPricerow(dto);
    }

    @Override
    public int deletePricerowByProductAndVersion(Product dto) {
        return pricerowMapper.deletePricerowByProductAndVersion(dto);
    }

    @Override
    public List<Pricerow> queryInfo(Pricerow dto) {
        return pricerowMapper.queryInfo(dto);
    }


    @Override
    public int coverPricerow(IRequest iRequest, Pricerow dto) {
        return pricerowMapper.coverPricerow(dto);
    }

    @Override
    public Map<String,Object> checkPricerow(IRequest iRequest, List<Pricerow> list) throws ParseException {
        //校验输入的值是否在快码表中存在
        List<CodeValue> priceTypeData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRICE_TYPE");
        List<CodeValue> priceGroupData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRICE_GROUP");
        List<CodeValue> saleUnitData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.UNIT");
        List<CodeValue> odtypeData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.ODTYPE");

        List<Pricerow> pricerowList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        String result = null;
        if (list != null && list.size() > 0) {
            for (Pricerow pricerow : list) {

                //校验数据的必输性
                if (pricerow.getCode() == null || pricerow.getPriceType() == null || pricerow.getBasePrice() == null || pricerow.getSaleUnit() == null) {
                    result = "商品编码,价格类型，价格，销售单位不能为空!";
                    continue;
                }

                //验证价格类型
                int flag = 1;
                for (CodeValue codeValue : priceTypeData) {
                    if (pricerow.getPriceType().equals(codeValue.getMeaning())) {
                        pricerow.setPriceType(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的价格类型不存在!";
                    continue;
                }

                //验证价目表
                flag = 1;
                for (CodeValue codeValue : priceGroupData) {
                    if (pricerow.getPriceGroup() != null && pricerow.getPriceGroup().equals(codeValue.getMeaning())) {
                        pricerow.setPriceGroup(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (pricerow.getPriceGroup() != null && flag == 1) {
                    result = "您输入的价目表不存在!";
                    continue;
                }

                //验证销售单位
                flag = 1;
                for (CodeValue codeValue : saleUnitData) {
                    if (pricerow.getSaleUnit().equals(codeValue.getMeaning())) {
                        pricerow.setSaleUnit(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的销售单位不存在!";
                    continue;
                }

                //验证频道
                flag = 1;
                if(pricerow.getOdtype()==null){  //频道默认为1
                    pricerow.setOdtype("1");
                }else{
                    for(CodeValue codeValue:odtypeData){
                        if(pricerow.getOdtype().equals(codeValue.getMeaning())){
                            pricerow.setOdtype(codeValue.getValue());
                            flag =0;
                            break;
                        }
                    }
                    if(flag==1){
                        result = "您输入的频道不存在!";
                        continue;
                    }
                }


                //校验用户是否存在
                if (pricerow.getCustomerid() != null) {
                    MstUser user = new MstUser();
                    user.setCustomerid(pricerow.getCustomerid());
                    List<MstUser> userList = userService.select(iRequest, user, 1, 10);
                    if (userList == null || userList.size() == 0) {
                        result = "您输入的用户不存在!";
                        continue;
                    } else {
                        pricerow.setUserId(userList.get(0).getUserId());
                    }
                }

                //catalogName不填则默认为美克美家staged版本目录
                if (pricerow.getCatalogName() == null) {
                    pricerow.setCatalogName("美克美家-staged");
                }

                //校验目录版本
                flag = 1;
                for (Catalogversion catalogversion : catalogversionService.selectCatalogVersion()) {
                    if (pricerow.getCatalogName().equals(catalogversion.getCatalogName())) {
                        pricerow.setCatalogversionId(catalogversion.getCatalogversionId());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的目录版本不存在!";
                    continue;
                }
                //校验商品编码和目录版本组合的商品是否存在
                Product product = new Product();
                product.setCode(pricerow.getCode());
                product.setCatalogversionId(pricerow.getCatalogversionId());
                List<Product> pList = productService.selectByCodeAndCatalogVersion(product);
                if (pList == null || pList.size() == 0) {
                    result = "您输入的商品编码和目录版本组成的商品不存在!";
                    continue;
                } else {
                    pricerow.setProductId(pList.get(0).getProductId());
                }

                //如果没有输入开始时间&结束时间，默认从1990.01.01--2100.01.01
                if (pricerow.getStartTime() == null && pricerow.getEndTime() == null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    pricerow.setStartTime(sdf.parse("1990-01-01"));
                    pricerow.setEndTime(sdf.parse("2100-01-01"));
                }


                //Y--删除与本条记录频道相同的价格行
                if (pricerow.getIsCovered() != null && "Y".equals(pricerow.getIsCovered())) {
                    this.coverPricerow(iRequest,pricerow);
                }

                pricerowList.add(pricerow);
            }
        }
        map.put("pricerowList",pricerowList);
        map.put("result",result);
        return map;
    }

    @Override
    public List<Pricerow> selectUnsyncPricerows(Long catalogversionId) {
        return pricerowMapper.selectUnsyncPricerows(catalogversionId);
    }

    @Override
    public void postToM3D(JSONArray jsonArray, List<Pricerow> pricerowList, String URL, String programDesc) {
        String content = jsonArray.toString();
        try {
            Response response = restClient.postString(Constants.M3D, URL, content, Constants.MINI_TYPE_JSON, null, null);
            if (response.code() == 200) {
                JSONObject responseObj = RestClient.responseToJSON(response);
                if (responseObj.getInt("err") == 200) {
                    for (Pricerow pricerow : pricerowList) {
                        pricerow.setSyncflag(Constants.YES);
                        this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), pricerow);
                    }
                } else {
                    iLogManagerService.logError(this.getClass(), programDesc, null, responseObj.getString("errmsg"));
                }
            } else {
                iLogManagerService.logError(this.getClass(), programDesc, null, response.message());
            }
        } catch (Exception e) {
            iLogManagerService.logError(this.getClass(), programDesc, null, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);// 事务回滚
        }
    }

    @Override
    public void postToM3D(List<Pricerow> pricerowList) {
        // 价格行按照商品分组
        Map<Long, List<Pricerow>> priceGroups = pricerowList.stream().collect(Collectors.groupingBy(Pricerow::getProductId));
        JSONArray jsonArray = new JSONArray();

        for (Long productId : priceGroups.keySet()) {
            // 检查商品是否存在
            Product product = iProductService.selectByProductId(productId);
            if (product == null) {
                iLogManagerService.logTrace(this.getClass(), "订制品价格信息推送M3D", productId, "商品[" + productId + "]不存在");
                continue;
            }

            // 构造请求数据
            for (Pricerow pricerow : priceGroups.get(productId)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("CODE", product.getCode());
                jsonObject.put("PRICE_TYPE", pricerow.getPriceType());
                jsonObject.put("PRICE_LIST", pricerow.getPriceGroup());
                jsonObject.put("SALE_UNIT", pricerow.getSaleUnit());
                jsonObject.put("BASE_PRICE", pricerow.getBasePrice());
                jsonObject.put("PRODUCT_GRADE", pricerow.getProductGrade());
                jsonObject.put("ODTYPE", pricerow.getOdtype());
                if (pricerow.getStartTime() != null) {
                    jsonObject.put("START_TIME", pricerow.getStartTime().getTime());
                }
                if (pricerow.getEndTime() != null) {
                    jsonObject.put("END_TIME", pricerow.getEndTime().getTime());
                }
                jsonArray.add(jsonObject);
            }

        }

        if (jsonArray.isEmpty()) {
            iLogManagerService.logTrace(this.getClass(), "订制品价格信息推送M3D", null, "没有满足条件的支付信息");
            return ;
        }

        // 进行数据同步，成功后将同步标志改为'Y'
        try {
            Response response = restClient.postString(Constants.M3D, PRICE_URL, jsonArray.toString(), Constants.MINI_TYPE_JSON, null, null);
            if (response.code() == 200) {
                JSONObject responseObj = RestClient.responseToJSON(response);
                if (responseObj.getInt("err") == 200) {
                    for (Pricerow pricerow : pricerowList) {
                        pricerow.setSyncflag(Constants.YES);
                        this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), pricerow);
                    }
                } else {
                    throw new RuntimeException("定制品价格信息同步失败，" + responseObj.getString("errmsg"));
                }
            } else {
                throw new RuntimeException("定制品价格同步接口返回失败，code[" + response.code() + "]，message[" + response.message() + "]");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}