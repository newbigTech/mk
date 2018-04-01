package com.hand.hmall.process.engine;

/**
 * @author 马君
 * @version 0.1
 * @name AbstractProcessNode
 * @description 抽象流程节点
 * @date 2017/7/29 13:32
 */
public abstract class AbstractProcessNode<T> {

	// 节点权重，一个大于0的整数，节点权重标志了
	// 该节点在整个流程中的位置
	private int index;

	// 该节点是否可用，可以控制该节点是否被执行
	// use只代表该节点是否阶段性可用，不代表是否被废弃
	// index和use两个字段可以实现流程节点的"可拔插"效果
	private boolean use;

	// 流程服务类，封装了流程的所有业务逻辑
	// execute方法需要调用iProcessService来实现流程
	// 节点的业务逻辑
	private IProcessService<T> iProcessService;

	/**
	 * 流程节点的执行方法
	 * @param data 操作数据
	 * @return NodeResponse<T>
     */
	public abstract NodeResponse<T> execute(T data);

	/**
	 * 流程节点的错误处理方法
	 * @param data 流程节点的操作数据
	 * @param e 异常类
	 * @param message 错误消息
     */
	public abstract void errorHandle(T data, Exception e, String message);

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (index <= 0) 
			throw new IllegalArgumentException("illegal index");
		this.index = index;
	}

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}
	
}
