package cn.com.clickhouse.controller;

import cn.com.clickhouse.enums.IndicatorTypeEnum;
import cn.com.clickhouse.service.IndicatorAnalysisBO;
import cn.com.clickhouse.uitil.ExtJSResponse;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 指标监控分析Service
 * @author kuyi
 *
 */
@RestController
@RequestMapping("/rs/credit/indicator")
@Slf4j
public class IndicatorAnalysisController {
	
	
	@Autowired
	private IndicatorAnalysisBO indicatorAnalysisBO;
	
	/**
	 * 指标选择值组装
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/indlist" , method={RequestMethod.GET, RequestMethod.POST} )
	public ExtJSResponse indlist(String params){
		try{
			Map<String, Object> paramsMap = JSON.parseObject(params, Map.class);
			String indColumnName = "ind";//定义指标列名
			Map<String,String> column2nameMap = new HashMap<String,String>();//定义表头和指标名的对应关系
			Object indObject = paramsMap.get("inds");
			if(indObject != null){
				List<String> indicatornameList = (List<String>) indObject;
				for(int i=0;i<indicatornameList.size();i++){
					column2nameMap.put(indColumnName+i, indicatornameList.get(i));//组装对应关系
				}
			}
			ExtJSResponse response = ExtJSResponse.successResWithData(column2nameMap);
			return response;
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("查询指标选择数据失败!");
			return ExtJSResponse.errorRes(e.getLocalizedMessage());
		}
		
	}
	/**
	 * 指标直方图数据查询
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/indHistogramList" , method={RequestMethod.GET, RequestMethod.POST} )
	public ExtJSResponse indHistogramList(@RequestBody Map<String, Object> map){
		try{
			TimeInterval interval = new TimeInterval();
			Map<String, Object> paramsMap = (Map<String, Object>) map.get("params");
			String winowType = "historgram";//暂定historgram、grid
//			int totalNum = 0;//总量
//			int emptyNum = 100;//空值数
//			double emptyPercent = 0d;//缺失率
			List<String> typelist = null;
			Object indnameObj = paramsMap.get("indname");
			if(indnameObj!=null){
				String indname = indnameObj.toString().replace("<a href=\"javascript:void(0);\">", "").replace("<a>", "");
				paramsMap.put("indname", indname);
				typelist = indicatorAnalysisBO.getIndicatorType(paramsMap);
				if(typelist==null || typelist.size()==0){
					return ExtJSResponse.errorRes("该指标类型或指标数据为空");
				}else if(typelist.size()>1){
					return ExtJSResponse.errorRes("该指标类型存在多个");
				}
				if(IndicatorTypeEnum.TIME.getCode().equals(typelist.get(0))){
					log.debug("IndicatorTypeEnum:{}",IndicatorTypeEnum.TIME);
					return indicatorAnalysisBO.getTimeList(paramsMap);
				}else if(IndicatorTypeEnum.NUMBER.getCode().equals(typelist.get(0))){
					log.debug("IndicatorTypeEnum:{}",IndicatorTypeEnum.NUMBER);
					return indicatorAnalysisBO.getNumberList(paramsMap);
				}else if(IndicatorTypeEnum.CHAR.getCode().equals(typelist.get(0))){
					log.debug("IndicatorTypeEnum:{}",IndicatorTypeEnum.CHAR);
					return indicatorAnalysisBO.getCharList(paramsMap);
				}else if(IndicatorTypeEnum.BOOLEAN.getCode().equals(typelist.get(0))){
					log.debug("IndicatorTypeEnum:{}",IndicatorTypeEnum.BOOLEAN);
					return indicatorAnalysisBO.getCharList(paramsMap);
				}else if(IndicatorTypeEnum.ARRAY.getCode().equals(typelist.get(0))){
					log.debug("IndicatorTypeEnum:{}",IndicatorTypeEnum.ARRAY);
				}
			}else{
				return ExtJSResponse.errorRes("请选择指标名称");
			}
			return ExtJSResponse.errorRes("该指标类型不支持："+ IndicatorTypeEnum.getTextByCode(typelist.get(0)));
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("查询指标数据失败!");
			return ExtJSResponse.errorRes(e.getLocalizedMessage());
		}
		
	}
}
