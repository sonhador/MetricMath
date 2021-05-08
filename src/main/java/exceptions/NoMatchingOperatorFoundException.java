package exceptions;

public class NoMatchingOperatorFoundException extends RuntimeException {
	private String operator;
	
	public NoMatchingOperatorFoundException() {}
	
	public NoMatchingOperatorFoundException(String operator) {
		this.operator = operator;
	}
	
	@Override
	public String getMessage() {
		return "Cannot recognize operator " + this.operator;
	}
}
