#Boo
Boo is an Atmel 8051 microcontroller interpreter written in Java. Boo is written with Atmel's 8051 in mind and therefore may not function properly with other 8051 family microcontrollers.

##Feature
* Supports the following memory maps,
  1. A 16-bit 64KB ROM that is used for storing the program.
  2. A 8-bit 128B RAM that is further split into the following,
    * 4 x 8B Data Banks.
    * 128-bit Bit addressable bank.
    * 80B Scratch pad.
  3. Special Function Registers including but not limited to,
    * DPTR, an 16-bit Data Pointer Register which is further split into,
      1. DPH, an 8-bit Data Pointer Higher Register.
      2. DPL, an 8-bit Data Pointer Lower Register.
    * SP, an 8-bit Stack Pointer
    * A, an 8-bit Accumulator Register.
    * B, an 8-bit Carry Accumulator Register.
    * PSW, an 8-bit Program Status Word Register
* Supports multiple CLK frequencies as per the original Intel 8051.

##TODO
!!!!!! Rigorous testing required. Must find sane means to test the hell out of all the features. !!!!!!
Implement AJMP and ACALL Mnemonics properly. --> Done. Testing required.
Implement support for directives such EQU, DB, BIT. --> Done for EQU. BIT will not be supported. DB will probably be messy.
Implement support for signed arithmetic. --> Done.
Implement support for "Symbols" such as PSW, DP[HL] and bit level symbols line A.0, P1.4 etc. --> Done. Testing required.
Implement support for ASCII characters as data. --> Testing required.