package com.hand.hmall.service;

import com.hand.hmall.dto.OmEdPromote;

import java.util.List;

/**
 * 事后促销
 */
public interface IOmEdPromoteService{

    public int insert(OmEdPromote omEdPromote);

    public int update(OmEdPromote omEdPromote);

    public OmEdPromote findByPrimaryKey(OmEdPromote omEdPromote);

    public List<OmEdPromote> queryCondition(OmEdPromote omEdPromote);

    public OmEdPromote findOne(OmEdPromote omEdPromote);

}