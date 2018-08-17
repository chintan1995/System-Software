import java.util.*;

class Node{
	Node lchild, rchild;
	String value;
	
	Node()
	{
		this.lchild=null;
		this.rchild=null;
		this.value="";
	}
	Node(String token)
	{
		this.value=token;
		this.lchild=null;
		this.rchild=null;
	}
	Node(String token, Node lchild, Node rchild)
	{
		this.lchild=lchild;
		this.rchild=rchild;
		this.value=token;
	}
	public void print()
	{
		
		if(lchild!=null)
			lchild.print();
		
		System.out.print(value);
		
		if(rchild!=null)
			rchild.print();
		
	}
}

public class RecursiveParser{
	
	static int ssm=0;
	
	public static void main(String args[]){
		Scanner sc=new Scanner(System.in);
		String expression;
		System.out.print("Enter the Expression: ");
		expression=sc.nextLine();
		sc.close();
		
		if(expression.charAt(0)=='*' || expression.charAt(0)=='+')
		{
			System.out.println("Invalid Expression");
		}
		else 
		{
			Node root=procE(expression);
			if(root!=null)
			{
				System.out.println("Expression Parsed!");
				root.print();
			}
			else
				System.out.println("Invalid Expression");
		}
		
		
	}
	
	public static Node procE(String exp) 
	{
		Node a,b;
		
		a=procT(exp);
		while(ssm<exp.length() && a!=null && exp.charAt(ssm)=='+')
		{
			ssm++;
			b=procT(exp);
			if(b!=null)
			{
				a=new Node("+",a,b);
			}
			else
				return null;
		}
		return a;
	}
	
	public static Node procT(String exp) 
	{
		Node a,b;
		
		a=procV(exp);
		while(ssm<exp.length() && a!=null && exp.charAt(ssm)=='*') 
		{
			ssm++;
			b=procV(exp);
			if(b!=null) 
			{
				a=new Node("*",a,b);
			}
			else
				return null;
		}
		return a;
	}
	
	public static Node procV(String exp) 
	{
		Node a;
		if(ssm<exp.length() && exp.charAt(ssm)!='+' || exp.charAt(ssm)!='*')
		{
			a=new Node(exp.charAt(ssm)+"");
			ssm++;
		}
		else
			return null;
		return a;
	}
}