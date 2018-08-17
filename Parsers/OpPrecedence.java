import java.util.*;
import java.io.*;

class Node
{
	public String value;
	public Node left;
	public Node right;
	
	Node(String value)
	{
		this.value=value;
		this.left=null;
		this.right=null;
	}
	Node(String value, Node left, Node right)
	{
		this.value=value;
		this.left=left;
		this.right=right;
	}
}

class OpPrecedence
{
	static Stack<String> stk1=new Stack<String>();
	static Stack<Node> stk2=new Stack<Node>();	//expression tree
	static String precedenceMatrix[][]=new String[5][5];
	
	public static void main(String[] args) throws Exception
	{
		int ssm=0;
		String exp, precedence, tmp;
		//Initializing precedenceMatrix[][]
		for(int i=0; i<5; i++)
			for(int j=0; j<5; j++)
				precedenceMatrix[i][j]=new String();
			
		System.out.println("Enter the Expression: ");
		Scanner sc=new Scanner(System.in);
		exp=sc.nextLine();
		exp=exp.concat("$");
		storePrecedenceMatrix();
		dispPrecedenceMatrix();
		stk1.push("$");
		
		while(true)
		{
			precedence=getPrecedence(stk1.peek(), exp.charAt(ssm)+"");
			if(precedence.equals("<")) //push from stk1
			{
				stk1.push(exp.charAt(ssm)+"");
				ssm++;
			}
			else if(precedence.equals(">")) //pop off from stk1
			{
				if(operatorCheck(stk1.peek()))
				{
					Node newnode=new Node(stk1.pop(), stk2.pop(), stk2.pop());
					stk2.push(newnode);
				}
				else //create node and push it in stk2
				{
					Node newnode=new Node(stk1.pop());
					stk2.push(newnode);
				}
			}
			else if(precedence.equals("-"))
			{
				System.out.println("Expression parsed!!");
				inOrder(stk2.peek());
				break;
			}
			else
			{
				System.out.println("Error! Cannot parse the Expression...");
				break;
			}
		}
	}
	
	public static boolean operatorCheck(String tos)
	{
		if(tos.equals("+") || tos.equals("*"))
		{
			return true;
		}
		
		return false;
	}
	
	public static String getPrecedence(String row, String col)
	{
		int i=0, j=0;
		
		//row search i
		for(i=0; i<5; i++)
		{
			if(row.equals(precedenceMatrix[i][0]))
				break;
		}
		
		//column search j
		for(j=0; j<5; j++)
		{
			if(col.equals(precedenceMatrix[0][j]))
				break;
		}
		
		return precedenceMatrix[i][j];
	}
	
	public static void storePrecedenceMatrix() throws Exception
	{
		BufferedReader br= new BufferedReader(new FileReader("precedence_matrix.txt"));
		String line;
		String tokens[];
		int i=0;
		
		while((line=br.readLine())!=null)
		{
			tokens=line.split("\t");
			
			for(int j=0; j<5; j++)
			{
				precedenceMatrix[i][j]=tokens[j];
			}
			i++;
		}
	}
	
	public static void dispPrecedenceMatrix()
	{
		for(int i=0; i<5; i++)
		{
			for(int j=0; j<5; j++)
			{
				System.out.print(precedenceMatrix[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	public static void inOrder(Node root)
	{
		if(root==null)
			return;
		
		inOrder(root.left);
		System.out.print(root.value+" ");
		inOrder(root.right);
		System.out.print(" ");
	}
}