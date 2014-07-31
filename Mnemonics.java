import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;

class Mnemonics {
	
	private String operands;
	private Matcher match;
	
	Mnemonics() {
	}
	
	Mnemonics(String operands) {
		this.operands = operands;
	}
	
	boolean validate() {
		
		List<Pattern> instructions = HelperClasses.hexCodes.get(this.getClass().getName());
		
		for(int i = 0; i < instructions.size(); i++) {
			match = instructions.get(i).matcher(this.operands);
			
			if(match.matches())
				return true;
		}
		
		return false;
	}
}

class MOV extends Mnemonics {
	
	public MOV(String operands) {
		super(operands);
	}
}

class MOVC extends Mnemonics {
	
	public MOVC(String operands) {
		super(operands);
	}
}

class MOVX extends Mnemonics {
	
	public MOVX(String operands) {
		super(operands);
	}
}

class ADD extends Mnemonics {
	
	public ADD(String operands) {
		super(operands);
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
	
	public INC(String operands) {
		super(operands);
	}
}

class DEC extends Mnemonics {
	
	public DEC(String operands) {
		super(operands);
	}
}

class MUL extends Mnemonics {
	
	public MUL(String operands) {
		super(operands);
	}
}

class DIV extends MUL {
	
	public DIV(String operands) {
		super(operands);
	}
}

class ANL extends Mnemonics {
	
	public ANL(String operands) {
		super(operands);
	}
}

class ORL extends ANL {
	
	public ORL(String operands) {
		super(operands);
	}
}

class XRL extends Mnemonics {
	
	public XRL(String operands) {
		super(operands);
	}
}

class PUSH extends Mnemonics {
	
	public PUSH(String operands) {
		super(operands);
	}
}

class POP extends PUSH {
	
	public POP(String operands) {
		super(operands);
	}
}

class DA extends Mnemonics {
	
	public DA(String operands) {
		super(operands);
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
	
	public XCH(String operands) {
		super(operands);
	}
}

class XCHD extends XCH {
	
	public XCHD(String operands) {
		super(operands);
	}
}

class CLR extends Mnemonics {
	
	public CLR(String operands) {
		super(operands);
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
	
	public JC(String operands) {
		super(operands);
	}
}

class JNC extends JC {
	
	public JNC(String operands) {
		super(operands);
	}
}

class JB extends Mnemonics {
	
	public JB(String operands) {
		super(operands);
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
	
	public ACALL(String operands) {
		super(operands);
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
	
	public JMP(String operands) {
		super(operands);
	}
}

class CJNE extends Mnemonics {
	
	public CJNE(String operands) {
		super(operands);
	}
}

class DJNZ extends Mnemonics {
	
	public DJNZ(String operands) {
		super(operands);
	}
}
