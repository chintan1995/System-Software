import java.io.*;

class NewAssembler
{
	static int opcounter;
	static int symbcounter;
	static int litcounter;
	static int poolcounter;
	static int MNCODE;
	static int REGNO;
	static int SYMBCODE;
	
	
	public static void main(String[] a) throws Exception
	{
		String line, tokens[], registers[]={"AREG","BREG","CREG","DREG"};
		int startflag=0, lc=0, i, tempflag=0;
		int[] pool=new int[10];
		pool[poolcounter]=0;
		String type;
		
		BufferedReader br=new BufferedReader(new FileReader("tempsource.txt"));
		BufferedWriter bw=new BufferedWriter(new FileWriter("IC.txt"));
		//Declaring Opcode Storage
		OpStorage[] opobj=new OpStorage[18];
		for(i=0;i<18;i++)
			opobj[i]=new OpStorage();
		
		getMnemonic(opobj);
		
		//Declaring Opcode Table
		Optab[] op_table=new Optab[30];
		for(i=0;i<30;i++)
			op_table[i]=new Optab();
		
		//Declaring Symbol table
		Symbtab[] s_table=new Symbtab[20];
		for(i=0;i<20;i++)
			s_table[i]=new Symbtab();
		
		//Declareing Literal Table
		Littab[] lit_table=new Littab[10];
		for(i=0;i<10;i++)
			lit_table[i]=new Littab();
		
		line=br.readLine();
		while(line!=null && !line.equals("END") && !line.equals("STOP"))
		{
			tokens=line.split(" ");
			if(tokens[0].equals("START"))
			{
				/*fillOptab(tokens[0], opobj, op_table);*/
				if(startflag==0)
				{
					lc=Integer.parseInt(tokens[1]);
					
					type=mnType(tokens[0],opobj);
					bw.write(type+"-"+MNCODE+" ");
					bw.write("C-"+lc);
					bw.newLine();
					System.out.println("IC is = "+type+"-"+MNCODE +" C-"+lc);
					System.out.println("Initialized LC with "+lc);
					startflag=1;
				}
				else
				{
					System.out.println("Error! Start used two times!");
					return;
				}
			}
			//Code for LTORG//
			else if(line.equals("LTORG"))
			{
				lc=allocateLiterals(lit_table, lc, pool);
				poolcounter++;
				pool[poolcounter]=litcounter;
				
				type=mnType(line,opobj);
				bw.write(type+"-"+MNCODE+" ");
				bw.newLine();
			}
			else if(opcodeCheck(tokens[0], opobj))
			{
				if(validatePara(tokens[0], tokens.length-1, opobj))  //logically, the number of parameters will be length-1 (if NO label)
				{
					//if the control gets in this branch, then the number of parameters would be tokens.length-1
					
					if(tokens.length-1 == 2)  //if there are 2 parameters, then first one must be a register
					{
						//before filling the op_table, check the register (1st arg) and also fill the s_table (2nd arg)
						if(checkRegister(tokens[1], registers))
						{
							fillOptab(tokens[0], opobj, op_table);
							
							type=mnType(tokens[0],opobj);
							bw.write(type+"-"+MNCODE+" ");
							bw.write(REGNO+" ");
							//check here if the 2nd arg is literal or not. If literal enter it in lit table with -1 address till LTORG
							if(tokens[2].startsWith("'"))
							{
								fillLittable(tokens[2], -1, lit_table);
								
								SYMBCODE=symbolCode(tokens[2], lit_table);
								bw.write("L-"+SYMBCODE);
								bw.newLine();
								System.out.println("IC is = "+type+"-"+MNCODE +" "+REGNO+" L-"+SYMBCODE);
							}
							else
							{
								fillSymbtab(tokens[2], -1, 1, s_table);  //-1, bc variable not yet declared. So no address
								
								SYMBCODE=symbolCode(tokens[2], s_table);
								bw.write("S-"+SYMBCODE);
								bw.newLine();
								System.out.println("IC is = "+type+"-"+MNCODE +" "+REGNO+" S-"+SYMBCODE);
							}
						}
						else
						{
							System.out.println("Error! Wrong Register used! You used "+tokens[1]);
							return;
						}
					}
					else if(tokens.length-1 == 1)  //if there is one parameter, like READ N
					{
						fillOptab(tokens[0], opobj, op_table);
						type=mnType(tokens[0],opobj);
						bw.write(type+"-"+MNCODE+" ");
						fillSymbtab(tokens[1], -1, 1, s_table);  //-1, bc variable not yet declared. So no address
						SYMBCODE=symbolCode(tokens[1], s_table);
						bw.write("S-"+SYMBCODE);
						bw.newLine();
						System.out.println("IC is = "+type+"-"+MNCODE +" S-"+SYMBCODE);
					}

					lc++;
				}
				else
				{
					System.out.println("Error! Wrong Parameters");
					return;
				}
			}
			else  //it ought to be a label
			{
				//entry of the label in the Symbol Table
				fillSymbtab(tokens[0], lc, 1, s_table);  //1 is length
				System.out.println("Label Found "+tokens[0]+" and assigned address "+lc);
				if(opcodeCheck(tokens[1], opobj))  //Now checking if the 2nd token (after the label) is OpCode or not
				{
					if(validatePara(tokens[1], tokens.length-2, opobj))  //logically, the number of parameters will be length-2 (if label is THERE)
					{

						fillOptab(tokens[1], opobj, op_table);
						
						//check here if the 2nd arg is literal or not. If literal enter it in lit table with -1 address till LTORG
						if(tokens[3].startsWith("'"))
						{
							fillLittable(tokens[3], -1, lit_table);
							type=mnType(tokens[1],opobj);
							bw.write(type+"-"+MNCODE+" ");
							SYMBCODE=symbolCode(tokens[3], lit_table);
							bw.write("L-"+SYMBCODE);
							bw.newLine();
							System.out.println("IC is = "+type+"-"+MNCODE +" "+REGNO+" L-"+SYMBCODE);
						}
						else
						{
							fillSymbtab(tokens[3], lc, 1, s_table);
							type=mnType(tokens[1],opobj);
							bw.write(type+"-"+MNCODE+" ");
							SYMBCODE=symbolCode(tokens[3], s_table);
							bw.write("S-"+SYMBCODE);
							bw.newLine();
							System.out.println("IC is = "+type+"-"+MNCODE +" S-"+SYMBCODE);
						}
						
						lc++;
					}
					else
					{
						System.out.println("Error! Wrong Parameters");
						return;
					}
				}
				else
				{
					System.out.println("Error! Wrong Syntax!");
					return;
				}
			}
			line=br.readLine();
		}
		
		if(line.equals("STOP"))
		{
			fillOptab(line, opobj, op_table);
			type=mnType(line,opobj);
			bw.write(type+"-"+MNCODE);
			bw.newLine();
		}
		
		line=br.readLine();
		while(!line.equals("END"))
		{
			tempflag=0;
			System.out.println(line);
			tokens=line.split(" ");
			if(tokens[1].equals("DS"))
			{
				fillOptab(tokens[1] ,opobj, op_table);
				if(validatePara(tokens[1], tokens.length-2, opobj))
				{
					for(i=0; i<symbcounter; i++)
					{
						if(tokens[0].equals(s_table[i].name))
						{
							s_table[i].address=lc;
							System.out.println("Address assigned to "+tokens[0]+ " is "+s_table[i].address);
							s_table[i].length=Integer.parseInt(tokens[2]);
							lc += Integer.parseInt(tokens[2]);
							tempflag=1;
							type=mnType(tokens[1],opobj);
							bw.write(type+"-"+MNCODE+" ");
							bw.write("C-"+tokens[2]);
							bw.newLine();
							break;
						}
					}
				}
				else
				{
					System.out.println("Error! Wrong Parameters");
					return;
				}
				if(tempflag==0)
				{
					System.out.println("Error! Variable declared but not used");
					return;
				}
			}
			else if(tokens[1].equals("DC"))
			{
				fillOptab(tokens[1] ,opobj, op_table);
				if(validatePara(tokens[1], tokens.length-2, opobj))
				{
					for(i=0; i<symbcounter; i++)
					{
						if(tokens[0].equals(s_table[i].name))
						{
							s_table[i].address=lc;
							System.out.println("Address assigned to "+tokens[0]+ " is "+s_table[i].address);
							lc++;
							tempflag=1;
							type=mnType(tokens[1],opobj);
							bw.write(type+"-"+MNCODE+" ");
							bw.write("C-"+1);
							bw.newLine();
							break;
						}
					}
				}
				else
				{
					System.out.println("Error! Wrong Parameters");
					return;
				}
				
				if(tempflag==0)
				{
					System.out.println("Error! Variable declared but not used");
					return;
				}
			}
			else
			{
				System.out.println("Error! Wrong Keyword. You used "+tokens[1]);
				return;
			}
			
			line=br.readLine();
		}
		/*fillOptab(line, opobj, op_table);*/
		
		//fill all the remaining literals in literal table after END
		if(pool[poolcounter]!=litcounter)
		{
			allocateLiterals(lit_table, lc, pool);
			poolcounter++;
			pool[poolcounter]=litcounter;
		}
		
		System.out.println();
		displayOptable(op_table);
		System.out.println();
		System.out.println();
		displaySymbtable(s_table);
		System.out.println();
		System.out.println();
		lit_table.displayLittable(litcounter);
		System.out.println();
		System.out.println();
		displayPootable(pool);
		bw.close();
		br.close();
	}
	
