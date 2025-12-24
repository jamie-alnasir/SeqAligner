// Needleman-Wunsch GLOBAL Sequence Alignment Algorithm ------------------------
// Class to implement Needleman-Wunsch global sequence alignment algorithm
// Written by Jamie Al-Nasir 2013


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TNWSeqAlign extends TSeqAligner {
	
	public TNWSeqAlign(String aSeqA, String aSeqB) {
		super(aSeqA, aSeqB);
		// TODO Auto-generated constructor stub
	}

	String[][] lstSimilarityM, lstScoreM;	
	
    
	public void ExecSimilarScore()
	// JJA Implemented
	// Generate similarity score matrix
	{
	  for (int i=0; i < _LenSeqA(); i++)
	  {
		for (int j=0; j < _LenSeqB(); j++)
	    {
	      if (SeqA.charAt(i) == SeqB.charAt(j))
	      {
	    	  m[i][j] = 1;
	      } else
	      {
	    	  m[i][j] = 0;
	      }
	    }
	  }		
	}
	
	
	private int _ScoreRow(int i, int j)
	// JJA Implemented
	// Score Row
	{
	  int r = 0, Tmp = 0;	  
	  // Basic bounds check
	  if ((j+1) > (_LenSeqB() - 1)) { return 0; }	  
	  for (int n=i+1; n< _LenSeqA(); n++)
	  {
		  Tmp = m[n][j+1];
		  if (Tmp > r) { r = Tmp; };
	  }	    
	  return r;
	}
	
	private int _ScoreCol(int i, int j)
	// JJA Implemented
	// Score Column
	{
	  int r = 0, Tmp = 0;	  
	  // Basic bounds check
	  if ((i+1) > (_LenSeqA() - 1)) { return 0; }
	  
	  for (int n=j; n< _LenSeqB(); n++)
	  {
		  Tmp = m[i+1][n];
		  if (Tmp > r) { r = Tmp; };
	  }
	    
	  return r;
	}
	  
		
	
	public void ExecScore()
	// JJA Implemented
	// Score the matrix
	{
	  int r, c, Tmp;
	  for (int j=_LenSeqB() - 1; j >= 0; j--)
	  {
		for (int i=_LenSeqA() - 1; i >= 0; i--)
	    {
			r = _ScoreRow(i, j);
	        c = _ScoreCol(i, j);
	        
	        if (r > c)
	        {
	          Tmp = r;
	        } else
	        {
	          Tmp = c;
	        }
	        
            m[i][j] = m[i][j] + Tmp;
	        
	    }
	  }		
	}
	
	
	private TTraceBackScore _TracebackScore(int i, int j)
    {
		TTraceBackScore r = new TTraceBackScore();
		r.Right    = 0;
	    r.Below    = 0;
	    r.Diagonal = 0;	
	    // Basic bounds check
	    if ((i+1) > (_LenSeqA() - 1)) { return r; };
	    if ((j+1) > (_LenSeqB() - 1)) { return r; };	
	    r.Right    = m[i+1][j];
	    r.Below    = m[i][j+1];
	    r.Diagonal = m[i+1][j+1];	    
	    return r;
    }
	
	private boolean _InRange(int i, int j)
	{
	    return ( i < _LenSeqA() ) && ( j < _LenSeqB() );
	}
	
	private void _TraceBack(int i, int j, int n, int p, char direction)
    // Recursive function
	// JJA Implemented
    {
        //
        n++;
        //if (!_InRange(i,j)) return;
        boolean bR = false;
        boolean bB = false;      
        
        System.out.println("i=" + i + ", j=" + j + ", n=" + n + ", p=" + p + ", dir=" + direction);        
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        
        if (!_InRange(i,j)) 
        {
          // Recursion should stop as one of the indexers (i or j) is out of range
          // terminate the process by ensuring any sequence lengths are padded
          // with trailing '-' and matched up with the remaining trailing sequence
          if ( i >= _LenSeqA() )
          {
            lstR.get(p).SeqB = lstR.get(p).SeqB + SeqB.substring(j, _LenSeqB());            
            return;
          }
          
          if ( j >= _LenSeqB() ) 
          {
        	lstR.get(p).SeqA = lstR.get(p).SeqA + SeqA.substring(i, _LenSeqA());            
            return;
          }
        }
       

        TTraceBackScore TB = _TracebackScore(i,j);
        tmp[i][j] = true; // we have inspected this cell
        
        
        // Manages construction of the resultant alignment strings                
        switch (direction)
        {
        case  'r': // Copy character from one sequence pair item, insert gap into other
            addResultCharPair(p, SeqA.charAt(i), '-', true);            
        break;
        case 'd': // Copy character from each Sequence pair
            addResultCharPair(p, SeqA.charAt(i), SeqB.charAt(j), true);
        break;
        case  'b': // Copy character from one sequence pair item, insert gap into other       
        	addResultCharPair(p, '-', SeqA.charAt(j), true);          
        break;
        default: // '*' (same as diagonal move)        
        	addResultCharPair(p, SeqA.charAt(i), SeqB.charAt(j), true);
        } 
        

        // Manages recursion along different, "discovered" paths
        if ((TB.Diagonal >= TB.Right) && (TB.Diagonal >= TB.Below))
        {
          _TraceBack(i+1, j+1, n, p, 'd');
        } else
        {
          if (TB.Right > TB.Diagonal) { bR = true; }
          if (TB.Below > TB.Diagonal) { bB = true; }      
         
          if (bR && bB) 
          { // There becomes two pathways for the recursion
                // to occur down, p and p+1
            _TraceBack(i+1, j, n, p, 'r');
            addResultNewPath(n, p+1, true);
            _TraceBack(i, j+1, n, p+1, 'b');

          } else
          { // Recurse along same pathway, p
        	  if (bR) { _TraceBack(i+1, j, n, p, 'r'); }
              if (bB) { _TraceBack(i, j+1, n, p, 'b'); }
          }
         
        }
       
    }
	
	public void TraceBack()
	{
		//
		_TraceBack(0,0,0,0, '*');	
	}
	
	
		
	
}

