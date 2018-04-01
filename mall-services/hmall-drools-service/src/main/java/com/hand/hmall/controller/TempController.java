package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IRuleTempService;
import com.hand.hmall.service.ISaleActivityService;
import com.hand.hmall.temp.ActionTemp;
import com.hand.hmall.temp.Field;
import com.hand.hmall.temp.ModelTemp;
import com.hand.hmall.temp.RuleInputTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 操作redis中的促销脚本数据
 * 未使用
 */
@RestController
@RequestMapping("/h")
public class TempController {
    @Autowired
    private IRuleTempService ruleTempService;
    @Autowired
    private ISaleActivityService saleActivityService;

    /**
     * 新增model数据
     *
     * @param model
     * @return
     */
    @PostMapping("/insert/model")
    public Map insertModel(@RequestBody ModelTemp model){
        return ruleTempService.addModel(model);
    }

    /**
     * 新增definitions数据
     * @param definitions
     * @return
     */
    @PostMapping("/insert/definition")
    public Map insertModel(@RequestBody List<Map> definitions){
        for (Map definition: definitions){
            ruleTempService.addDefinition(definition);
        }
        return null;
    }

    /**
     * 新增action数据
     * @param actionTemp
     * @return
     */
    @PostMapping("/insert/action")
    public Map insertAction(@RequestBody ActionTemp actionTemp){
        return ruleTempService.addAction(actionTemp);
    }

    /**
     * 新增分组数据
     * @param group
     * @return
     */
    @PostMapping("/insert/group")
    public Map insertGroup(@RequestBody Map group){
        return ruleTempService.addGroup(group);
    }

    /**
     * 新增Field数据
     * @param field
     * @return
     */
    @PostMapping("/insert/field")
    public Map insertField(@RequestBody Field field){
        return ruleTempService.insertField(field);
    }

    /**
     * 生成促销规则
     * @param ruleInputTemp
     * @return
     */
    @PostMapping("/create/rule")
    public ResponseData createRule(@RequestBody RuleInputTemp ruleInputTemp) {
        try {
            return ruleTempService.createRule(ruleInputTemp);
        }catch (NullPointerException e){
            return new ResponseData(false,"NULL_POINTER");
        }catch (IllegalArgumentException | ClassCastException e){
            return new ResponseData(false,"ERROR_DATA");
        }catch(InvocationTargetException | IllegalAccessException e){
            return new ResponseData(false,"SYSTEM_ERROR");
        }
    }

    /**
     * 重新打包优惠券规则的jar包
     * @param couponId
     * @return
     */
    @PostMapping("/create/kjar/{couponId}")
    public ResponseData createJar(@PathVariable String couponId){
        ruleTempService.releaseCoupon(couponId);
        return new ResponseData();
    }

    /**
     * 移除优惠券的jar包
     * @param couponId
     * @return
     */
    @PostMapping("/remove/kjar/{couponId}")
    public ResponseData removeJar(@PathVariable String couponId){
        ruleTempService.removeCoupon(couponId);
        return new ResponseData();
    }

//    @PostMapping("/release/activity")
//    public ResponseData releaseActivity(){
//        ruleTempService.releaseActivity(saleActivityService.selectByStatusAndIsUsing(Status.ACTIVITY.getValue(),"Y"));
//        return new ResponseData();
//    }
}
