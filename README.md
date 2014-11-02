#Rift
Rift is an Intel 8051/52 assembler written in Java. Rift is built with the text 
"The 8051 Microcontroller and Embedded Systems" by Mazidi & Mazidi in mind.

  1. Rift can parse and perform error checking on standard 8051/52 based assembly
  programs.
  2. Errors, if any, are printed on the terminal for rectification.
  3. If no errors are found, a list and a hex file are created.

#Mnemonics
  The entire 8051/52 instruction set is supported. The error checking is
through. Everything from unidentified Mnemonic to invalid operands to incompatible
data/address size to improper jump ranges is reported.

#Directives
  Supported directives include ORG, END, DB, BIT & EQU.

#TODO
  1. Make error statements more specific about the error.
  2. Implement a full featured emulator.
  3. GUI.
  