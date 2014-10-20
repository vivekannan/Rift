import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Mnemonics {
	
	int size;
	String opcode = "";
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
		
		try {
			if(s.charAt(0) == '\"')
				temp = this.asciify(s);
			
			else if(s.charAt(s.length() - 1) == 'H')
				temp = Integer.parseInt(s.substring(0, s.length() - 1), 16);
			
			else if(s.charAt(s.length() - 1) == 'B')
				temp = Integer.parseInt(s.substring(0, s.length() - 1), 2);
			
			else
				temp = Integer.parseInt(s.replace("D", ""));
			
			if(temp < 0)
				temp += temp > -255 ? 256 : 65536;
			
			s = Integer.toHexString(temp);
			
			if(this.getClass().getName().equals("DB"))
				return (s.length() % 2 == 0 ? s : "0" + s).toUpperCase();
			
			if((this.opcode.equals("90") && s.length() < 5) || s.length() < 3)
				return ((this.opcode.equals("90") ? "0000" : "00") + s).substring(s.length()).toUpperCase();
		
			throw new Exception();
		}
		
		catch(Exception e) {
			if(this.getClass().getName().equals("DB"))
				throw new Exception("Directive DB allows only upto 32 bits of data.");
			
			throw new Exception(String.format("Mnemonic %s expects %d-bit address/data.", this.getClass().getName(), this.opcode.equals("90") ? 16 : 8));
		}
	}
	
	boolean validate(String operands) throws Exception {
		
		String operand;
		Matcher match;
		ArrayList<Object[]> op = Boo.opcodes.get(this.getClass().getName());
		
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

class ADDC extends Mnemonics {
}

class SUBB extends Mnemonics {
}

class INC extends Mnemonics {
}

class DEC extends Mnemonics {
}

class MUL extends Mnemonics {
}

class DIV extends Mnemonics {
}

class ANL extends Mnemonics {
}

class ORL extends Mnemonics {
}

class XRL extends Mnemonics {
}

class PUSH extends Mnemonics {
}

class POP extends Mnemonics {
}

class DA extends Mnemonics {
}

class RL extends Mnemonics {
}

class RLC extends Mnemonics {
}

class RR extends Mnemonics {
}

class RRC extends Mnemonics {
}

class SWAP extends Mnemonics {
}

class XCH extends Mnemonics {
}

class XCHD extends Mnemonics {
}

class CLR extends Mnemonics {
}

class CPL extends Mnemonics {
}

class SETB extends Mnemonics {
}

class JC extends Mnemonics {
}

class JNC extends Mnemonics {
}

class JB extends Mnemonics {
}

class JNB extends Mnemonics {
}

class JBC extends Mnemonics {
}

class ACALL extends Mnemonics {
}

class LCALL extends Mnemonics {
}

class AJMP extends Mnemonics {
}

class LJMP extends Mnemonics {
}

class SJMP extends Mnemonics {
}

class JZ extends Mnemonics {
}

class JNZ extends Mnemonics {
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

class DB extends Mnemonics {
	
	boolean validate(String operands) throws Exception {
		
		Matcher match;
		Pattern p = Pattern.compile("(?:(?:[01]+B|\\d+D?|\\d[0-9A-F]*H|\"\\p{ASCII}+\")(?:,|$))+");
		
		if(!p.matcher(operands).matches())
			throw new Exception("Invalid operands for DB directive.");
		
		String[] tokens = operands.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$) *");
		
		for(String data : tokens)
			this.opcode += this.hexify(data);
		
		this.size = this.opcode.length();
		
		return true;
	}
}