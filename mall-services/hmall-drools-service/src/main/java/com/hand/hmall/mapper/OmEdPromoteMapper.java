package com.hand.hmall.mapper;

import com.hand.hmall.dto.OmEdPromote;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OmEdPromoteMapper extends Mapper<OmEdPromote> {

    public List<OmEdPromote> queryCondition(OmEdPromote omEdPromote);
}