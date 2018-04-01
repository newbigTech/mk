package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Maincarriage;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 主干运费对象的service接口
 * @date 2017/7/10 14:37
 */
public interface IMaincarriageService extends IBaseService<Maincarriage>, ProxySelf<IMaincarriageService> {

    /**
     * @param iRequest
     * @param dto
     * @param page
     * @param pageSize
     * @return
     * @description 主干运费界面查询
     */
    public List<Maincarriage> selectMaincarriage(IRequest iRequest, Maincarriage dto, int page, int pageSize);

    /**
     * @param dto
     * @return
     * @description 通过承运商编码+承运商类型+区编码确认唯一记录
     */
    public List<Maincarriage> selectUnique(Maincarriage dto);

    /**
     * @param iRequest
     * @param list
     * @return
     * @description 导入主干运费时，验证数据的正确性
     */
    public Map<String,Object> checkMaincarriage(IRequest iRequest, List<Maincarriage> list) throws InvocationTargetException, IllegalAccessException;
}