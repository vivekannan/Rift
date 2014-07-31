import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Mnemonics {
	
	private String operands;
	private Pattern[] instructions;
	private Matcher match;
	
	Mnemonics() {
	}
	
	Mnemonics(String operands, Pattern[] instructions) {
		
		this.operands = operands;
		this.instructions = instructions;
	}
	
	boolean validate() {
		
		//System.out.println(this.getClass().getName());
		
		for(int i = 0; i < this.instructions.length; i++) {
			match = this.instructions[i].matcher(this.operands);
			
			if(match.matches())
				return true;
		}
		
		return false;
	}
}

class MOV extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A),(R[0-7]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|@R[01])$"),
		Pattern.compile("^(R[0-7]|@R[01]),(A|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$"),
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H),(A|R[0-7]|@R[01]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$"),
		Pattern.compile("^(DPTR),(#(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$"),
		Pattern.compile("^(C,(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H),C)$")
	};
	
	public MOV(String operands) {
		super(operands, instructions);
	}
}

class MOVC extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A),(@A\\+(?:DPTR|PC))$")
	};
	
	public MOVC(String operands) {
		super(operands, instructions);
	}
}

class MOVX extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A),(@(?:R[01]|DPTR))$"),
		Pattern.compile("^(@(?:R[01]|DPTR)),(A)$"),
	};
	
	public MOVX(String operands) {
		super(operands, instructions);
	}
}

class ADD extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A),(R[0-7]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|@R[01])$")
	};
	
	public ADD(String operands) {
		super(operands, instructions);
	}
}

class ADDC extends ADD {
	
	public ADDC(String operands) {
		super(operands);
	}
}

class SUBB extends ADD {
	
	public SUBB(String operands) {
		super(operands);
	}
}

class INC extends Mnemonics {
	
	private static Pattern[] instructions = {
		Pattern.compile("^(A|R[0-7]|@R[01]|(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|DPTR)$")
	};
	
	public INC(String operands) {
		super(operands, instructions);
	}
}

class DEC extends Mnemonics {
	
	private static Pattern[] instructions = {
		Pattern.compile("^(A|R[0-7]|@R[01]|(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$")
	};
	
	public DEC(String operands) {
		super(operands, instructions);
	}
}

class MUL extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(AB)$")
	};
	
	public MUL(String operands) {
		super(operands, instructions);
	}
}

class DIV extends MUL {
	
	public DIV(String operands) {
		super(operands);
	}
}

class ANL extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A),(R[0-7]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|@R[01])$"),
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H),([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$"),
		Pattern.compile("^(C,//?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$")
	};
	
	public ANL(String operands) {
		super(operands, instructions);
	}
}

class ORL extends ANL {
	
	public ORL(String operands) {
		super(operands);
	}
}

class XRL extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A),(R[0-7]|#?(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)|@R[01])$"),
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H),([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$"),
	};
	
	public XRL(String operands) {
		super(operands, instructions);
	}
}

class PUSH extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$")
	};
	
	public PUSH(String operands) {
		super(operands, instructions);
	}
}

class POP extends PUSH {
	
	public POP(String operands) {
		super(operands);
	}
}

class DA extends Mnemonics {
	
	final private static Pattern[] instructions = {
		Pattern.compile("^(A)$")
	};
	
	public DA(String operands) {
		super(operands, instructions);
	}
}

class RL extends DA {
	
	public RL(String operands) {
		super(operands);
	}
}

class RLC extends DA {
	
	public RLC(String operands) {
		super(operands);
	}
}

class RR extends DA {
	
	public RR(String operands) {
		super(operands);
	}
}

class RRC extends DA {
	
	public RRC(String operands) {
		super(operands);
	}
}

class SWAP extends DA {
	
	public SWAP(String operands) {
		super(operands);
	}
}

class XCH extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^(A),(R[0-7]|[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H|@R[01])$")
	};
	
	public XCH(String operands) {
		super(operands, instructions);
	}
}

class XCHD extends XCH {
	
	public XCHD(String operands) {
		super(operands);
	}
}

class CLR extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^(A|C|(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H))$")
	};
	
	public CLR(String operands) {
		super(operands, instructions);
	}
}

class CPL extends CLR {
	
	public CPL(String operands) {
		super(operands);
	}
}

class SETB extends CLR {
	
	public SETB(String operands) {
		super(operands);
	}
}

class JC extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^([A-Z][A-Z0-9]*)$")
	};
	
	public JC(String operands) {
		super(operands, instructions);
	}
}

class JNC extends JC {
	
	public JNC(String operands) {
		super(operands);
	}
}

class JB extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H),([A-Z][A-Z0-9]*)$")
	};
	
	public JB(String operands) {
		super(operands, instructions);
	}
}

class JNB extends JB {
	
	public JNB(String operands) {
		super(operands);
	}
}

class JBC extends JB {
	
	public JBC(String operands) {
		super(operands);
	}
}

class ACALL extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$")
	};
	
	public ACALL(String operands) {
		super(operands, instructions);
	}
}

class LCALL extends ACALL {
	
	public LCALL(String operands) {
		super(operands);
	}
}

class AJMP extends ACALL {
	
	public AJMP(String operands) {
		super(operands);
	}
}

class LJMP extends ACALL {
	
	public LJMP(String operands) {
		super(operands);
	}
}

class SJMP extends ACALL {
	
	public SJMP(String operands) {
		super(operands);
	}
}

class JZ extends ACALL {
	
	public JZ(String operands) {
		super(operands);
	}
}

class JNZ extends ACALL {
	
	public JNZ(String operands) {
		super(operands);
	}
}

class RET extends Mnemonics {
}

class RETI extends Mnemonics {
}

class NOP extends Mnemonics {
}

class JMP extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^@A+DPTR$")
	};
	
	public JMP(String operands) {
		super(operands, instructions);
	}
}

class CJNE extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^(A|R[0-7]|@R[01]),(#(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)),([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$"),
		Pattern.compile("^(A),([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H),([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$")
	};
	
	public CJNE(String operands) {
		super(operands, instructions);
	}
}

class DJNZ extends Mnemonics {
	
	final static private Pattern[] instructions = {
		Pattern.compile("^(R[0-7]|(?:[01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)),([01]+B|[0-9]+D?|(?:0[A-Z]|\\d)[0-9A-Z]+H)$")
	};
	
	public DJNZ(String operands) {
		super(operands, instructions);
	}
}
