package com.hand.promotion.job;

import com.hand.promotion.pojo.activity.SelectConditionActionPojo;
import com.hand.promotion.service.ISelectConditionActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description 启动加载condition、action
 */
@Component
public class InitSelectConditionActionJob extends BaseJob {


    @Value("${application.job.initSelectConditionAction}")
    private boolean start;
    @Autowired
    private ISelectConditionActionService selectConditionActionService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doExecute() {
        if (start) {
            logger.info("-----------开始加载SelectConditionAction----------");
            initAction();
            initCondition();
            logger.info("-----------加载完成----------");

        } else {
            logger.info("-----start为{}------ 不加载SelectConditionAction----------", start);

        }


    }

    private void initAction() {
        SelectConditionActionPojo selectConditionActionPojo = new SelectConditionActionPojo();
        int i = 1;

        selectConditionActionPojo.setDefinitionId("o_total_discount");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("订单减X元");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("oe_total_discount");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("订单行减X元");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_total_rate");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("订单打x折");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("oe_total_rate");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("订单行打x折");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_freight_waiver");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("运费减免");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_giver_product");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("赠品");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_fixed_number");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("商品固定价格");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_fixed_rate");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("商品固定折扣");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_meet_delete");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("订单每满X元减Y元");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("oe_meet_delete");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("订单行每满X元减Y元");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_target_price");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("目标包价格");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("p_number_discount");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("X件商品固定金额");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("p_number_rate");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("X件商品打Y折");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);
        logger.info(">>>>>>>>>>>>>>{}个结果加载完成",i);

    }

    private void initCondition() {
        int i = 1;

        SelectConditionActionPojo selectConditionActionPojo = new SelectConditionActionPojo();


        selectConditionActionPojo.setDefinitionId("o_total_reached");
        selectConditionActionPojo.setCode("ADD_CONDITIONS");
        selectConditionActionPojo.setMeaning("订单满X元");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("oe_total_reached");
        selectConditionActionPojo.setCode("ADD_CONDITIONS");
        selectConditionActionPojo.setMeaning("订单行满X元");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_quantity_reached");
        selectConditionActionPojo.setCode("ADD_CONDITIONS");
        selectConditionActionPojo.setMeaning("订单满X件");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("ALL");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_product_range");
        selectConditionActionPojo.setCode("ADD_CONDITIONS");
        selectConditionActionPojo.setMeaning("商品范围");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_type_range");
        selectConditionActionPojo.setCode("ADD_CONDITIONS");
        selectConditionActionPojo.setMeaning("类别范围");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_area_range");
        selectConditionActionPojo.setCode("ADD_CONDITIONS");
        selectConditionActionPojo.setMeaning("地区范围");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("ALL");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("GROUP");
        selectConditionActionPojo.setCode("ADD_CONDITIONS");
        selectConditionActionPojo.setMeaning("组");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("CONTAINER");
        selectConditionActionPojo.setCode("ADD_CONDITIONS");
        selectConditionActionPojo.setMeaning("容器");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_total_reached");
        selectConditionActionPojo.setCode("ADD_GROUPS");
        selectConditionActionPojo.setMeaning("订单满X元");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("ALL");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("oe_total_reached");
        selectConditionActionPojo.setCode("ADD_GROUPS");
        selectConditionActionPojo.setMeaning("订单行满X元");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);
//


        selectConditionActionPojo.setDefinitionId("o_quantity_reached");
        selectConditionActionPojo.setCode("ADD_GROUPS");
        selectConditionActionPojo.setMeaning("订单满X件");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("ALL");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_product_range");
        selectConditionActionPojo.setCode("ADD_GROUPS");
        selectConditionActionPojo.setMeaning("商品范围");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("2");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);
//


        selectConditionActionPojo.setDefinitionId("o_type_range");
        selectConditionActionPojo.setCode("ADD_GROUPS");
        selectConditionActionPojo.setMeaning("类别范围");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("ALL");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_product_range");
        selectConditionActionPojo.setCode("ADD_CONTAINERS");
        selectConditionActionPojo.setMeaning("商品范围");
        selectConditionActionPojo.setType("ALL");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);


        selectConditionActionPojo.setDefinitionId("o_discount_ladders");
        selectConditionActionPojo.setCode("ADD_ACTIONS");
        selectConditionActionPojo.setMeaning("订单阶梯折扣");
        selectConditionActionPojo.setType("ACTIVITY");
        selectConditionActionPojo.setPriority(i);
        selectConditionActionPojo.setLevel("1");
        i++;
        selectConditionActionService.upsertByDfId(selectConditionActionPojo);
        logger.info(">>>>>>>>>>>>>>{}个条件加载完成",i);


    }
}
