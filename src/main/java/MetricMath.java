import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import exceptions.UnsupportedFunctionException;
import model.Calc;
import model.Function;

public class MetricMath {
	private Map<String, List> data;
	
	public MetricMath(Map<String, List> data) {
		this.data = data;
	}
	
	private List getOperands(Stack<Object> operands, int paramCnt) {
		List operandsList = new ArrayList();
		while (operands.isEmpty() == false && paramCnt-- > 0) {
			operandsList.add(operands.pop());
		}
		return operandsList;
	}
	
	private List getOperands(Stack<Object> operands) {
		return getOperands(operands, operands.size());
	}
	
	private void compute(String computer, Stack<Object> operands, Calc calc) throws Exception {
		try {
			int operandCnt = Function.getOperator(computer).getParamCnt();
			Method method = Calc.getMethod(computer, operandCnt);
			List operandList = getOperands(operands, operandCnt);
			
			switch(operandCnt) {
			case 0:
				operands.push(method.invoke(calc));
				break;
			case 1:
				operands.push(method.invoke(calc, operandList.get(0)));
				break;
			case 2:
				operands.push(method.invoke(calc, operandList.get(0), operandList.get(1)));
				break;
			case 3:
				operands.push(method.invoke(calc, operandList.get(0), operandList.get(1), operandList.get(2)));
				break;
			case 4:
				operands.push(method.invoke(calc, operandList.get(0), operandList.get(1), operandList.get(2), operandList.get(3)));
				break;
			case 5:
				operands.push(method.invoke(calc, operandList.get(0), operandList.get(1), operandList.get(2), operandList.get(3), operandList.get(4)));
				break;
			}
		} catch (UnsupportedFunctionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw e;
		}
	}
	
	private void computeBinary(String computer, Stack<Object> operands, Calc calc) throws Exception {
		Method method;
		if (computer.equals(Function.ADD.name())) {
			method = Calc.getMethod(computer, 2);
		} else if (computer.equals(Function.SUBTRACT.name())) {
			method = Calc.getMethod(computer, 2);
		} else if (computer.equals(Function.MULTIPLY.name())) {
			method = Calc.getMethod(computer, 2);
		} else if (computer.equals(Function.DIVIDE.name())) {
			method = Calc.getMethod(computer, 2);
		} else if (computer.equals(Function.POWER.name())) {
			method = Calc.getMethod(computer, 2);
		} else {
			throw new UnsupportedFunctionException(computer);
		}
		
		while (operands.size() != 1) {
			try {
				operands.push(method.invoke(calc, operands.pop(), operands.pop()));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw e;
			}
		}
	}
	
	public Object execute(String expr) {
		Calc calc = new Calc();
		
		Stack<String> computers = new Stack<>();
		Stack<String> operators = new Stack<>();
		Stack<Object> operands = new Stack<>();
		
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<expr.length(); i++) {
			switch (expr.charAt(i)) {
			case '(':
			case ',':
			case ']':
			case ')':
			case '+':
			case '-':
			case '*':
			case '/':
			case '^':
				computers.push(buf.toString());
				buf = new StringBuffer();
				computers.push(""+expr.charAt(i));
				break;
			case '[':
				computers.push(""+expr.charAt(i));
				break;
			case ' ':
				break;
			default:
				buf.append(expr.charAt(i));
			}
		}
		
		if (buf.isEmpty() == false) {
			computers.push(buf.toString());
		}
		
		while (computers.isEmpty() == false) {
			String computer = computers.pop();
			try {
				compute(computer, operands, calc);
			} catch (Exception e) {
				switch(computer) {
				case "":
				case ",":
				case ")":
					operators.push(")");
					break;
				case "]":
					operands.push("]");
					break;
				case "(":
					while (operators.isEmpty() == false) {
						String operator = operators.pop();
						if (operator.equals(")")) {
							break;
						}
						try {computeBinary(operator, operands, calc);} 
						catch (Exception e2) {e2.printStackTrace();}
					}
					break;
				case "[":
					List operandsList = new ArrayList();
					while (operands.isEmpty() == false) {
						Object operand = operands.pop();
						if (operand.toString().equals("]")) {
							break;
						}
						operandsList.add(operand);
					}
					List<List<Object>> tmpList = new ArrayList<>();
					for (Object obj : operandsList) {
						if (Calc.isList(obj)) {
							tmpList.add((List)obj);
						} else {
							operands.add(obj);
						}
					}
					operands.add(tmpList.toArray());
					break;
				case "+":
				case "-":
				case "*":
				case "/":
				case "^":
					operators.add(Function.getOperator(computer).name());
					break;
				default:
					if (data.containsKey(computer)) {
						operands.push(data.get(computer));
					} else {
						operands.push(computer);
					}
				}
			}
		}
		
		while (operators.isEmpty() == false) {
			String operator = operators.pop();
			if (operator.equals(")")) {
				continue;
			}
			try {computeBinary(operator, operands, calc);} 
			catch (Exception e2) {e2.printStackTrace();}
		}
		
		return operands.pop();
	}
}
