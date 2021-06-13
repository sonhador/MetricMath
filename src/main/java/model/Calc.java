package model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import exceptions.InvalidArgumentException;
import exceptions.NoMatchingOperatorFoundException;
import exceptions.UnsupportedFunctionException;

public class Calc {
	public static boolean isList(Object obj) {
		if (obj.getClass().getSimpleName().equals(ArrayList.class.getSimpleName()) ||
			obj.getClass().getSimpleName().equals(List.class.getSimpleName())) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isArray(Object obj) {
		if (obj.getClass().getSimpleName().endsWith("[]")) {
			return true;
		}
		
		return false;
	}
	
	private static boolean isNonDecimal(Object obj) {
		try {
			Integer.parseInt(obj.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private static boolean isNumeric(Object obj) {
		try {
			Long.parseLong(obj.toString());
			return true;
		} catch (NumberFormatException e) {
			try {
				Double.parseDouble(obj.toString());
				return true;
			} catch (NumberFormatException e1) {
				return false;
			}
		}
	}
	
	private static Object numberFirst(Object obj) {
		try {
			return Integer.parseInt(obj.toString());
		} catch (NumberFormatException e) {
			try {
				return Double.parseDouble(obj.toString());
			} catch (NumberFormatException e1) {
				return obj;
			}
		}
	}
	
	private static boolean isBoolean(Object obj) {
		if (obj.toString().toLowerCase().equals("true") ||
			obj.toString().toLowerCase().equals("false")) {
			return true;
		}		
		
		return false;
	}
	
	private static boolean isTrue(Object obj) {
		if (isBoolean(obj)) {
			return Boolean.valueOf(obj.toString());
		} else if (isNonDecimal(obj)) {
			if (Integer.parseInt(obj.toString()) == 0) {
				return false;
			} else {
				return true;
			}
		}
		
		return true;
	}
	
	private static boolean isTS(Object obj) {
		return isList(obj);
	}
	
	private static boolean isTSArray(Object obj) {
		return isArray(obj);
	}
	
	private static ArrayList abs(List obj) {
		ArrayList absList = new ArrayList();
		for (Object elem : obj) {
			if (isNonDecimal(elem)) {
				absList.add(Math.abs((int)elem));
			} else {
				absList.add(Math.abs((double)elem));
			}
		}
		
		return absList;
	}
	
	private static Object[] abs(Object[] obj) {
		Object []absListArray = new Object[obj.length];
		int idx = 0;
		for (Object elem : obj) {
			if (isTS(elem)) {
				absListArray[idx++] = abs((List)elem); 
			} else {
				throw new InvalidArgumentException(obj, Function.ABS);
			}
		}
		
		return absListArray;
	}
	
	private static Object avg(List obj) {
		if (obj.size() == 0) {
			return 0;
		}
		
		return obj.stream()
				  .collect(Collectors.averagingDouble(
						  o -> Double.parseDouble(o.toString())));
	}
	
	private static ArrayList avg(Object[] obj) {
		ArrayList avgList = new ArrayList();
		int idx = 0;
		for (Object elem : obj) {
			if (isTS(elem)) {
				avgList.add(avg((List)elem));
			} else {
				throw new InvalidArgumentException(obj, Function.AVG);
			}
		}
		
		return avgList;
	}
	
	private static ArrayList ceil(List obj) {
		ArrayList ceilList = new ArrayList();
		for (Object elem : obj) {
			if (isNonDecimal(elem)) {
				ceilList.add((int)elem);
			} else {
				ceilList.add(Math.ceil((double)elem));
			}
		}
		
		return ceilList;
	}
	
	private static Object[] ceil(Object[] obj) {
		Object[] ceilListArray = new Object[obj.length];
		int idx = 0;
		for (Object elem : obj) {
			if (isTS(elem)) {
				ceilListArray[idx++] = ceil((List)elem); 
			} else {
				throw new InvalidArgumentException(obj, Function.CEIL);
			}
		}
		
		return ceilListArray;
	}
	
	private static ArrayList floor(List obj) {
		ArrayList floorList = new ArrayList();
		for (Object elem : obj) {
			if (isNonDecimal(elem)) {
				floorList.add((int)elem);
			} else {
				floorList.add(Math.floor((double)elem));
			}
		}
		
		return floorList;
	}
	
	private static Object[] floor(Object[] obj) {
		Object[] floorListArray = new Object[obj.length];
		int idx = 0;
		for (Object elem : obj) {
			if (isTS(elem)) {
				floorListArray[idx++] = floor((List)elem); 
			} else {
				throw new InvalidArgumentException(obj, Function.FLOOR);
			}
		}
		
		return floorListArray;
	}
	
	public static boolean boolCond(Object val1, Operator operator, Object val2) {
		switch(operator) {
			case EqualTo:
				return val1.toString().equals(val2.toString());
			case NotEqualTo:
				return val1.toString().equals(val2.toString()) == false;
			case LessThanOrEqualTo:
				if (isNonDecimal(val1) && isNonDecimal(val2)) {
					return Integer.parseInt(val1.toString()) <= Integer.parseInt(val2.toString());
				} else if (isNonDecimal(val1) && isNonDecimal(val2) == false) {
					return Integer.parseInt(val1.toString()) <= Double.parseDouble(val2.toString());
				} else if (isNonDecimal(val2) == false && isNonDecimal(val2)) {
					return Double.parseDouble(val1.toString()) <= Integer.parseInt(val2.toString());
				} else {
					return Double.parseDouble(val1.toString()) <= Double.parseDouble(val2.toString());
				}
			case GreaterThanOrEqualTo:
				if (isNonDecimal(val1) && isNonDecimal(val2)) {
					return Integer.parseInt(val1.toString()) >= Integer.parseInt(val2.toString());
				} else if (isNonDecimal(val1) && isNonDecimal(val2) == false) {
					return Integer.parseInt(val1.toString()) >= Double.parseDouble(val2.toString());
				} else if (isNonDecimal(val2) == false && isNonDecimal(val2)) {
					return Double.parseDouble(val1.toString()) >= Integer.parseInt(val2.toString());
				} else {
					return Double.parseDouble(val1.toString()) >= Double.parseDouble(val2.toString());
				}
			case GreaterThan:
				if (isNonDecimal(val1) && isNonDecimal(val2)) {
					return Integer.parseInt(val1.toString()) > Integer.parseInt(val2.toString());
				} else if (isNonDecimal(val1) && isNonDecimal(val2) == false) {
					return Integer.parseInt(val1.toString()) > Double.parseDouble(val2.toString());
				} else if (isNonDecimal(val2) == false && isNonDecimal(val2)) {
					return Double.parseDouble(val1.toString()) > Integer.parseInt(val2.toString());
				} else {
					return Double.parseDouble(val1.toString()) > Double.parseDouble(val2.toString());
				}
			case LessThan:
				if (isNonDecimal(val1) && isNonDecimal(val2)) {
					return Integer.parseInt(val1.toString()) < Integer.parseInt(val2.toString());
				} else if (isNonDecimal(val1) && isNonDecimal(val2) == false) {
					return Integer.parseInt(val1.toString()) < Double.parseDouble(val2.toString());
				} else if (isNonDecimal(val2) == false && isNonDecimal(val2)) {
					return Double.parseDouble(val1.toString()) < Integer.parseInt(val2.toString());
				} else {
					return Double.parseDouble(val1.toString()) < Double.parseDouble(val2.toString());
				}
			case AND:
				return (boolean)val1 && (boolean)val2;
			case OR:
				return (boolean)val1 || (boolean)val2;
			default:
				throw new NoMatchingOperatorFoundException();
		}
	}
	
	public static ArrayList condition(Object val1, Operator operator, Object val2) {
		if (isList(val1) && isList(val2)) {
			int length = Math.min(((List)val1).size(), ((List)val2).size());
			ArrayList newTS = new ArrayList();
			for (int i=0; i<length; i++) {
				if (boolCond(((List)val1).get(i), operator, ((List)val2).get(i))) {
					newTS.add(true);
				} else {
					newTS.add(false);
				}
			}
			return newTS;
		} else if (isList(val1)) {
			ArrayList newTS = new ArrayList();
			for (Object obj : (List)val1) {
				if (boolCond(obj, operator, val2)) {
					newTS.add(1);
				} else {
					newTS.add(0);
				}
			}
			return newTS;
		} else if (isNumeric(val1)) {
			ArrayList newTS = new ArrayList();
			if (boolCond(val1, operator, val2)) {
				newTS.add(true);
			} else {
				newTS.add(false);
			}
			return newTS;
		} else {
			throw new NoMatchingOperatorFoundException();
		}
	}
	
	private static Object max(List obj) {
		return obj.stream().collect(Collectors.maxBy(
			(o1, o2) -> {
				if (Calc.boolCond(o1, Operator.GreaterThan, o2)) {
					return 1;
				} else if (Calc.boolCond(o1, Operator.LessThan, o2)) {
					return -1;
				} else {
					return 0;
				}
			}
		));
	}
	
	private static ArrayList max(Object[] obj) {
		ArrayList maxList = new ArrayList();
		
		for (Object o : obj) {
			if (isTS(o)) {
				maxList.add(max((List)o));
			} else {
				throw new InvalidArgumentException(obj, Function.MAX);
			}
		}
		
		return maxList;
	}
	
	private static Object min(List obj) {
		return ((Optional)obj.stream().collect(Collectors.minBy(
				(o1, o2) -> {
					if (Calc.boolCond(o1, Operator.GreaterThan, o2)) {
						return 1;
					} else if (Calc.boolCond(o1, Operator.LessThan, o2)) {
						return -1;
					} else {
						return 0;
					}
				}
			))).get();
	}
	
	private static ArrayList min(Object[] obj) {
		ArrayList maxList = new ArrayList();
		
		for (Object o : obj) {
			if (isTS(o)) {
				maxList.add(min((List)o));
			} else {
				throw new InvalidArgumentException(obj, Function.MAX);
			}
		}
		
		return maxList;
	}
	
	private static Object[] slice(Object[] obj, int start, int end) {
		return Arrays.asList(obj)
					 .stream()
					 .skip(start)
					 .limit(end - start)
					 .collect(Collectors.toList()).toArray(new Object[] {});
	}
	
	private static Object[] slice(Object[] obj, int start) {
		return slice(obj, start, ((Object[])obj).length);
	}
	
	private static Object[] sort(Object[] obj, Function function, SortOrder sortOrder) {
		ArrayList computedList;
		switch(function) {
		case AVG:
			computedList = avg(obj);
			break;
		case MIN:
			computedList = min(obj);
			break;
		case MAX:
			computedList = max(obj);
			break;
		case SUM:
			computedList = sum(obj);
			break;
		default:
			throw new UnsupportedFunctionException(function.name());
		}
		
		List<ComputedList> sortedList = new ArrayList<>();
		int idx = 0;
		for (Object list : obj) {
			sortedList.add(new ComputedList(list, computedList.get(idx++), sortOrder));
		}
		
		Collections.sort(sortedList);
		
		return sortedList.stream()
						 .map(l -> l.getComputeList())
						 .collect(Collectors.toList())
						 .toArray(new Object[] {});
	}
	
	private static double diffSquared(Object obj, double avg) {
		return Math.pow(Double.parseDouble(obj.toString()) - avg, 2);
	}
	
	private static Object stddev(List obj) {
		double avg = (double)avg(obj);
		double sum = (double)obj.stream()
							    .map(o -> diffSquared(o, avg))
							    .reduce(0, (v1, v2) -> Double.parseDouble(v1.toString())+Double.parseDouble(v2.toString()));
		return Math.sqrt(sum / obj.size());
	}
	
	private static ArrayList stddev(Object[] obj) {
		ArrayList stddevList = new ArrayList();
		
		for (Object l : obj) {
			stddevList.add(stddev((List)l));
		}
		
		return stddevList;
	}
	
	private static Object sum(List obj) {
		return obj.stream()
				  .collect(Collectors.summingDouble(
						  	o -> Double.parseDouble(o.toString())));
	}
	
	private static ArrayList sum(Object[] obj) {
		ArrayList sumList = new ArrayList();
		
		for (Object l : obj) {
			sumList.add(sum((List)l));
		}
		
		return sumList;
	}
	
	private static double calcBinary(Number d1, Number d2, Function function) {
		switch (function) {
		case ADD:
			if (isNonDecimal(d1) && isNonDecimal(d2)) {
				return d1.longValue() + d2.longValue();
			} else if (isNonDecimal(d1) && isNonDecimal(d2) == false) {
				return d1.longValue() + d2.doubleValue();
			} else if (isNonDecimal(d1) == false && isNonDecimal(d2)) {
				return d1.doubleValue() + d2.longValue();
			} else {
				return d1.doubleValue() + d2.doubleValue();
			}
		case SUBTRACT:
			if (isNonDecimal(d1) && isNonDecimal(d2)) {
				return d1.longValue() - d2.longValue();
			} else if (isNonDecimal(d1) && isNonDecimal(d2) == false) {
				return d1.longValue() - d2.doubleValue();
			} else if (isNonDecimal(d1) == false && isNonDecimal(d2)) {
				return d1.doubleValue() - d2.longValue();
			} else {
				return d1.doubleValue() - d2.doubleValue();
			}
		case MULTIPLY:
			if (isNonDecimal(d1) && isNonDecimal(d2)) {
				return d1.longValue() * d2.longValue();
			} else if (isNonDecimal(d1) && isNonDecimal(d2) == false) {
				return d1.longValue() * d2.doubleValue();
			} else if (isNonDecimal(d1) == false && isNonDecimal(d2)) {
				return d1.doubleValue() * d2.longValue();
			} else {
				return d1.doubleValue() * d2.doubleValue();
			}
		case DIVIDE:
			try {
				if (isNonDecimal(d1) && isNonDecimal(d2)) {
					return d1.longValue() / d2.longValue();
				} else if (isNonDecimal(d1) && isNonDecimal(d2) == false) {
					return d1.longValue() / d2.doubleValue();
				} else if (isNonDecimal(d1) == false && isNonDecimal(d2)) {
					return d1.doubleValue() / d2.longValue();
				} else {
					return d1.doubleValue() / d2.doubleValue();
				}
			} catch (Exception e) {
				return Double.MAX_VALUE;
			}
		case POWER:
			return Math.pow(d1.doubleValue(), d2.doubleValue());
		default:
			throw new UnsupportedFunctionException(function.name());
		}
	}
	
	private static ArrayList calcBinary(Number obj1, List obj2, Function function, boolean flip) {
		ArrayList added = new ArrayList();
		
		for (Object obj : obj2) {
			if (flip) {
				added.add(calcBinary(
						obj1,
					    isNonDecimal(obj) ? 
					    		Integer.parseInt(obj.toString()) : 
					    		Double.parseDouble(obj.toString()),
						function)); 
			} else {
				added.add(calcBinary(					
					    isNonDecimal(obj) ? 
					    		Integer.parseInt(obj.toString()) : 
					    		Double.parseDouble(obj.toString()),
						obj1,
						function)); 
			}
		}
		
		return added;
	}
	
	private static ArrayList calcBinary(List obj1, List obj2, Function function, boolean flip) {
		ArrayList added = new ArrayList();
		int length = Math.max(obj1.size(), obj2.size());
		
		for (int i=0; i<length; i++) {
			Number obj1val;
			Number obj2val;
			
			if (i >= obj1.size()) {
				obj1val = 0;
			} else {
				obj1val = isNonDecimal(obj1.get(i)) ?
						Integer.parseInt(obj1.get(i).toString()) :
						Double.parseDouble(obj1.get(i).toString());
			}
			
			if (i >= obj2.size()) {
				obj2val = 0;
			} else {
				obj2val = isNonDecimal(obj2.get(i)) ?
						Integer.parseInt(obj2.get(i).toString()) :
						Double.parseDouble(obj2.get(i).toString());
			}
			
			if (flip) {
				added.add(calcBinary(obj2val, obj1val, function));
			} else {
				added.add(calcBinary(obj1val, obj2val, function));
			}
		}
		
		return added;
	}
	
	private static Object[] calcBinary(Number obj1, Object[] obj2, Function function, boolean flip) {
		Object[] added = new Object[obj2.length];
		
		int idx=0;
		for (Object obj : obj2) {
			added[idx++] = calcBinary(obj1, (List)obj, function, flip);
		}
		
		return added;
	}
	
	private static Object[] calcBinary(List obj1, Object[] obj2, Function function, boolean flip) {
		Object[] added = new Object[obj2.length];
		
		int idx=0;
		for (Object obj : obj2) {
			added[idx++] = calcBinary(obj1, (List)obj, function, flip);
		}
		
		return added;
	}
	
	public static Object calcBinary(Object obj1, Object obj2, Function function) {	
		if (isList(obj1) && isList(obj2)) {
			return calcBinary((List)obj1, (List)obj2, function, false);
		} else if (isList(obj1) && isArray(obj2)) {
			return calcBinary((List)obj1, (Object[])obj2, function, false);
		} else if (isArray(obj1) && isList(obj2)) {
			return calcBinary((List)obj2, (Object[])obj1, function, true);
		} else if (isList(obj1)) {
			if (isNonDecimal(obj2)) {
				return calcBinary(Integer.parseInt(obj2.toString()), (List)obj1, function, false);
			} else {
				return calcBinary(Double.parseDouble(obj2.toString()), (List)obj1, function, false);
			}
		} else if (isList(obj2)) {
			if (isNonDecimal(obj1)) {
				return calcBinary(Integer.parseInt(obj1.toString()), (List)obj2, function, true);
			} else {
				return calcBinary(Double.parseDouble(obj1.toString()), (List)obj2, function, true);
			}
		} else if (isArray(obj1)) {
			if (isNonDecimal(obj2)) {
				return calcBinary(Integer.parseInt(obj2.toString()), (Object[])obj1, function, false);
			} else {
				return calcBinary(Double.parseDouble(obj2.toString()), (Object[])obj1, function, false);
			}
		} else if (isArray(obj2)) {
			if (isNonDecimal(obj1)) {
				return calcBinary(Integer.parseInt(obj1.toString()), (Object[])obj2, function, true);
			} else {
				return calcBinary(Double.parseDouble(obj1.toString()), (Object[])obj2, function, true);
			}
		} else {
			if (isNonDecimal(obj1) && isNonDecimal(obj2)) {
				return calcBinary(Integer.parseInt(obj1.toString()), Integer.parseInt(obj2.toString()), function);
			} else if (isNonDecimal(obj1) && isNonDecimal(obj2) == false) {
				return calcBinary(Integer.parseInt(obj1.toString()), Double.parseDouble(obj2.toString()), function);
			} else if (isNonDecimal(obj1) == false && isNonDecimal(obj2)) {
				return calcBinary(Double.parseDouble(obj1.toString()), Integer.parseInt(obj2.toString()), function);
			} else {
				return calcBinary(Double.parseDouble(obj1.toString()), Double.parseDouble(obj2.toString()), function);
			}
		}
	}
	
	public static Object ADD(Object obj1, Object obj2) {
		return calcBinary(obj1, obj2, Function.ADD);
	}
	
	public static Object SUBTRACT(Object obj1, Object obj2) {
		return calcBinary(obj1, obj2, Function.SUBTRACT);
	}
	
	public static Object MULTIPLY(Object obj1, Object obj2) {
		return calcBinary(obj1, obj2, Function.MULTIPLY);
	}
	
	public static Object DIVIDE(Object obj1, Object obj2) {
		return calcBinary(obj1, obj2, Function.DIVIDE);
	}
	
	public static Object POWER(Object obj1, Object obj2) {
		return calcBinary(obj1, obj2, Function.POWER);
	}
	
	public static Object SUM(Object obj) {
		if (isList(obj)) {
			return sum((List)obj);
		} else if (isArray(obj)) {
			return sum((Object[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.SUM);
		}
	}
	
	public static Object STDDEV(Object obj) {
		if (isList(obj)) {
			return stddev((List)obj);
		} else if (isArray(obj)) {
			return stddev((Object[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.STDDEV);
		}
	}
	
	public static Object SORT(Object obj, String function, String sortOrder) {
		if (isArray(obj)) {
			return sort((Object[])obj, Function.valueOf(function), SortOrder.valueOf(sortOrder));
		} else {
			throw new InvalidArgumentException(obj, Function.SORT);
		}
	}
	
	public static Object SLICE(Object obj, Object start, Object end) {
		if (isArray(obj)) {
			return slice((Object[])obj, Integer.parseInt(start.toString()), Integer.parseInt(end.toString()));
		} else {
			throw new InvalidArgumentException(obj, Function.SLICE);
		}
	}
	
	public static Object SLICE(Object obj, Object start) {
		if (isArray(obj)) {
			return slice((Object[])obj, Integer.parseInt(start.toString()));
		} else {
			throw new InvalidArgumentException(obj, Function.SLICE);
		}
	}
	
	public static Object METRICS(String id, Map<String, ArrayList> metrics) {
		if (id != null) {
			return metrics.get(id);
		} else {
			Object[] ts = new Object[metrics.keySet().size()];
			int idx = 0;
			for (String key : metrics.keySet()) {
				ts[idx++] = metrics.get(key);
			}
			return ts;
		}
	}
	
	public static Object METRIC_COUNT(Object obj) {
		if (isArray(obj)) {
			int total = 0;
			for (Object o : (Object[])obj) {
				if (isTS(o)) {
					total += ((List)o).size();
				} else {
					throw new InvalidArgumentException(obj, Function.METRIC_COUNT);
				}
			}
			
			return total;
		} else {
			throw new InvalidArgumentException(obj, Function.METRIC_COUNT);
		}
	}
	
	public static Object MIN(Object obj) {
		if (isTS(obj)) {
			return min((List)obj);
		} else if (isArray(obj)) {
			return min((Object[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.MAX);
		}
	}
	
	public static Object MAX(Object obj) {
		if (isTS(obj)) {
			return max((List)obj);
		} else if (isArray(obj)) {
			return max((Object[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.MAX);
		}
	}
	
	public static Object IF(Object obj, Object v1, Object v2) {
		ArrayList newTS = new ArrayList();
		
		if (isBoolean(obj)) {
			if (Boolean.valueOf(obj.toString())) {
				newTS.add(numberFirst(v1));
			} else {
				newTS.add(numberFirst(v2));
			}
		} else if (isList(obj)) {
			List objList = (List)obj;
			
			if (isList(v1) && isList(v2)) {
				List v1List = (List)v1;
				List v2List = (List)v2;
				
				for (int i=0; i<objList.size(); i++) {
					Object v1Obj;
					Object v2Obj;
					boolean objBool = isTrue(objList.get(i));
					
					if (i >= v1List.size()) {
						v1Obj = 0;
					} else {
						v1Obj = v1List.get(i);
					}
					
					if (i >= v2List.size()) {
						v2Obj = null;
					} else {
						v2Obj = v2List.get(i);
					}
					
					if (objBool) {
						newTS.add(numberFirst(v1Obj));
					} else {
						if (v2Obj != null) {
							newTS.add(numberFirst(v2Obj));
						}
					}
				}
			} else if (isList(v1)) {
				List v1List = (List)v1;
				
				for (int i=0; i<objList.size(); i++) {
					Object v1Obj;
					boolean objBool = isTrue(objList.get(i));
					
					if (i >= v1List.size()) {
						v1Obj = 0;
					} else {
						v1Obj = v1List.get(i);
					}
					
					if (objBool) {
						newTS.add(numberFirst(v1Obj));
					} else {
						newTS.add(numberFirst(v2));
					}
				}
			} else if (isList(v2)) {
				List v2List = (List)v2;
				
				for (int i=0; i<objList.size(); i++) {
					Object v2Obj;
					boolean objBool = isTrue(objList.get(i));
					
					if (i >= v2List.size()) {
						v2Obj = null;
					} else {
						v2Obj = v2List.get(i);
					}
					
					if (objBool) {
						newTS.add(numberFirst(v1));
					} else {
						if (v2Obj != null) {
							newTS.add(numberFirst(v2Obj));
						}
					}
				}
			} else {
				for (int i=0; i<objList.size(); i++) {
					boolean objBool = isTrue(objList.get(i));
					
					if (objBool) {
						newTS.add(numberFirst(v1));
					} else {
						newTS.add(numberFirst(v2));
					}
				}
			}
		} else {
			throw new InvalidArgumentException(obj, Function.IF);
		}
		
		return newTS;
	}
	
	public static Object IF(Object obj, String operator, Object s, Object v1, Object v2) {
		ArrayList newTS = new ArrayList();

		if (isTS(obj)) {
			Operator op = Operator.getOperator(operator);

			for (Object o : (List)obj) {
				if (boolCond(o, op, s)) {
					newTS.add(v1);
				} else {
					newTS.add(v2);
				}
			}
		} else {
			throw new InvalidArgumentException(obj, Function.IF);
		}
		
		return newTS;
	}
	
	public static Object FLOOR(Object obj) {
		if (isTS(obj)) {
			return floor((List)obj);
		} else if (isTSArray(obj)) {
			return floor((Object[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.FLOOR);
		}
	}
	
	public static Object FIRST(Object[] obj) {
		if (isTS(obj[0])) {
			return (Object)obj[0];
		} else {
			throw new InvalidArgumentException(obj, Function.FIRST);
		}
	}
	
	public static Object LAST(Object[] obj) {
		if (isTS(obj[obj.length-1])) {
			return (Object)obj[obj.length-1];
		} else {
			throw new InvalidArgumentException(obj, Function.LAST);
		}
	}
	
	public static Object CEIL(Object obj) {
		if (isTS(obj)) {
			return ceil((List)obj);
		} else if (isTSArray(obj)) {
			return ceil((Object[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.CEIL);
		}
	}
	
	public static Object ABS(Object obj) {
		if (isTS(obj)) {
			return abs((List)obj);
		} else if (isTSArray(obj)) {
			return abs((Object[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.ABS);
		}
	}
	
	public static Object AVG(Object obj) {
		if (isTS(obj)) {
			return avg((List)obj);
		} else if (isTSArray(obj)) {
			return avg((Object[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.AVG);
		}
	}
	
	public static Method getMethod(String function, int argc) {
		for (Method method : Calc.class.getMethods()) {
			if (method.getName().equals(function.toUpperCase()) && 
				method.getParameterCount() == argc) {
				return method;
			}
		}
		
		throw new UnsupportedFunctionException(function);
	}
}
