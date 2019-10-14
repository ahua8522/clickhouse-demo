package cn.com.clickhouse.service.imp;

import cn.com.clickhouse.mapper.IndicatorAnalysisMapper;
import cn.com.clickhouse.mapper.UisTestMapper;
import cn.com.clickhouse.service.IndicatorAnalysisBO;
import cn.com.clickhouse.uitil.Constants;
import cn.com.clickhouse.uitil.DateUtil;
import cn.com.clickhouse.uitil.ExtJSResponse;
import cn.com.clickhouse.uitil.ReportUtils;
import cn.com.clickhouse.vo.IntervalItme;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 指标监控分析
 * @author kuyi
 *
 */
@Component
@Slf4j
public class IndicatorAnalysisBOImpl implements IndicatorAnalysisBO {
	
	@Value("#{'${frms.monitor.indicator.empty.list:null}'.split(',')}")
	private  List<String> emptyStandard;
	@Autowired
	private UisTestMapper uisTestMapper;

	@Autowired
	private IndicatorAnalysisMapper indicatorAnalysisMapper;

	@Override
	public List<String> getIndicatorType(Map<String, Object> paramsMap) {
		List<String> list = indicatorAnalysisMapper.getIndicatorType(paramsMap);
		return list;
	}

	@Override
	public ExtJSResponse getNumberList(Map<String, Object> paramsMap) {
		TimeInterval interval = new TimeInterval();
		interval.start();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		int totalCnt = 0;
		int goodTagCnt = 0;
		int badTagCnt = 0;
		int emptyTagCnt = 0;
		int emptyNum = 0;
		double emptyPercent = 0d;
		
		try{
			List<Map<String,Object>> minMaxList = indicatorAnalysisMapper.getMinMaxValue(paramsMap);
			if(minMaxList!=null && minMaxList.size()>0){
				Map<String,Object> minMaxMap = minMaxList.get(0);
				double min = ReportUtils.getDoubleVal(minMaxMap.get("min"));
				double max = ReportUtils.getDoubleVal(minMaxMap.get("max"));
				List<IntervalItme> intervalItmes = new ArrayList<IntervalItme>();
				ReportUtils.getIntervalItmes(intervalItmes, min, max, 10);
				List<Map<String,Object>> datalist = new ArrayList<Map<String, Object>>();
				if(intervalItmes!=null && intervalItmes.size()>0){
					paramsMap.put("intervalItmes", intervalItmes);
					datalist = indicatorAnalysisMapper.getNumberList(paramsMap);
				}
				if(datalist!=null && datalist.size()>0){
					for(int i=0;i<datalist.size();i++){
						Map<String,Object> data = datalist.get(i);
						String intervalName = ReportUtils.getStringVal(data.get("intervalName"));
						int cnt = ReportUtils.getIntVal(data.get("cnt"));
						if(intervalName==null || "".equals(intervalName) || (emptyStandard!=null && emptyStandard.contains(intervalName))){
							emptyNum += cnt;
							totalCnt += cnt;
							datalist.remove(i);
							i--;
						}
					}
				}
				for (IntervalItme intervalItme : intervalItmes) {
					Map<String,Object> ind = new HashMap<String,Object>();
					ind.put("product", intervalItme.getIntervalName());
					if(datalist!=null && datalist.size()>0){
						for(int i=0;i<datalist.size();i++){
							Map<String,Object> data = datalist.get(i);
							if(intervalItme.getIntervalName().equals(ReportUtils.getStringVal(data.get("intervalName")))){
								int cnt = ReportUtils.getIntVal(data.get("cnt"));
								int badtag = ReportUtils.getIntVal(data.get("badtag"));
								int goodtag = ReportUtils.getIntVal(data.get("goodtag"));
								int emptytag = ReportUtils.getIntVal(data.get("emptytag"));
								totalCnt+=cnt;
								badTagCnt+=badtag;
								goodTagCnt+=goodtag;
								emptyTagCnt+=emptytag;
								ind.put("count1", badtag);//坏标签
								ind.put("count2", goodtag);//好标签
								ind.put("count3", emptytag);//空标签
								datalist.remove(i);
								break;
							}
						}
					}
					if(ind.get("count1")==null)
						ind.put("count1", 0);//坏标签
					if(ind.get("count2")==null)
						ind.put("count2", 0);//好标签
					if(ind.get("count3")==null)
						ind.put("count3", 0);//空标签
					result.add(ind);
				}
			}
//			emptyNum = totalCnt-badTagCnt-goodTagCnt;
			if(emptyNum<0)
				emptyNum=0;
			if(emptyNum>0 && totalCnt>0){
				emptyPercent = ((double)emptyNum)/((double)totalCnt)*100d;
			}
			ExtJSResponse response = ExtJSResponse.successResWithData(result);
			response.put("type", Constants.IndicatorAnalysisType.HISTORGRAM);
			response.put("totalNum", totalCnt);
			response.put("emptyNum", emptyNum);
			response.put("emptyPercent", emptyPercent);
			log.info("查询消耗时间：[{}]毫秒", interval.interval());
			return response;
		}catch(Exception e){
			e.printStackTrace();
			log.error("查询数值指标数据失败!");
			return ExtJSResponse.errorRes(e.getLocalizedMessage());
		}
	}
	
