/**
 *
 * Copyright © 2010 杭州邦盛金融信息技术有限公司 版权所有
 *
 */

package cn.com.clickhouse.uitil;
/**
 * 常量定义. <br>
 * 时间:2016年4月18日 下午6:09:19 <br>
 * @author   hong
 * @version
 */


public interface Constants {
	
	interface ProcessInstanceStatus{
		public static final int RUNNING = 1;								//运行中
		public static final int EXCEPTION = 3;								//异常
		public static final int EXCEPTION_END = 2;							//异常结束(没有结束节点的结束)
		public static final int END = 9;									//正常结束
	}
	
	interface WorkflowEngineUrl{
		public static final String QUEERY_ACTIVITY_NAME = "/flow/getCurenntActivityNames";			//查询流程当前执行节点
		public static final String FLOW_IMG_URL = "/flow/getFlowImg";								//查询流程图URL
		public static final String IND_URL = "/dm/rulequery/indiInfo";								//查询指标
		public static final String RESTART_URL = "/flow/rerun";                                                //决策流重跑
		
		public static final String FLOW_MONITORVIEW_IMG_URL = "/flow/getFlowImgs";								//查询流程图URL
	}
	
	interface RiskType{
		public static final String GroupResult = "Group";			//决策树
		public static final String CreditResult = "Credit";			//评分卡
		public static final String MatrixResult = "Matrix";			//决策矩阵
		public static final String FormulaResult = "Formula";			//公式
	}
	
	interface NodeType{
		public static final String AUDIT = "audit";
		public static final String CREDIT = "credit";
		public static final String GROUP = "group";
		public static final String MATRIX = "matrix";
		public static final String FORMULA = "formula";
	}
	
	interface IsexistAction{
		public static final String RESTART = "restart";						//重跑
		public static final String RGAUDIT = "rgaudit";						//人工审核
	}
	
	interface IndicatorAnalysisType{
		public static final String HISTORGRAM = "historgram"; //直方图
		public static final String GRID = "grid"; //图表
	}
}