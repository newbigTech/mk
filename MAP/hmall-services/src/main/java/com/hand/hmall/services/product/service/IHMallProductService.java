package com.hand.hmall.services.product.service;

import com.hand.hmall.services.product.dto.Product;

import java.util.List;

/**
 * @author alaowan
 * Created at 2017/12/26 13:47
 * @description
 */
public interface IHMallProductService {

    /**
     * 根据V码获取商品（主推款）信息
     *
     * @param vcode
     * @return
     */
    List<Product> getProductByVCode(String vcode);
}
