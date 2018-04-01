package com.hand.hmall.validator;

import com.hand.hmall.pojo.PointOfServiceData;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author 马君
 * @version 0.1
 * @name PointOfServiceDataValidator
 * @description PointOfServiceDataValidator
 * @date 2017/6/9 10:59
 */

@Component
public class PointOfServiceDataValidator implements Validator<PointOfServiceData> {

    @Override
    public ValidateResult validator(PointOfServiceData pointOfServiceData) {
        ValidateResult validateResult = new ValidateResult();
        validateResult.setValid(false);

        // 服务点编号
        if (StringUtils.isBlank(pointOfServiceData.getCode())) {
            validateResult.setMessage("服务点编码不能为空");
            return validateResult;
        }

        // 服务点名称
        if (StringUtils.isBlank(pointOfServiceData.getDisplayname())) {
            validateResult.setMessage("服务点名称不能为空");
            return validateResult;
        }

        // 服务点类型
        if (StringUtils.isBlank(pointOfServiceData.getType())) {
            validateResult.setMessage("服务点类型不能为空");
            return validateResult;
        } else {
            if (!Arrays.asList("WAREHOUSE", "POINTOFSERVICE", "STORE")
                    .contains(pointOfServiceData.getType())) {
                validateResult.setMessage("服务点类型非法（WAREHOUSE, POINTOFSERVICE, STORE）");
                return validateResult;
            }
        }

        // 服务点状态
        if (StringUtils.isNotBlank(pointOfServiceData.getShopstatus())) {
            if (!Arrays.asList("10", "20").contains(pointOfServiceData.getShopstatus())) {
                validateResult.setMessage("服务点状态非法（10, 20）");
                return validateResult;
            }
        }

        validateResult.setValid(true);
        return validateResult;
    }
}
