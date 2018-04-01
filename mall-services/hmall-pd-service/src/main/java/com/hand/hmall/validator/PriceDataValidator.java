package com.hand.hmall.validator;

import com.hand.hmall.model.Product;
import com.hand.hmall.pojo.PriceData;
import com.hand.hmall.pojo.PriceRowData;
import com.hand.hmall.service.IProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 马君
 * @version 0.1
 * @name PriceDataValidator
 * @description PriceDataValidator
 * @date 2017/7/7 11:03
 */
@Component
public class PriceDataValidator implements Validator<PriceData> {

    @Autowired
    private IProductService iProductService;

    @Override
    public ValidateResult validate(PriceData priceData) {
        ValidateResult validateResult = new ValidateResult();
        validateResult.setValid(false);
        if (StringUtils.isBlank(priceData.getMainProduct())) {
            validateResult.setMessage("平台号不能为空");
            return validateResult;
        } else {
            Product product = iProductService.selectMarkorOnlineByCode(priceData.getMainProduct());
            if (product == null) {
                validateResult.setMessage("平台号[" + priceData.getMainProduct() + "]不存在");
                return validateResult;
            }
        }

        for (PriceRowData priceRowData : priceData.getPartList()) {
            if (StringUtils.isBlank(priceRowData.getPartCode())) {
                validateResult.setMessage("零部件编号不能为空");
                return validateResult;
            }
            if (priceRowData.getQuantity() == null) {
                validateResult.setMessage("零部件数量不能为空");
                return validateResult;
            }
        }

        validateResult.setValid(true);
        validateResult.setMessage("验证通过");

        return validateResult;
    }
}
