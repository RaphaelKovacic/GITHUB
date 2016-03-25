package Class_For_JSON;

public class OperationResult {
	int value;
	String comment;
	
	public OperationResult(int value, String comment) {
		this.value = value;
		this.comment = comment;
	}

	public OperationResult(){
	}
	
	public int getValue(){
		return this.value;
	}
	
	public String getComment(){
		return this.comment;
	}
}