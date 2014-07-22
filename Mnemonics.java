import java.io.*;
import java.util.regex.*;

class Mnemonics {
	
	private String operands;
	private Pattern[] instructions;
	
	Mnemonics(String operands, Pattern[] instructions) {
		
		this.operands = operands;
		this.instructions = instructions;
	}
	
	public boolean validate() {
	
		Matcher m;
		
		for(int i = 0; i < this.instructions.length; i++) {
			m = this.instructions[i].matcher(this.operands);
			
			if(m.matches())
				return true;
		}
		
		return false;
	}
	
}

class MOV extends Mnemonics {
	
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A)\\s?,\\s?(R[0-7]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|@R[01])$"),
		Pattern.compile("^(R[0-7]|@R[01])\\s?,\\s?(A|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$"),
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)\\s?,\\s?(A|R[0-7]|@R[01]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$"),
		Pattern.compile("^(DPTR)\\s?,\\s?(#(?:[01]{1,16}B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$")
	};
	
	MOV(String operands) {
		super(operands, instructions);
	}
}

class MOVC extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A)\\s?,\\s?(@A\\+(?:DPTR|PC))$")
	};
	
	MOVC(String operands) {
		super(operands, instructions);
	}
}

class MOVX extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A)\\s?,\\s?(@(?:R[01]|DPTR))$"),
		Pattern.compile("^(@(?:R[01]|DPTR))\\s?,\\s?(A)$"),
	};
	
	MOVX(String operands) {
		super(operands, instructions);
	}
}

class ADD extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A)\\s?,\\s?(R[0-7]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|@R[01])$")
	};
	
	ADD(String operands) {
		super(operands, instructions);
	}
}

class ADDC extends ADD {
	
	ADDC(String operands) {
		super(operands);
	}
}

class SUBB extends ADD {
	
	SUBB(String operands) {
		super(operands);
	}
}

class INC extends Mnemonics {
	
	private static Pattern[] instructions = {
		Pattern.compile("^(A|R[0-7]|@R[01]|(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|DPTR)$")
	};
	
	INC(String operands) {
		super(operands, instructions);
	}
}

class DEC extends Mnemonics {
	
	private static Pattern[] instructions = {
			Pattern.compile("^(A|R[0-7]|@R[01]|(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$")
	};
	
	DEC(String operands) {
		super(operands, instructions);
	}
}

class MUL extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^AB$")
	};
	
	MUL(String operands) {
		super(operands, instructions);
	}
}

class DIV extends MUL {
	
	DIV(String operands) {
		super(operands);
	}
}

class ANL extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A)\\s?,\\s?(R[0-7]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|@R[01])$"),
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)\\s?,\\s?([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$")
	};
	
	ANL(String operands) {
		super(operands, instructions);
	}
}

class ORL extends ANL {
	
	ORL(String operands) {
		super(operands);
	}
}

class XRL extends ANL {
	
	XRL(String operands) {
		super(operands);
	}
}

class PUSH extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$")
	};
	
	PUSH(String operands) {
		super(operands, instructions);
	}
}

class POP extends PUSH {
	
	POP(String operands) {
		super(operands);
	}
}

class DA extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^A$")
	};
	
	DA(String operands) {
		super(operands, instructions);
	}
}

class CLR extends DA {
	
	CLR(String operands) {
		super(operands);
	}
}

class CPL extends DA {
	
	CPL(String operands) {
		super(operands);
	}
}

class RL extends DA {
	
	RL(String operands) {
		super(operands);
	}
}

class RLC extends DA {
	
	RLC(String operands) {
		super(operands);
	}
}

class RR extends DA {
	
	RR(String operands) {
		super(operands);
	}
}

class RRC extends DA {
	
	RRC(String operands) {
		super(operands);
	}
}

class SWAP extends DA {
	
	SWAP(String operands) {
		super(operands);
	}
}

class XCH extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^(A)//s?,//s?(R[0-7]|[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H|@R[01])$")
	};
	
	XCH(String operands) {
		super(operands, instructions);
	}
}

class XCHD extends XCH {
	
	XCHD(String operands) {
		super(operands);
	}
}
