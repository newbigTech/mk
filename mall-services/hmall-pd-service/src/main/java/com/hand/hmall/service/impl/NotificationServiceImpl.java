package com.hand.hmall.service.impl;

import com.hand.hmall.common.Constants;
import com.hand.hmall.model.Notification;
import com.hand.hmall.model.Product;
import com.hand.hmall.service.INotificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name NotificationServiceImpl
 * @description 通知Service实现类
 * @date 2017/10/24 16:49
 */
@Service
public class NotificationServiceImpl extends BaseServiceImpl<Notification> implements INotificationService{

    @Override
    public void addProductNewNotice(Product product) {
        Notification notification = new Notification();
        notification.setNotificationContent("有新的商品" + product.getCode() + "推送到中台");
        notification.setNotificationStatus(Constants.NOTICE_STATUS_PENDDING);
        notification.setNotificationTime(new Date());
        notification.setNotificationType(Constants.NOTICE_TYPE_PRODUCT_NEW);
        notification.setRelatedDataid(product.getProductId());
        this.insertSelective(notification);
    }

    @Override
    public void addProductChangeNotice(Product target, Product source) {

        String content = "";

        if (target.getName() != null) {
            if (!target.getName().equals(StringUtils.defaultString(source.getName()))) {
                content += "商品" + source.getCode() + "中文名称由" + StringUtils.defaultString(source.getName(), "空")
                        + "变更为" + (StringUtils.isBlank(target.getName()) ? "空" : target.getName());
            }
        }

        if (target.getNameEn() != null) {
            if (!target.getNameEn().equals(StringUtils.defaultString(source.getNameEn()))) {
                if (StringUtils.isEmpty(content)) {
                    content += "商品" + source.getCode() + "英文名称由" + StringUtils.defaultString(source.getNameEn(), "空")
                            + "变更为" + (StringUtils.isBlank(target.getNameEn()) ? "空" : target.getNameEn());
                } else {
                    content += ",英文名称由" + StringUtils.defaultString(source.getNameEn(), "空")
                            + "变更为" + (StringUtils.isBlank(target.getNameEn()) ? "空" : target.getNameEn());
                }
            }
        }

        if (!content.equals("")) {
            Notification notification = new Notification();
            notification.setNotificationContent(content);
            notification.setNotificationStatus(Constants.NOTICE_STATUS_PENDDING);
            notification.setNotificationTime(new Date());
            notification.setNotificationType(Constants.NOTICE_TYPE_PRODUCT_CHANGE);
            notification.setRelatedDataid(source.getProductId());
            this.insertSelective(notification);
        }
    }

    private String ifEmpty(String str, String to) {
        return StringUtils.isBlank(str) ? "空" : str;
    }
}
