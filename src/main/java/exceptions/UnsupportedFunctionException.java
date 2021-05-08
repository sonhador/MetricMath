package exceptions;

public class UnsupportedFunctionException extends RuntimeException {
	private String function;
	
	public UnsupportedFunctionException(String function) {
		this.function = function;
	}
	
	@Override
	public String getMessage() {
		return this.function.toUpperCase() + " is not supported !!";
	}
}
