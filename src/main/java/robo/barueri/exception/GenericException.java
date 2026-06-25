package robo.barueri.exception;

@SuppressWarnings("serial")
public class GenericException extends Exception{
	
	private String string;
	public GenericException(String string) {
		super();
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}
}
