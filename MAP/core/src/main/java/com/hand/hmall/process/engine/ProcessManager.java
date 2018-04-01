package com.hand.hmall.process.engine;

import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.List;

public class ProcessManager<T> implements InitializingBean {

	List<AbstractProcessNode<T>> processNodeList;

	public List<AbstractProcessNode<T>> getProcessNodeList() {
		return processNodeList;
	}

	public void setProcessNodeList(List<AbstractProcessNode<T>> processNodeList) {
		this.processNodeList = processNodeList;
	}
	
	public void start(T data) {
		start(data, 0);
	}
	
	public void start(T firstData, int index) {
		for (AbstractProcessNode<T> processNode : processNodeList) {

			// 检查流程节点的权重是否合法
			if (processNode.getIndex() <= 0) 
				throw new RuntimeException("error index");

			// 跳过无需执行的流程节点，当GOTO时会出现
			if (processNode.getIndex() < index || !processNode.isUse())
				continue;

			// 当状态为PASS是会使用firstData遍历所有流程节点
			NodeResponse<T> response = processNode.execute(firstData);
			// 获取流程节点的携带数据
			List<T> datas = response.getDatas();

			// 根据状态控制流程走向
			if (NodeResponse.RETRY.equals(response.getStatus())) {
				datas.stream().forEach(data -> start(data));
			} else if (NodeResponse.GOTO.equals(response.getStatus())) {
				datas.stream().forEach(data -> start(data, getNodeIndex(response.getGotoClass())));
				return;
			} else if (NodeResponse.FAIL.equals(response.getStatus())) {
				return;
			}
		}
	}

	/**
	 * 根据目标节点的实现类来确定流程节点的权重
	 * @param gotoClass 目标流程节点实现类
	 * @return int
     */
	private int getNodeIndex(Class<? extends AbstractProcessNode<T>> gotoClass) {
		AbstractProcessNode<T> gotoNode = processNodeList.stream()
				.filter(processNode -> processNode.getClass().equals(gotoClass))
				.findFirst().orElse(null);
		if (gotoNode == null) {
			throw new RuntimeException("error gotoClass");
		}
		return gotoNode.getIndex();
	}

	/**
	 * 对流程节点按照权重进行排序
	 * @throws Exception
     */
	@Override
	public void afterPropertiesSet() throws Exception {
		Collections.sort(processNodeList, (node1, node2)
				-> Integer.valueOf(node1.getIndex()).compareTo(Integer.valueOf(node2.getIndex())));
	}
}
