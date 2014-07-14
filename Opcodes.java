import java.io.*;
import java.util.*;

class Opcodes {
}

class MOV {

	private boolean isInternal;
	private String operands;
	private byte from, to;
	
	MOV(String op, boolean inEx) {
		
		this.isInternal = inEx;
		this.operands = op;
		this.translate();
	}
	
	private boolean isMovable(String[] s) {
		
		return (addressing.isRegisterDirect(s[0]) || addressing.isRegisterIndirect(s[0]) || addressing.isAddressDirect(s[0])) && (addressing.isRegisterDirect(s[1]) || addressing.isRegisterIndirect(s[1]) || addressing.isAddressDirect(s[1]) || addressing.isImmediate(s[1]));
	}
	
	private int terminate() {
		
		System.out.println("Error! Invalid operands for MOV" + (this.isInternal ? "." : "X."));
		return -1;
	}
	
	private int translate() {
		
		String[] opTokens = this.operands.split(",");
		
		if(opTokens.length != 2)
			return this.terminate();
		
		if(this.isMovable(opTokens))
			System.out.println("Movable!");
		
		else
			return this.terminate();
		
		return 0;
	}
}
