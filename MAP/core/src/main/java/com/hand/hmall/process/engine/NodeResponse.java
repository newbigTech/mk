package com.hand.hmall.process.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name NodeResponse
 * @description 流程节点返回结果
 * @date 2017/7/29 12:30
 */
public class NodeResponse<T> {

	public enum Status {
		RETRY, // 流程将会重新回到起点
		GOTO,  // 跳转到指定的流程节点
		PASS,  // 流程节点执行成功，顺序执行下一个节点
		FAIL   // 流程节点执行失败，中止流程
	}
	
	public static final Status RETRY = Status.RETRY;
	public static final Status GOTO = Status.GOTO;
	public static final Status PASS = Status.PASS;
	public static final Status FAIL = Status.FAIL;

	// 流程节点执行状态
	private Status status;

	// 流程节点执行后的携带数据
	// 该数据只在status为RETRY、GOTO时使用
	// 例如：发货单consignment1执行拆单流程，成功拆出consignment2、consignment3
	// 两张发货单，consignment1会被设置成"拆单关闭"将不再继续流程
	// consignment2、consignment3会直接跳到"承运商选择"流程节点
	// 此时，status为GOTO，datas为[consignment1, consignment2]
	private List<T> datas = new ArrayList<T>();

	// 目标流程节点的类型，只在status为GOTO是有效
	private Class<? extends AbstractProcessNode<T>> gotoClass;

	/**
	 * 构造不携带数据的Response，PASS和FAIL时使用
	 * @param status PASS或FAIL
     */
	public NodeResponse(Status status) {
		this.status = status;
	}

	/**
	 * 构造无需目标节点的Response，即RETRY
	 * @param status RETRY
	 * @param data 携带数据
     */
	public NodeResponse(Status status, T data) {
		this.status = status;
		this.datas.add(data);
	}

	/**
	 * 构造无需目标节点的Response，即RETRY
	 * @param status RETRY
	 * @param datas 携带数据
	 */
	public NodeResponse(Status status, List<T> datas) {
		this.status = status;
		this.datas.addAll(datas);
	}

	/**
	 * 构造GOTO的Response
	 * @param status GOTO
	 * @param data 携带数据
	 * @param gotoClass 目标节点
     */
	public NodeResponse(Status status, T data, Class<? extends AbstractProcessNode<T>> gotoClass) {
		this.status = status;
		this.datas.add(data);
		this.gotoClass = gotoClass;
	}

	/**
	 * 构造GOTO的Response
	 * @param status GOTO
	 * @param datas 携带数据
	 * @param gotoClass 目标节点
	 */
	public NodeResponse(Status status, List<T> datas, Class<? extends AbstractProcessNode<T>> gotoClass) {
		this.status = status;
		this.datas.addAll(datas);
		this.gotoClass = gotoClass;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public List<T> getDatas() {
		return this.datas;
	}
	
	public Class<? extends AbstractProcessNode<T>> getGotoClass() {
		return this.gotoClass;
	}
}
