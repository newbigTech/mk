package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Subcarriage;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 支线运费对象的service接口
 * @date 2017/7/10 14:37
 */
public interface ISubcarriageService extends IBaseService<Subcarriage>, ProxySelf<ISubcarriageService> {


    /**
     * @param iRequest
     * @param dto
     * @param page
     * @param pageSize
     * @return
     * @description 支线运费维护界面查询
     */
    public List<Subcarriage> selectSubcarriage(IRequest iRequest, Subcarriage dto, int page, int pageSize);

    /**
     * @param dto
     * @return
     * @description 通过承运商编码+承运商类型+区编码确认唯一记录
     */
    public List<Subcarriage> selectUnique(Subcarriage dto);

    /**
     * @param request
     * @param list
     * @return
     * @description 导入支线运费时，校验数据的正确性
     */
    public Map<String,Object> checkSubcarriage(IRequest request, List<Subcarriage> list) throws InvocationTargetException, IllegalAccessException;
}