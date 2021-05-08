package model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import exceptions.InvalidArgumentException;
import exceptions.NoMatchingOperatorFoundException;
import exceptions.UnsupportedFunctionException;

public class Calc {
	private static boolean isList(Object obj) {
		if (obj.getClass().getSimpleName().equals(ArrayList.class.getSimpleName())) {
			return true;
		}
		
		return false;
	}
	
	private static boolean isArray(Object obj) {
		if (obj.getClass().getSimpleName().equals(ArrayList.class.getSimpleName()+"[]")) {
			return true;
		}
		
		return false;
	}
	
	private static boolean isNonDecimal(Object obj) {
		try {
			Long.parseLong(obj.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
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
				absList.add(Math.abs((long)elem));
			} else {
				absList.add(Math.abs((double)elem));
			}
		}
		
		return absList;
	}
	
	private static ArrayList[] abs(List[] obj) {
		ArrayList []absListArray = new ArrayList[obj.length];
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
	
	private static ArrayList avg(List[] obj) {
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
				ceilList.add((long)elem);
			} else {
				ceilList.add(Math.ceil((double)elem));
			}
		}
		
		return ceilList;
	}
	
	private static ArrayList[] ceil(List[] obj) {
		ArrayList []ceilListArray = new ArrayList[obj.length];
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
				floorList.add((long)elem);
			} else {
				floorList.add(Math.floor((double)elem));
			}
		}
		
		return floorList;
	}
	
	private static ArrayList[] floor(List[] obj) {
		ArrayList []floorListArray = new ArrayList[obj.length];
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
	
	public static boolean condition(Object val1, Operator operator, Object val2) {
		switch(operator) {
			case EqualTo:
				return val1.toString().equals(val2.toString());
			case NotEqualTo:
				return val1.toString().equals(val2.toString()) == false;
			case LessThanOrEqualTo:
				if (isNonDecimal(val1) && isNonDecimal(val2)) {
					return (long)val1 <= (long)val2;
				} else if (isNonDecimal(val1) && isNonDecimal(val2) == false) {
					return (long)val1 <= (double)val2;
				} else if (isNonDecimal(val2) == false && isNonDecimal(val2)) {
					return (double)val1 <= (long)val2;
				} else {
					return (double)val1 <= (double)val2;
				}
			case GreaterThanOrEqualTo:
				if (isNonDecimal(val1) && isNonDecimal(val2)) {
					return (long)val1 >= (long)val2;
				} else if (isNonDecimal(val1) && isNonDecimal(val2) == false) {
					return (long)val1 >= (double)val2;
				} else if (isNonDecimal(val2) == false && isNonDecimal(val2)) {
					return (double)val1 >= (long)val2;
				} else {
					return (double)val1 >= (double)val2;
				}
			case GreaterThan:
				if (isNonDecimal(val1) && isNonDecimal(val2)) {
					return (long)val1 > (long)val2;
				} else if (isNonDecimal(val1) && isNonDecimal(val2) == false) {
					return (long)val1 > (double)val2;
				} else if (isNonDecimal(val2) == false && isNonDecimal(val2)) {
					return (double)val1 > (long)val2;
				} else {
					return (double)val1 > (double)val2;
				}
			case LessThan:
				if (isNonDecimal(val1) && isNonDecimal(val2)) {
					return (long)val1 < (long)val2;
				} else if (isNonDecimal(val1) && isNonDecimal(val2) == false) {
					return (long)val1 < (double)val2;
				} else if (isNonDecimal(val2) == false && isNonDecimal(val2)) {
					return (double)val1 < (long)val2;
				} else {
					return (double)val1 < (double)val2;
				}
			case AND:
				return (boolean)val1 && (boolean)val2;
			case OR:
				return (boolean)val1 || (boolean)val2;
			default:
				throw new NoMatchingOperatorFoundException();
		}
	}
	
	private static Object max(List obj) {
		return obj.stream().collect(Collectors.maxBy(
			(o1, o2) -> {
				if (Calc.condition(o1, Operator.GreaterThan, o2)) {
					return 1;
				} else if (Calc.condition(o1, Operator.LessThan, o2)) {
					return -1;
				} else {
					return 0;
				}
			}
		));
	}
	
	private static ArrayList max(List[] obj) {
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
		return obj.stream().collect(Collectors.minBy(
			(o1, o2) -> {
				if (Calc.condition(o1, Operator.GreaterThan, o2)) {
					return 1;
				} else if (Calc.condition(o1, Operator.LessThan, o2)) {
					return -1;
				} else {
					return 0;
				}
			}
		));
	}
	
	private static ArrayList min(List[] obj) {
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
	
	private static ArrayList[] slice(List[] obj, int start, int end) {
		return Arrays.asList((ArrayList[])obj)
					 .stream()
					 .skip(start)
					 .limit(end - start)
					 .collect(Collectors.toList()).toArray(new ArrayList[] {});
	}
	
	private static ArrayList[] slice(List[] obj, int start) {
		return slice(obj, start, ((List[])obj).length);
	}
	
	private static ArrayList[] sort(List[] obj, Function function, SortOrder sortOrder) {
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
		for (List list : obj) {
			sortedList.add(new ComputedList(list, computedList.get(idx++), sortOrder));
		}
		
		Collections.sort(sortedList);
		
		return sortedList.stream()
						 .map(l -> l.getComputeList())
						 .collect(Collectors.toList())
						 .toArray(new ArrayList[] {});
	}
	
	private static double diffSquared(Object obj, double avg) {
		return Math.pow(Double.parseDouble(obj.toString()) - avg, 2);
	}
	
	private static Object stddev(List obj) {
		double avg = (double)avg(obj);
		double sum = (double)obj.stream()
							    .map(o -> diffSquared(o, avg))
							    .reduce(0, (v1, v2) -> (double)v1+(double)v2);
		return Math.sqrt(sum / obj.size());
	}
	
	private static ArrayList stddev(List[] obj) {
		ArrayList stddevList = new ArrayList();
		
		for (List l : obj) {
			stddevList.add(stddev(l));
		}
		
		return stddevList;
	}
	
	private static Object sum(List obj) {
		return obj.stream()
				  .collect(Collectors.summingDouble(
						  	o -> Double.parseDouble(o.toString())));
	}
	
	private static ArrayList sum(List[] obj) {
		ArrayList sumList = new ArrayList();
		
		for (List l : obj) {
			sumList.add(sum(l));
		}
		
		return sumList;
	}
	
	public static Object SUM(Object obj) {
		if (isList(obj)) {
			return sum((List)obj);
		} else if (isArray(obj)) {
			return sum((List[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.SUM);
		}
	}
	
	public static Object STDDEV(Object obj) {
		if (isList(obj)) {
			return stddev((List)obj);
		} else if (isArray(obj)) {
			return stddev((List[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.STDDEV);
		}
	}
	
	public static Object SORT(Object obj, String function, String sortOrder) {
		if (isArray(obj)) {
			return sort((List[])obj, Function.valueOf(function), SortOrder.valueOf(sortOrder));
		} else {
			throw new InvalidArgumentException(obj, Function.SORT);
		}
	}
	
	public static Object SLICE(Object obj, int start, int end) {
		if (isArray(obj)) {
			return slice((List[])obj, start, end);
		} else {
			throw new InvalidArgumentException(obj, Function.SLICE);
		}
	}
	
	public static Object SLICE(Object obj, int start) {
		if (isArray(obj)) {
			return slice((List[])obj, start);
		} else {
			throw new InvalidArgumentException(obj, Function.SLICE);
		}
	}
	
	public static Object METRICS(String id, Map<String, ArrayList> metrics) {
		if (id != null) {
			return metrics.get(id);
		} else {
			ArrayList[] ts = new ArrayList[metrics.keySet().size()];
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
			for (Object o : (List[])obj) {
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
			return min((List[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.MAX);
		}
	}
	
	public static Object MAX(Object obj) {
		if (isTS(obj)) {
			return max((List)obj);
		} else if (isArray(obj)) {
			return max((List[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.MAX);
		}
	}
	
	public static Object IF(Object obj, String operator, Object s, Object v1, Object v2) {
		ArrayList newTS = new ArrayList();

		if (isTS(obj)) {
			Operator op = Operator.getOperator(operator);

			for (Object o : (List)obj) {
				if (condition(o, op, s)) {
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
			return floor((List[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.FLOOR);
		}
	}
	
	public static ArrayList FIRST(List[] obj) {
		if (isTS(obj[0])) {
			return (ArrayList)obj[0];
		} else {
			throw new InvalidArgumentException(obj, Function.FIRST);
		}
	}
	
	public static ArrayList LAST(List[] obj) {
		if (isTS(obj[obj.length-1])) {
			return (ArrayList)obj[obj.length-1];
		} else {
			throw new InvalidArgumentException(obj, Function.LAST);
		}
	}
	
	public static Object CEIL(Object obj) {
		if (isTS(obj)) {
			return ceil((List)obj);
		} else if (isTSArray(obj)) {
			return ceil((List[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.CEIL);
		}
	}
	
	public static Object ABS(Object obj) {
		if (isTS(obj)) {
			return abs((List)obj);
		} else if (isTSArray(obj)) {
			return abs((List[])obj);
		} else {
			throw new InvalidArgumentException(obj, Function.ABS);
		}
	}
	
	public static Object AVG(Object obj) {
		if (isTS(obj)) {
			return avg((List)obj);
		} else if (isTSArray(obj)) {
			return avg((List[])obj);
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
