import java.io.*;
import java.util.regex.*;

abstract class Mnemonics {
	
	abstract public int translate();
}

class MOV extends Mnemonics {

	private boolean isInternal;
	private String operands;
	private int from, to;
	private static Pattern[] instructions = { Pattern.compile("^(A)\\s?,\\s?(R[0-7]|#?[0-9][0-9A-Z]{0,3}|@R[01])$"), Pattern.compile("^(R[0-7]|@R[01])\\s?,\\s?(A|#?[0-9][0-9A-Z]{0,3})$"), Pattern.compile("^([0-9][0-9A-Z]{0,3})\\s?,\\s?(A|R[0-7]|#?[0-9][0-9A-Z]{0,3}|@R[01])$") };
	
	MOV(String op, boolean inEx) {
		
		this.isInternal = inEx;
		this.operands = op;
	}
	
	public int translate() {
		
		Matcher m;
		
		for(int i = 0; i < this.instructions.length; i++) {
			m = this.instructions[i].matcher(this.operands);
			
			if(m.matches())
				return 1;
		}
		
		return 0;
	}
}

class ADD extends Mnemonics {
	
	private String operands;
	private static Pattern instruction = Pattern.compile("^(A)\\s?,\\s?(R[0-7]|@R[01]|#?[0-9][0-9A-Z]{0,3})$");
	
	ADD(String op) {
		this.operands = op;
	}
	
	public int translate() {
		
		Matcher m = this.instruction.matcher(this.operands);
		
		return m.matches() ? 1: 0;
	}
}

class INC extends Mnemonics {
	
	private String operands;
	private static Pattern instruction = Pattern.compile("^(A|R[0-7]|@R[01]|[0-9][0-9A-Z]{0,3})$");
	
	INC(String op) {
		this.operands = op;
	}
	
	public int translate() {
		
		Matcher m = this.instruction.matcher(this.operands);
		
		if(m.matches())
			return 1;
		
		return 0;
	}
}
