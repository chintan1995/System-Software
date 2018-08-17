class Littab
{
	String literal;
	int address;
	
	public String getLiteral()
	{
		return literal;
	}
	
	public int getAddress()
	{
		return address;
	}
	
	public void displayLittable(int litcounter)
	{
		for(int i=0; i<litcounter; i++)
		{
			System.out.print(literal+"\t");
			System.out.println(address+"\t");
		}
	}
}