	@Override
	public ExtJSResponse getTimeList(Map<String, Object> paramsMap) {
		TimeInterval interval = new TimeInterval();
		interval.start();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		int totalCnt = 0;
		int goodTagCnt = 0;
		int badTagCnt = 0;
		int emptyTagCnt = 0;
		int emptyNum = 0;
		double emptyPercent = 0d;
		
		try{
			List<Map<String,Object>> minMaxList = indicatorAnalysisMapper.getMinMaxValue(paramsMap);
			if(minMaxList!=null && minMaxList.size()>0){
				Map<String,Object> minMaxMap = minMaxList.get(0);
				double min = ReportUtils.getDoubleVal(minMaxMap.get("min"));
				double max = ReportUtils.getDoubleVal(minMaxMap.get("max"));
				List<IntervalItme> intervalItmes = new ArrayList<IntervalItme>();
				ReportUtils.getIntervalItmes(intervalItmes, min, max, 10);
				List<Map<String,Object>> datalist = new ArrayList<Map<String, Object>>();
				if(intervalItmes!=null && intervalItmes.size()>0){
					paramsMap.put("intervalItmes", intervalItmes);
					datalist = indicatorAnalysisMapper.getNumberList(paramsMap);
				}
				if(datalist!=null && datalist.size()>0){
					for(int i=0;i<datalist.size();i++){
						Map<String,Object> data = datalist.get(i);
						String intervalName = ReportUtils.getStringVal(data.get("intervalName"));
						int cnt = ReportUtils.getIntVal(data.get("cnt"));
						if(intervalName==null || "".equals(intervalName) || (emptyStandard!=null && emptyStandard.contains(intervalName))){
							emptyNum += cnt;
							totalCnt += cnt;
							datalist.remove(i);
							i--;
						}
					}
				}
				for (IntervalItme intervalItme : intervalItmes) {
					Map<String,Object> ind = new HashMap<String,Object>();
					if(!StringUtils.isEmpty(intervalItme.getIntervalName())){
						try{
							Long intervalName = Long.parseLong(intervalItme.getIntervalName());
							ind.put("product", DateUtil.formateTime(new Date(intervalName),DateUtil.TYPE_DATE));
						}catch(NumberFormatException e){
							log.error("时间类型指标日期格式转换失败:{}",e);
						}
					}
					if(datalist!=null && datalist.size()>0){
						for(int i=0;i<datalist.size();i++){
							Map<String,Object> data = datalist.get(i);
							if(intervalItme.getIntervalName().equals(ReportUtils.getStringVal(data.get("intervalName")))){
								int cnt = ReportUtils.getIntVal(data.get("cnt"));
								int badtag = ReportUtils.getIntVal(data.get("badtag"));
								int goodtag = ReportUtils.getIntVal(data.get("goodtag"));
								int emptytag = ReportUtils.getIntVal(data.get("emptytag"));
								totalCnt+=cnt;
								badTagCnt+=badtag;
								goodTagCnt+=goodtag;
								emptyTagCnt+=emptytag;
								ind.put("count1", badtag);//坏标签
								ind.put("count2", goodtag);//好标签
								ind.put("count3", emptytag);//空标签
								datalist.remove(i);
								break;
							}
						}
					}
					if(ind.get("count1")==null)
						ind.put("count1", 0);//坏标签
					if(ind.get("count2")==null)
						ind.put("count2", 0);//好标签
					if(ind.get("count3")==null)
						ind.put("count3", 0);//空标签
					result.add(ind);
				}
			}
//			emptyNum = totalCnt-badTagCnt-goodTagCnt;
			if(emptyNum<0)
				emptyNum=0;
			if(emptyNum>0 && totalCnt>0){
				emptyPercent = ((double)emptyNum)/((double)totalCnt)*100d;
			}
			ExtJSResponse response = ExtJSResponse.successResWithData(result);
			response.put("type", Constants.IndicatorAnalysisType.HISTORGRAM);
			response.put("totalNum", totalCnt);
			response.put("emptyNum", emptyNum);
			response.put("emptyPercent", emptyPercent);
			log.info("查询消耗时间：[{}]毫秒", interval.interval());
			return response;
		}catch(Exception e){
			log.error("查询时间指标数据失败:{}",e);
			return ExtJSResponse.errorRes(e.getLocalizedMessage());
		}
	}
	
