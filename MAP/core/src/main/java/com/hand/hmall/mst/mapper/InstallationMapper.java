package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Installation;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 安装费对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface InstallationMapper extends Mapper<Installation> {

    /**
     * @param dto
     * @return
     * @description 安装费维护界面查询功能
     */
    List<Installation> selectInstallation(Installation dto);

}