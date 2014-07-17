import java.io.*;
import java.util.*;
import java.util.regex.*;

class Mnemonics {
}

class MOV {

	private boolean isInternal;
	private String operands;
	private int from, to;
	private static Pattern[] instructions = { Pattern.compile("^(A)\\s?,\\s?(R[0-7]|#?[0-9][0-9A-Z]{0,3}|@R[01])$"), Pattern.compile("^(R[0-7]|@R[01])\\s?,\\s?(A|#?[0-9][0-9A-Z]{0,3})$"), Pattern.compile("^([0-9][0-9A-Z]{0,3})\\s?,\\s?(A|R[0-7]|#?[0-9][0-9A-Z]{0,3}|@R[01])$") };
	
	MOV(String op, boolean inEx) {
		
		this.isInternal = inEx;
		this.operands = op;
	}
	
	public int translate() {
		
		for(int i = 0; i < 3; i++) {
			Matcher m = this.instructions[i].matcher(this.operands);
			
			if(m.matches()) {
				System.out.println(m.group(1) + " " + m.group(2));
				return 1;
			}
		}
		
		return 0;
	}
}
