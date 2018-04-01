package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@ExtensionAttribute(disable=true)
@Table(name = "HMALL_OM_ABNORMALTYPE")
public class AbnormalType extends BaseDTO {
    /**
     * 主键
     */
	@Id
	@GeneratedValue(generator = GENERATOR_TYPE)
    private Long abnormaltypeId;

    /**
     * 异常判定类型
     */
    private String abnormalType;

    /**
     * 异常判定描述
     */
    private String description;

    /**
     * 审核次数
     */
    private int approvedtimes;

    /**
     * 异常原因
     */
    private String abnormalreason;

    /**
     * 是否启用(Y/N)
     */
    private String active;

	private String parameter;

	public Long getAbnormaltypeId() {
		return abnormaltypeId;
	}

	public void setAbnormaltypeId(Long abnormaltypeId) {
		this.abnormaltypeId = abnormaltypeId;
	}

	public String getAbnormalType() {
		return abnormalType;
	}

	public void setAbnormalType(String abnormalType) {
		this.abnormalType = abnormalType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getApprovedtimes() {
		return approvedtimes;
	}

	public void setApprovedtimes(int approvedtimes) {
		this.approvedtimes = approvedtimes;
	}

	public String getAbnormalreason() {
		return abnormalreason;
	}

	public void setAbnormalreason(String abnormalreason) {
		this.abnormalreason = abnormalreason;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}