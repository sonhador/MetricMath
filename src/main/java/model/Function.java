package model;

import exceptions.UnsupportedFunctionException;

public enum Function {
	ABS(1),
//	ANOMALY_DETECTION_BAND,
	AVG(1),
	CEIL(1),
//	FILL,
	FIRST(1),
	LAST(1),
	FLOOR(1),
	IF(3),
//	INSIGHT_RULE_METRIC,
	MAX(1),
	METRIC_COUNT(1),
//	METRICS(2),
	MIN(1),
//	PERIOD,
//	RATE,
//	REMOVE_EMPTY,
//	SEARCH,
//	SERVICE_QUOTA,
	SLICE(3),
	SORT(3),
	STDDEV(1),
	SUM(1),
	ADD("+", 2),
	SUBTRACT("-", 2),
	MULTIPLY("*", 2),
	DIVIDE("/", 2),
	POWER("^", 2);
	
	public String value = null;
	public int argc = 0;
	
	private Function() {}
	
	private Function(String value, int argc) {
		this.value = value;
		this.argc = argc;
	}
	
	private Function(int argc) {
		this.argc = argc;
	}
	
	private boolean equals(String value) {
		if (this.value == null) {
			return value.equals(name());
		} else {
			return value.equals(this.value);
		}
	}
	
	public static Function getOperator(String function) {
		for (Function func : values()) {
			if (func.equals(function)) {
				return func;
			}
		}
		
		throw new UnsupportedFunctionException(function);
	}
	
	public int getParamCnt() {
		return argc;
	}
}