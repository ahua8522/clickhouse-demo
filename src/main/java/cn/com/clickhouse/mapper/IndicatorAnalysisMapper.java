package cn.com.clickhouse.mapper;

import java.util.List;
import java.util.Map;

public interface IndicatorAnalysisMapper {
    /**
     * 获取指标类型
     * @param paramsMap
     * @return
     */
    public List<String> getIndicatorType(Map<String, Object> paramsMap);
    /**
     * 获取数值指标最小值、最大值
     * @param paramsMap
     * @return
     */
    public List<Map<String, Object>> getMinMaxValue(Map<String, Object> paramsMap);
    /**
     * 获取数值指标明细数据
     * @param paramsMap
     * @return
     */
    public List<Map<String, Object>> getNumberList(Map<String, Object> paramsMap);
    /**
     * 获取枚举指标明细数据
     * @param paramsMap
     * @return
     */
    public List<Map<String, Object>> getCharList(Map<String, Object> paramsMap);
}
