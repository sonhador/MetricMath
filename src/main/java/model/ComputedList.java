package model;

public class ComputedList implements Comparable<ComputedList> {
	private Object computedValue;
	private Object computeList;
	private SortOrder sortOrder;
	
	public ComputedList(Object computeList, Object computedValue, SortOrder sortOrder) {
		this.computeList = computeList;
		this.computedValue = computedValue;
		this.sortOrder = sortOrder;
	}
	
	@Override
	public int compareTo(ComputedList o) {
		if (Calc.condition(computedValue, Operator.GreaterThan, o.getComputedValue())) {
			return sortOrder == SortOrder.ASC ? 1 : -1; 
		} else if (Calc.condition(computedValue, Operator.LessThan, o.getComputedValue())) {
			return sortOrder == SortOrder.ASC ? -1 : 1;
		} else {
			return 0;
		}
	}
	
	public Object getComputedValue() {
		return computedValue;
	}
	
	public Object getComputeList() {
		return computeList;
	}
}