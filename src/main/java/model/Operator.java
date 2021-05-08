package model;

import exceptions.NoMatchingOperatorFoundException;

public enum Operator {
	EqualTo("=="),
	NotEqualTo("!="),
	LessThanOrEqualTo("<="),
	GreaterThanOrEqualTo(">="),
	LessThan("<"),
	GreaterThan(">"),
	AND("AND", "&&"),
	OR("OR", "||");
	
	public final String value1;
	public final String value2;
	
	private Operator(String value) {
		this.value1 = value;
		this.value2 = null;
	}
	
	private Operator(String value1, String value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	
	private boolean equals(String value) {
		if (this.value2 == null) {
			return value.equals(this.value1);
		} else {
			return value.equals(this.value1) || value.equals(this.value2);
		}
	}
	
	public static Operator getOperator(String operator) {
		for (Operator op : values()) {
			if (op.equals(operator)) {
				return op;
			}
		}
		
		throw new NoMatchingOperatorFoundException(operator);
	}
}
