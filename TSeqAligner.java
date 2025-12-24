import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class TSeqAligner
{
	String SeqA = "", SeqB = "";
	
	int[][] m;
    boolean[][] tmp;
    List<TSeqStrPair> lstR = new ArrayList<TSeqStrPair>();
    
    public TSeqAligner(String aSeqA, String aSeqB)
    {
    	SeqA = aSeqA;
    	SeqB = aSeqB;    	
    	m = new int[_LenSeqA()][_LenSeqB()];
    	tmp = new boolean[_LenSeqA()][_LenSeqB()];    	    	    	
    }
    
    
    // Score record
 	public class TTraceBackScore
 	{
 	  int Right, Below, Diagonal;
 	}
 	
    // Score record
  	public class TTraceBackScore2
  	{
  	  int Left, Up, Diagonal;
  	}
    
    // Resultant alignment pair record
 	public class TSeqStrPair
 	{
 	  String SeqA="", SeqB="";
 	  
 	  public String SandwichStr()
 	  {
 		String r = "";
 		for (int x=0; (x < SeqA.length()) && (x < SeqB.length()); x++)
 		{
 			if (SeqA.charAt(x) == SeqB.charAt(x))
 			{
 				r += "|";
 			} else
 			{
 				r += " ";
 			}
 		}
 		return r;
 	  }
 	  
 	  public void MatchPairLen()
       // Extend StrA or StrB by '-' char to ensure both strings are of same length
 	  {
 		if (SeqA.length() > SeqB.length())
 		{
 			for (int x=SeqB.length(); x < SeqA.length(); x++)  {SeqB += '-';}
 		} else
 		{
 			for (int x=SeqA.length(); x < SeqB.length(); x++)  {SeqA += '-';}
 		}
 	  }	
 	}
    
	
	public static class TFastaFile
	{
		String Desc = "";
		String Seq = "";
		
		public void LoadFile(String aFile)
		{
			try{
	        	  // Open the file that is the first 
	        	  // command line parameter
	        	  FileInputStream fstream = new FileInputStream(aFile);
	        	  // Get the object of DataInputStream
	        	  DataInputStream in = new DataInputStream(fstream);
	        	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        	  String strLine;
	        	  //Read File Line By Line
	        	  while ((strLine = br.readLine()) != null)   {
	        	  // Print the content on the console
	        	  //System.out.println (strLine);

	        		if (strLine.contains(">"))
	  				{
	  					Desc += strLine;
	  				} else
	  				{
	  					Seq += strLine;
	  				}
	        		  
	        	  
	        	  }
	        	  //Close the input stream
	        	  in.close();
	        	    }catch (Exception e){//Catch exception if any
	        	  System.err.println("Error: " + e.getMessage());
	        	  }		
		}
	}
	
	private void _ResultAlloc(int p)
	{
		// Use naughty method to determine if we need more space
		// in the dynamic array
		try { lstR.get(p); } catch (IndexOutOfBoundsException e)
		{
			System.out.println("New string-pair created");
			lstR.add(new TSeqStrPair());
		}
	}
	
	// Manages construction of the resultant alignment strings
	public void addResultCharPair(int p, char seqA, char seqB, boolean bLeftToRight)
	{
		_ResultAlloc(p);		
		
		System.out.println("add to result: " + p);		
		if (bLeftToRight)
		{
			lstR.get(p).SeqA = lstR.get(p).SeqA + seqA; 
			lstR.get(p).SeqB = lstR.get(p).SeqB + seqB;
		} else
		{
			lstR.get(p).SeqA = seqA + lstR.get(p).SeqA; 
			lstR.get(p).SeqB = seqB + lstR.get(p).SeqB;
		}
	}
	
	public void addResultNewPath(int n, int p, boolean bLeftToRight)
	{
		_ResultAlloc(p);
		if (bLeftToRight)
		{
			lstR.get(p).SeqA = lstR.get(p - 1).SeqA.substring(0, n-1);
			lstR.get(p).SeqB = lstR.get(p - 1).SeqB.substring(0, n-1);	
		} else
		{
			lstR.get(p).SeqA = lstR.get(p - 1).SeqA.substring(n);
			lstR.get(p).SeqB = lstR.get(p - 1).SeqB.substring(n);
		}
		
	}
	
	
	public int _LenSeqA()
	{
		return SeqA.length();
	}
	
	public int _LenSeqB()
	{
		return SeqB.length();
	}

}
