package com.hand.promotion.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 抽奖工具包
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class LotteryUtil {
	/**
	 * 抽奖
	 *
	 * @param rates 概率列表，保证顺序和实际物品对应
	 * @return 物品的索引
	 */
	public static int lottery(List<Double> rates) {
		if (rates == null || rates.isEmpty()) {
			return -1;
		}
		int size = rates.size();
		// 计算总概率，这样可以保证不一定总概率是1
		double sumRate = 0d;
		for (double rate : rates) {
			sumRate += rate;
		}
		// 计算每个物品在总概率的基础下的概率情况
		List<Double> sortRates = new ArrayList<Double>(size);
		Double tempSumRate = 0d;
		for (double rate : rates) {
			tempSumRate += rate;
			sortRates.add(tempSumRate / sumRate);
		}
		// 根据区块值来获取抽取到的物品索引
		double nextDouble = Math.random();
		sortRates.add(nextDouble);
		Collections.sort(sortRates);
		return sortRates.indexOf(nextDouble);
	}
}