	@Override
	public ExtJSResponse getCharList(Map<String, Object> paramsMap) {
		TimeInterval interval = new TimeInterval();
		interval.start();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		int totalCnt = 0;
		int goodTagCnt = 0;
		int badTagCnt = 0;
		int emptyTagCnt = 0;
		int emptyNum = 0;
		double emptyPercent = 0d;
		String chartType = Constants.IndicatorAnalysisType.HISTORGRAM;
		
		try{
			List<Map<String,Object>> datalist = indicatorAnalysisMapper.getCharList(paramsMap);
			if(datalist!=null && datalist.size()>0){
				for(int i=0;i<datalist.size();i++){
					Map<String,Object> data = datalist.get(i);
					String intervalName = ReportUtils.getStringVal(data.get("indicatorValue"));
					int cnt = ReportUtils.getIntVal(data.get("cnt"));
					if(intervalName==null || "".equals(intervalName) || (emptyStandard!=null && emptyStandard.contains(intervalName))){
						emptyNum += cnt;
						totalCnt += cnt;
						datalist.remove(i);
						i--;
					}
				}
			}
			if(datalist==null){
				return ExtJSResponse.errorRes("指标数据为空");
			}else if(datalist.size()>20){//分布列表
				for (Map<String, Object> data : datalist) {
					Map<String,Object> ind = new HashMap<String,Object>();
					ind.put("product", ReportUtils.getStringVal(data.get("indicatorValue")).replaceAll("[\u0000]", ""));
					int cnt = ReportUtils.getIntVal(data.get("cnt"));
					int goodtag = ReportUtils.getIntVal(data.get("goodtag"));
					int badtag = ReportUtils.getIntVal(data.get("badtag"));
					int emptytag = ReportUtils.getIntVal(data.get("emptytag"));
					totalCnt+=cnt;
					goodTagCnt+=goodtag;
					badTagCnt+=badtag;
					emptyTagCnt+=emptytag;
					ind.put("count1", cnt);//数量
					ind.put("count2", goodtag);//好标签
					ind.put("count3", badtag);//坏标签
					ind.put("count6", emptytag);//空标签
					result.add(ind);
				}
				for (Map<String, Object> map : result) {
					int goodtag = ReportUtils.getIntVal(map.get("count2"));
					int badtag = ReportUtils.getIntVal(map.get("count3"));
					int emptytag = ReportUtils.getIntVal(map.get("count6"));
					if(totalCnt>0){
						map.put("count4", ((double)goodtag/(double)totalCnt)*100d);//好标签百分比
						map.put("count5", ((double)badtag/(double)totalCnt)*100d);//坏标签百分比
						map.put("count7", ((double)emptytag/(double)totalCnt)*100d);//空标签百分比
					}else{
						map.put("count4", 0);//好标签百分比
						map.put("count5", 0);//坏标签百分比
						map.put("count7", 0);//空标签百分比
					}
				}
				chartType = Constants.IndicatorAnalysisType.GRID;
			}else{//直方图
				for (Map<String, Object> data : datalist) {
					Map<String,Object> ind = new HashMap<String,Object>();
					ind.put("product", ReportUtils.getStringVal(data.get("indicatorValue")).replaceAll("[\u0000]", ""));
					int cnt = ReportUtils.getIntVal(data.get("cnt"));
					int badtag = ReportUtils.getIntVal(data.get("badtag"));
					int goodtag = ReportUtils.getIntVal(data.get("goodtag"));
					int emptytag = ReportUtils.getIntVal(data.get("emptytag"));
					totalCnt+=cnt;
					badTagCnt+=badtag;
					goodTagCnt+=goodtag;
					emptyTagCnt+=emptytag;
					ind.put("count1", badtag);//坏标签
					ind.put("count2", goodtag);//好标签
					ind.put("count3", emptytag);//空标签
					result.add(ind);
				}
			}
//			emptyNum = totalCnt-badTagCnt-goodTagCnt;
			if(emptyNum<0)
				emptyNum=0;
			if(emptyNum>0 && totalCnt>0){
				emptyPercent = ((double)emptyNum)/((double)totalCnt)*100d;
			}
			ExtJSResponse response = ExtJSResponse.successResWithData(result);
			response.put("type", chartType);
			response.put("totalNum", totalCnt);
			response.put("emptyNum", emptyNum);
			response.put("emptyPercent", emptyPercent);
			log.info("查询消耗时间：[{}]毫秒", interval.interval());
			return response;
		}catch(Exception e){
			e.printStackTrace();
			log.error("查询枚举指标数据失败!");
			return ExtJSResponse.errorRes(e.getLocalizedMessage());
		}
	}
	

}