	static String mnType(String name, OpStorage[] obj)
	{
		int i;
		for(i=0; i<18 && !name.equals(obj[i].name); i++);
		
		MNCODE=i;
		//System.out.println("MNCODE= "+i);
		return obj[i].type;
	}
	
	static int symbolCode(String name, Littab[] obj)
	{
		int i;
		for(i=0; i<litcounter && !name.equals(obj[i].literal); i++);
		
		return i;
	}
	static int symbolCode(String name, Symbtab[] obj)
	{
		int i;
		for(i=0; i<symbcounter && !name.equals(obj[i].name); i++);
		
		return i;
	}

	
	static int allocateLiterals(Littab[] tableobj, int lc, int[] pool)
	{
		int i;
		
		for(i=pool[poolcounter]; i<litcounter; i++)
		{
			tableobj[i].address=lc;
			lc++;
		}
		return lc;
	}
	
	static void fillLittable(String name, int address, Littab[] tableobj)
	{
		int i, flag=0;
		for(i=0; i<litcounter; i++)
		{
			if(name.equals(tableobj[i].literal))
			{
				flag=1;
				break;
			}
		}
		
		if(flag==0)
		{
			tableobj[litcounter].literal=name;
			tableobj[litcounter].address=address;
			litcounter++;
		}
	}
	
