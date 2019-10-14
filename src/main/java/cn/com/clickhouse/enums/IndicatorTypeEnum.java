package cn.com.clickhouse.enums;


public enum IndicatorTypeEnum{
	TIME("T","时间"),
	NUMBER("N","数值"),
	CHAR("C","字符"),
	BOOLEAN("B","布尔类型"),
	ARRAY("A","集合类型");
	private String code;
	private String text;
	private IndicatorTypeEnum(String code,String text){
		this.code = code;
		this.text = text;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public static String getTextByCode (String code) {
		for(IndicatorTypeEnum indicatorTypeEnum : IndicatorTypeEnum.values()) {
			if(indicatorTypeEnum.code.equals(code)) {
				return indicatorTypeEnum.text;
			}
		}
		return code;
	}
	

}
