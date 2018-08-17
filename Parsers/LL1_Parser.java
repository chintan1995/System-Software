import java.util.*;
import java.io.*;

public class LL1_Parser {
	public static String table[][];
	public static void main(String args[]) throws Exception
	{
		Scanner sc=new Scanner(System.in);
		String expression;
		System.out.print("Enter an Expression: ");
		
		expression=sc.nextLine();
		expression=expression.concat("|");
		sc.close();
		
		getTable();
		
		int expptr=0, tempptr=0;
		String currstate=table[1][0], temp=table[1][0], expstate=expression.charAt(expptr)+"" ,prediction, tempsub;
		System.out.println(temp);
		
		while(expptr < expression.length()) 
		{
			prediction=getPrediction(currstate,expstate);
			if(prediction.equals("e"))
			{
				temp=temp.replaceFirst(temp.charAt(tempptr)+"","");
				System.out.println(temp);
			}
			else if((prediction.equals("-")))
			{
				System.out.println("Invalid Expression");
				break;
			}
			else if(((prediction.charAt(0)+"").equals(expstate)))
			{
				temp=temp.replaceFirst(temp.charAt(tempptr)+"",prediction);
				
				expptr++;
				tempptr++;
				expstate=expression.charAt(expptr)+"";
			}
			else
			{
				temp=temp.replaceFirst(temp.charAt(tempptr)+"",prediction);
			}
			System.out.println(temp);
			try
			{
				currstate=temp.charAt(tempptr)+"";
			}
			catch(Exception e)
			{
				break;
			}
		}
		
	}
	public static String getPrediction(String row, String col)
	{
		int i,j;
	
		for(i=0 ;i<table.length;i++)
		{
			if(table[i][0].equals(row))
			{
				break;
			}
		}
		for(j=0;j<table[0].length ;j++)
		{
			if(table[0][j].equals(col))
			{
				break;
			}
		}
		
		return table[i][j];
	}
	
	
	public static void getTable() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("LLTable.txt"));
		table=new String[6][5];
		String line=br.readLine();
		int i=0, j;
		
		while(line != null)
		{
			String tokens[]=line.split("\t");
			
			for(j=0; j<tokens.length; j++)
			{
				table[i][j]=tokens[j];
			}
			i++;
			line=br.readLine();
		}
		br.close();
		
		//Printing the table
		for(i=0; i<table.length; i++)
		{
			for(j=0; j<table[i].length; j++)
			{
				System.out.print(table[i][j] + "\t");
			}
			System.out.println();
		}
		
	}
}