	static void fillSymbtab(String name, int lc, int length, Symbtab[] sobj)
	{
		for(int i=0; i<symbcounter; i++)
		{
			if(name.equals(sobj[i].name))
				return;
		}

		//System.out.println("Filling Symbol Table with Symbol "+name);
		sobj[symbcounter].name=name;
		sobj[symbcounter].address=lc;
		sobj[symbcounter].length=length;
		symbcounter++;

	}
	
	static void fillOptab(String name, OpStorage opobj[], Optab[] tableobj)
	{
		int i;
		for(i=0; i<opcounter; i++)
		{
			if(name.equals(tableobj[i].opcode))
				return;
		}
		for(i=0 ; !name.equals(opobj[i].name); i++);
		
		tableobj[opcounter].opcode = opobj[i].name;
		tableobj[opcounter].mn_class = opobj[i].type;
		opcounter++;
	}
	

	static boolean validatePara(String token, int para, OpStorage[] obj)
	{
		int i=0;
		for( ; !token.equals(obj[i].name); i++);
		
		if(para==obj[i].args)
			return true;
		else
			return false;
		
	}
	
	static boolean opcodeCheck(String token, OpStorage[] obj)
	{
		for(int j=0; j<18; j++)
		{
			if(token.equals(obj[j].name))
			{
				//System.out.println("Mnemonic found -> "+ obj[j].name);
				return true;
			}
		}
		return false;
	}
	
	static void getMnemonic(OpStorage obj[])throws Exception
	{
		BufferedReader br=new BufferedReader(new FileReader("Mnemonic op codes.txt"));
		String line;
		String[] tokens;
		for(int i=0; i<18 ; i++)
		{
			line=br.readLine();
			tokens=line.split(" ");
			obj[i].name=tokens[0];
			obj[i].args=Integer.parseInt(tokens[1]);
			obj[i].type=tokens[2];
		}
	}
	
	static boolean checkRegister(String token, String[] regs)
	{
		for(int i=0; i<4; i++)
		{
			if(token.equals(regs[i]))
			{
				REGNO=i;
				return true;
			}
		}
		return false;
	}
	
	static void displayOptable(Optab[] obj)
	{
		for(int i=0; i<opcounter; i++)
		{
			System.out.print(obj[i].opcode+"\t");
			System.out.println(obj[i].mn_class);
		}	
	}
	
	static void displaySymbtable(Symbtab[] obj)
	{
		for(int i=0; i<symbcounter; i++)
		{
			System.out.print(obj[i].name+"\t");
			System.out.print(obj[i].address+"\t");
			System.out.println(obj[i].length);
		}		
	}
	
	/*static void displayLittable(Littab[] obj)
	{
		for(int i=0; i<litcounter; i++)
		{
			System.out.print(obj[i].literal+"\t");
			System.out.println(obj[i].address+"\t");
		}
	}*/
	
	static void displayPootable(int[] pool)
	{
		for(int i=0; i<poolcounter; i++)
		{
			System.out.println(pool[i]);
		}
	}
}