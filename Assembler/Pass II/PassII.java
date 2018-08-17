import java.io.*;

class OpStorage
{
	String name;
	int args;
	String type;
	String machineCode;
}

class Symbtab
{
	String name;
	int address;
	int length;
}

class Littab
{
	String literal;
	int address;
}


class PassII
{
	public static void main(String[] args) throws Exception
	{
		OpStorage[] opobj=new OpStorage[18];
		for(int i=0;i<18;i++)
			opobj[i]=new OpStorage();
		
		Symbtab[] s_table=new Symbtab[10];
		for(int i=0;i<10;i++)
			s_table[i]=new Symbtab();
		
		Littab[] lit_table=new Littab[10];
		for(int i=0;i<10;i++)
			lit_table[i]=new Littab();
		
		readOpStorage(opobj);			
		readSymbolTab(s_table);
		readLiteralTable(lit_table);
		int index;
		String line;
		String[] tokens;
		BufferedReader br=new BufferedReader(new FileReader("IC.txt"));
		BufferedWriter bw=new BufferedWriter(new FileWriter("Machine Code.txt"));
		
		while((line=br.readLine())!=null)
		{
			tokens=line.split("\t");
			
			index=Integer.parseInt(tokens[0].substring(3));
			bw.write(opobj[index].machineCode+" ");  //writing opcode mc
			try
			{
				if(tokens[2].startsWith("S"))
				{
					bw.write("R"+tokens[1]+" ");  //writing Register
					index=Integer.parseInt(tokens[2].substring(2));
					bw.write(s_table[index].address+" ");  //writing symboltab address
				}
				else if(tokens[2].startsWith("L"))
				{
					bw.write("R"+tokens[1]+" ");  //writing Register
					index=Integer.parseInt(tokens[2].substring(2));
					bw.write(lit_table[index].address+" ");  //writing littab address
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				try
				{
					if(tokens[1].startsWith("S"))
					{
						bw.write("R0 ");  //writing Register
						index=Integer.parseInt(tokens[1].substring(2));
						bw.write(s_table[index].address+" ");  //writing symboltab address
					}
					else if(tokens[1].startsWith("C"))
					{
						bw.write("R0 ");  //writing Register
						index=Integer.parseInt(tokens[1].substring(2));
						bw.write(index+" ");  //writing length of ds/dl variable
					}
				}
				catch(ArrayIndexOutOfBoundsException ex){}
			}
			bw.newLine();
		}
		br.close();
		bw.close();
	}
	
	static void readLiteralTable(Littab obj[])throws Exception
	{
		BufferedReader br=new BufferedReader(new FileReader("Literal Table.txt"));
		String line;
		String[] tokens;
		int i=0;
		while((line=br.readLine())!=null)
		{
			tokens=line.split("\t");
			obj[i].literal=tokens[0];
			obj[i].address=Integer.parseInt(tokens[1]);
			i++;
		}
	}
	
	static void readSymbolTab(Symbtab obj[])throws Exception
	{
		BufferedReader br=new BufferedReader(new FileReader("Symbol Table.txt"));
		String line;
		String[] tokens;
		int i=0;
		while((line=br.readLine())!=null)
		{
			tokens=line.split("\t");
			obj[i].name=tokens[0];
			obj[i].address=Integer.parseInt(tokens[1]);
			obj[i].length=Integer.parseInt(tokens[2]);
			i++;
		}
	}
	
	static void readOpStorage(OpStorage obj[])throws Exception
	{
		BufferedReader br=new BufferedReader(new FileReader("Mnemonic op codes.txt"));
		String line;
		String[] tokens;
		int i=0;
		while((line=br.readLine())!=null)
		{
			tokens=line.split("\t");
			obj[i].name=tokens[0];
			obj[i].args=Integer.parseInt(tokens[1]);
			obj[i].type=tokens[2];
			obj[i].machineCode=tokens[3];
			i++;
		}
	}
}