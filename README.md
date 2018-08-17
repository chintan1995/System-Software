# System-Software

This repository contains programs for system software which includes,
1). Scanner
2). Three types of Parsers
3). Two pass Assembler (Assembly language).

All programs are done in Java programming language.

Scanner
--------
The job of a scanner is to tokenize and identify each of the symbols in each statement and verify if the tokens are correctly written or not. Syntax checking is NOT done in this phase.

Parser
-------
The job of parser is to check the syntax of the expression used in the statement. Three type of parsers are implemented (seperately) here.
-> LL 1 Parser
-> Recursive Decent Parser
-> Opreator Precedence Parser

Assembler
----------
Assembler is a two pass process. 
In pass I, various tables like "OPTAB", "SYMBTAB", LITTAB", "POOLTAB" are constructed.
On completion of the Pass I, an IC (Intermediate Code) is generated and stored in "IC.txt" file.
Also, all the tables constructed are used in Pass II, so those final tables are stored in each files respective for each table.

In Pass II, we need the tables constructed like Literal Table, Symbol Table, Mnemonic Opcode Table (which has machine code for each opcode) in Pass I along with the IC.
After the Pass II is completed, a "Machine code" is generated, which is readable by the processor. (Here the machine codes are just a simulation, and does not represent the actual machine codes by the actual system :))
