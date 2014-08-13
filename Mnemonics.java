import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Mnemonics {
	
	int size = 0;
	String opcode;
	static Pattern label = Pattern.compile("[A-Z][A-Z0-9_]*");
	
	String hexify(String s) throws Exception {
		
		s = s.replaceAll("#", "");
		
		if(s.charAt(s.length() - 1) == 'H')
			s = s.substring(0, s.length() - 1).replaceAll("^0*", "");
		
		else if(s.charAt(s.length() - 1) == 'B')
			s = Integer.toHexString(Integer.parseInt(s.substring(0, s.length() - 1), 2));
		
		else
			s = Integer.toHexString(Integer.parseInt(s.replaceAll("D", "")));
		
		if((this.opcode.equals("90") && s.length() < 5) || s.length() < 3)
			return ((this.opcode.equals("90") ? "0000" : "00") + s).substring(s.length()).toUpperCase();
		
		else
			throw new Exception(String.format("Mnemonic %s expects %d-bit address/data.", this.getClass().getName(), this.opcode.equals("90") ? 16 : 8));
	}
	
	boolean validate(String operands) throws Exception {
		
		List<Object[]> op = Boo.opcodes.get(this.getClass().getName());
		Matcher match;
		
		for(Object[] o : op) {
			match = ((Pattern) o[0]).matcher(operands);
			
			if(match.matches()) {
				this.opcode = (String) o[1];
				this.size = 1 + match.groupCount() + (this.opcode.equals("03") || this.opcode.equals("12") || this.opcode.equals("90") ? 1 : 0);
				
				for(int j = 1; j <= match.groupCount(); j++) {
					if(label.matcher(match.group(j)).matches())
						this.opcode += "[" + match.group(j);
					
					else
						this.opcode += hexify(match.group(j));
				}
				
				return true;
			}
		}
		
		return false;
	}
}

class MOV extends Mnemonics {
}

class MOVC extends Mnemonics {
}

class MOVX extends Mnemonics {
}

class ADD extends Mnemonics {
}

class ADDC extends ADD {
}

class SUBB extends ADD {
}

class INC extends Mnemonics {
}

class DEC extends Mnemonics {
}

class MUL extends Mnemonics {
}

class DIV extends MUL {
}

class ANL extends Mnemonics {
}

class ORL extends ANL {
}

class XRL extends Mnemonics {
}

class PUSH extends Mnemonics {
}

class POP extends PUSH {
}

class DA extends Mnemonics {
}

class RL extends DA {
}

class RLC extends DA {
}

class RR extends DA {
}

class RRC extends DA {
}

class SWAP extends DA {
}

class XCH extends Mnemonics {
}

class XCHD extends XCH {
}

class CLR extends Mnemonics {
}

class CPL extends CLR {
}

class SETB extends CLR {
}

class JC extends Mnemonics {
}

class JNC extends JC {
}

class JB extends Mnemonics {
}

class JNB extends JB {
}

class JBC extends JB {
}

class ACALL extends Mnemonics {
}

class LCALL extends ACALL {
}

class AJMP extends ACALL {
}

class LJMP extends ACALL {
}

class SJMP extends ACALL {

}

class JZ extends ACALL {
}

class JNZ extends ACALL {
}

class RET extends Mnemonics {
}

class RETI extends Mnemonics {
}

class NOP extends Mnemonics {
}

class JMP extends Mnemonics {

}

class CJNE extends Mnemonics {
}

class DJNZ extends Mnemonics {
}
