package cn.com.clickhouse.service;

import cn.com.clickhouse.uitil.ExtJSResponse;

import java.util.List;
import java.util.Map;

public interface IndicatorAnalysisBO {
	
	/**
	 * 获取指标类型
	 * @param paramsMap
	 * @return
	 */
	public List<String> getIndicatorType(Map<String, Object> paramsMap);
	/**
	 * 获取数值指标明细数据
	 * @param paramsMap
	 * @return
	 */
	public ExtJSResponse getNumberList(Map<String, Object> paramsMap);
	/**
	 * 获取时间指标明细数据（时间戳）
	 * @param paramsMap
	 * @return
	 */
	public ExtJSResponse getTimeList(Map<String, Object> paramsMap);
	/**
	/**
	 * 
	 * 获取枚举指标明细数据
	 * @param paramsMap
	 * @return
	 */
	public ExtJSResponse getCharList(Map<String, Object> paramsMap);

}
