import java.io.*;
import java.util.regex.*;

abstract class Mnemonics {
	
	abstract public boolean translate();
}

class MOV extends Mnemonics {
	
	private int type;
	private String operands;
	private int from, to;
	private static Pattern[] instructions = { Pattern.compile("^(A)\\s?,\\s?(R[0-7]|#?(?:0[A-Z]|[0-9])[0-9A-Z]{0,1}|@R[01])$"), Pattern.compile("^(R[0-7]|@R[01])\\s?,\\s?(A|#?(?:0[A-Z]|[0-9])[0-9A-Z]{0,1})$"), Pattern.compile("^((?:0[A-Z]|[0-9])[0-9A-Z]{0,1})\\s?,\\s?(A|R[0-7]|#?(?:0[A-Z]|[0-9])[0-9A-Z]{0,1}|@R[01])$") };
	
	MOV(String op) {
		
		this.operands = op;
	}
	
	public boolean translate() {
		
		Matcher m;
		
		for(int i = 0; i < this.instructions.length; i++) {
			m = this.instructions[i].matcher(this.operands);
			
			if(m.matches())
				return true;
		}
		
		return false;
	}
}

class ADD extends Mnemonics {
	
	private int type;
	private String operands;
	private static Pattern instruction = Pattern.compile("^(A)\\s?,\\s?(R[0-7]|@R[01]|#?(?:0[A-Z]|[0-9])[0-9A-Z]{0,1})$");
	
	ADD(String op) {
		this.operands = op;
	}
	
	public boolean translate() {
		
		Matcher m = this.instruction.matcher(this.operands);
		
		return m.matches();
	}
}

class IDC extends Mnemonics {
	
	private int type;
	private String operands;
	private static Pattern[] instructions = { Pattern.compile("^(A|R[0-7]|@R[01]|(?:0[A-Z]|[0-9])[0-9A-Z]{0,1}|DPTR)$"), Pattern.compile("^(A|R[0-7]|@R[01]|(?:0[A-Z]|[0-9])[0-9A-Z]{0,1})$") };
	
	IDC(String op, int t) {
		this.operands = op;
		this.type = t;
	}
	
	public boolean translate() {
		
		Matcher m = this.instructions[this.type].matcher(this.operands);
		
		return m.matches();
	}
}

class MULDIV extends Mnemonics {
	
	private String operands;
	
	MULDIV(String op) {
		this.operands = op;
	}
	
	public boolean translate() {
		
		return this.operands.equals("AB");
	}
}

class DA extends Mnemonics {
	
	private String operands;
	
	DA(String op) {
		this.operands = op;
	}
	
	public boolean translate() {
		
		return this.operands.equals("A");
	}
}

class AOXL extends Mnemonics {
	
	private int type;
	private String operands;
	private static Pattern[] instructions = { Pattern.compile("^(A)\\s?,\\s?(R[0-7]|#?(?:0[A-Z]|[0-9])[0-9A-Z]{0,1}|@R[01])$"), Pattern.compile("^(?:0[A-Z]|[0-9])[0-9A-Z]{0,1}\\s?,\\s?(A|#(?:0[A-Z]|[0-9])[0-9A-Z]{0,1})$") };
	
	AOXL(String op) {
		this.operands = op;
	}
	
	public boolean translate() {
		
		Matcher m;
		
		for(int i = 0; i < this.instructions.length; i++) {
			m = this.instructions[i].matcher(this.operands);
			
			if(m.matches())
				return true;
		}
		
		return false;
	}
}
