#Rift
Rift is an Intel 8051/52 assembler written in Java. Rift is built with the book 
"The 8051 Microcontroller and Embedded Systems" by Mazidi & Mazidi in mind.

  1. Rift can parse and perform error checking on standard 8051/52 based assembly
  programs.
  2. Errors, if any, are printed on the terminal for rectification.
  3. If no errors are found, a list and a hex file are created.

##Mnemonics
  The entire 8051/52 instruction set is supported. The error checking is
through. Everything from unidentified Mnemonic to invalid operands to incompatible
data/address size to improper jump ranges are reported.

##Directives
  Supported directives include ORG, END, DB, BIT & EQU.

##Usage
  Compile with

    javac Rift.java HelperMethods.java

Run with

    java Rift [filename/path]

##TODO
  1. Make error statements more specific.
  2. Implement a full featured emulator.
  3. GUI.

##Note
  1. Rift considers the source file to be CASE INSENSITIVE (except for ascii data).
  2. All hex values MUST be suffixed with "h" and binary values MUST be suffixed with "b"; values without suffix are assumed to be decimal. This may lead to confusing errors. All hex values with a initial alphabet MUST be prefixed with 0 (As per Mazidi).
  3. The EQU/BIT directives have a slightly different syntax which is not in sync with Mazidi.
    * Mazidi ---> Symbol EQU/BIT Value
    * Rift   ---> EQU/BIT Symbol Value
  4. Rift considers EQU and BIT to be identical in every way.
  5. The reserved opcode (A5) is considered to be equivalent to NOP.
  6. A single DB can at the most define 255 bytes. DB either handles a list of comma seperated values or a single string.