package exceptions;

import model.Function;

public class InvalidArgumentException extends RuntimeException {
	private Object obj;
	private Function func;
	
	public InvalidArgumentException(Object obj, Function func) {
		this.obj = obj;
		this.func = func;
	}
	
	@Override
	public String getMessage() {
		return "Invalid Argument for "+func.name()+": "+obj;
	}
}
