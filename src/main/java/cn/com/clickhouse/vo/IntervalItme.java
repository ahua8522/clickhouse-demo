package cn.com.clickhouse.vo;


public class IntervalItme {

	private Object startScore;
	
	private Object endScore;
	
	private String startSymbol;
	
	private String endSymbol;
	
	private String intervalName;

	
	public IntervalItme() {
		super();
	}

	public IntervalItme(Object startScore, Object endScore, String startSymbol,
                        String endSymbol) {
		super();
		this.startScore = startScore;
		this.endScore = endScore;
		this.startSymbol = startSymbol;
		this.endSymbol = endSymbol;
	}
	
	

	public IntervalItme(Object startScore, Object endScore, String startSymbol,
                        String endSymbol, String intervalName) {
		super();
		this.startScore = startScore;
		this.endScore = endScore;
		this.startSymbol = startSymbol;
		this.endSymbol = endSymbol;
		this.intervalName = intervalName;
	}

	public String getIntervalName() {
		return intervalName;
	}

	public void setIntervalName(String intervalName) {
		this.intervalName = intervalName;
	}

	public Object getStartScore() {
		return startScore;
	}

	public void setStartScore(Object startScore) {
		this.startScore = startScore;
	}

	public Object getEndScore() {
		return endScore;
	}

	public void setEndScore(Object endScore) {
		this.endScore = endScore;
	}

	public String getStartSymbol() {
		return startSymbol;
	}

	public void setStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}

	public String getEndSymbol() {
		return endSymbol;
	}

	public void setEndSymbol(String endSymbol) {
		this.endSymbol = endSymbol;
	}

	
	
}
