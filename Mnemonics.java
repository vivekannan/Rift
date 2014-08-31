import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Mnemonics {
	
	int size;
	String opcode;
	final static Pattern LABEL = Pattern.compile("[A-Z][A-Z0-9]*");
	
	int asciify(String s) throws Exception {

		String temp = "";
		s = s.substring(1, s.length() - 1);

		for(int i = 0; i < s.length(); i++)
			temp += String.format("%2s", Integer.toHexString((int) s.charAt(i))).replace(" ", "0");

		return Integer.parseInt(temp, 16);
	}

	String hexify(String s) throws Exception {

		int temp;
		s = s.replace("#", "");
		
		try {
			if(s.charAt(0) == '\"')
				temp = this.asciify(s);

			else if(s.charAt(s.length() - 1) == 'H')
				temp = Integer.parseInt(s.substring(0, s.length() - 1), 16);
			
			else if(s.charAt(s.length() - 1) == 'B')
				temp = Integer.parseInt(s.substring(0, s.length() - 1), 2);
			
			else
				temp = Integer.parseInt(s.replace("D", ""));
		}

		catch(Exception e) {
			throw new Exception(String.format("Mnemonic %s expects %d-bit address/data.", this.getClass().getName(), this.opcode.equals("90") ? 16 : 8));
		}
		
		if(temp < 0)
			temp += temp > -257 ? 256 : 4096;
		
		s = Integer.toHexString(temp);

		if((this.opcode.equals("90") && s.length() < 5) || s.length() < 3)
			return ((this.opcode.equals("90") ? "0000" : "00") + s).substring(s.length()).toUpperCase();
		
		else
			throw new Exception(String.format("Mnemonic %s expects %d-bit address/data.", this.getClass().getName(), this.opcode.equals("90") ? 16 : 8));
	}
	
	boolean validate(String operands) throws Exception {
		
		String operand;
		Matcher match;
		List<Object[]> op = Boo.opcodes.get(this.getClass().getName());

		for(Object[] o : op) {
			match = ((Pattern) o[0]).matcher(operands);
			
			if(match.matches()) {
				this.opcode = (String) o[1];
				this.size = 1 + match.groupCount() + (this.opcode.equals("02") || this.opcode.equals("12") || this.opcode.equals("90") ? 1 : 0);
				
				for(int i = 1; i <= match.groupCount(); i++) {
					operand = match.group(i);
					
					if(Boo.symbols.containsKey(operand))
						this.opcode += Boo.symbols.get(operand);
					
					else if(LABEL.matcher(operand).matches())
						this.opcode += ":" + operand;
					
					else
						this.opcode += this.hexify(operand);
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
