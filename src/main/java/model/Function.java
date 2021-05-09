package model;

import exceptions.UnsupportedFunctionException;

public enum Function {
	ABS,
	ANOMALY_DETECTION_BAND,
	AVG,
	CEIL,
	FILL,
	FIRST,
	LAST,
	FLOOR,
	IF,
	INSIGHT_RULE_METRIC,
	MAX,
	METRIC_COUNT,
	METRICS,
	MIN,
	PERIOD,
	RATE,
	REMOVE_EMPTY,
	SEARCH,
	SERVICE_QUOTA,
	SLICE,
	SORT,
	STDDEV,
	SUM,
	ADD("+"),
	SUBTRACT("-"),
	MULTIPLY("*"),
	DIVIDE("/"),
	POWER("^");
	
	public String value = null;
	
	private Function() {}
	
	private Function(String value) {
		this.value = value;
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
}