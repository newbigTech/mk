package com.hand.hmall.service;

import com.hand.hmall.model.VCodeLine;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IVCodeLineService
 * @description VCodeLine Service
 * @date 2017/7/7 8:56
 */
public interface IVCodeLineService extends IBaseService<VCodeLine> {

    /**
     * 通过headerId查询
     * @param headerId vCodeHeader的id
     * @return List<VCodeLine>
     */
    List<VCodeLine> selectByHeaderId(Long headerId);
}
