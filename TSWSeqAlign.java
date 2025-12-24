// Smith-Waterman LOCAL Sequence Alignment Algorithm --------------------------- 
// Class to implement Smith-Waterman local sequence alignment algorithm 
// Written by Jamie Al-Nasir 2013 
  
import java.awt.Point; 
import java.util.ArrayList; 
import java.util.List; 
  
public class TSWSeqAlign extends TSeqAligner { 
      
    // Scoring 
    int _match = +5; 
    int _mismatch = -3;  
    int _gap = -4;
    int maxP = 0; // stores the latest path
      
      
    public TSWSeqAlign(String aSeqA, String aSeqB) { 
        super(aSeqA, aSeqB); 
        // TODO Auto-generated constructor stub 
    } 
      
      
    private int _wScore(char SeqA, char SeqB) 
    // Match = +5, Mismatch = -3, Gap penalty = -4 
    { 
        if (SeqA == SeqB) { return _match; } else { return _mismatch; } 
    } 
      
    public void ExecScore() 
    // JJA Implemented 
    // Score the matrix 
    { 
        int a,b,c; 
    
        // Fill first row and column of matrix with 0s 
        for (int i = 0; i < _LenSeqA(); i++) 
        { 
            m[0][0] = 0; 
        } 
        for (int j = 0; j < _LenSeqB(); j++) 
        { 
            m[0][j] = 0; 
        } 
          
        // Each cell is processed wrt others as follows: - 
        // Mi,j = Max [ Mi-1,j-1 + S(i,j)  ,  Mi,j-1 + W  ,  Mi-1,j + W  ,  0 ] 
          
          
        for (int i=1; i < _LenSeqA(); i++) 
        {        
            for (int j=1; j < _LenSeqB(); j++) 
            { 
                a = m[i-1][j-1] + _wScore(SeqA.charAt(i), SeqB.charAt(j)); 
                b = m[i][j-1] + _gap; 
                c = m[i-1][j] + _gap; 
  
                m[i][j] = Math.max(Math.max(Math.max(a,b),c), 0); 
            } 
        } 
    } 
      
      
  
  
    private boolean _InRange(int i, int j) 
    { 
      return   ( i < _LenSeqA() ) && ( j < _LenSeqB() ) 
            && ( i > 0 ) && ( j > 0 ); 
    } 
  
  
    private TTraceBackScore2 _TracebackScore(int i, int j) 
    { 
        TTraceBackScore2 r = new TTraceBackScore2(); 
        r.Left     = 0; 
        r.Diagonal = 0; 
        r.Up       = 0; 
  
        // Basic bounds check 
        if (i-1 == 0) { return r; } 
        if (j-1 == 0) { return r; } 
  
        r.Left    = m[i-1][j]; 
        r.Diagonal= m[i-1][j-1]; 
        r.Up      = m[i][j-1]; 
        return r; 
    }
    
    private boolean _AtEnd(int i, int j)
    {
    	return (_TracebackScore(i,j).Diagonal == 0)
    			&& (_TracebackScore(i,j).Up == 0)
    			&& (_TracebackScore(i,j).Left == 0);
    }    
  
// 
    private void _TraceBack(int i, int j, int n, int p, char direction)  
    { 
        TTraceBackScore2 TB2; 
        boolean bL, bU; 
        //if (!_InRange(i,j))  { return; }
        
        if (!_InRange(i,j) || _AtEnd(i,j))  
        { 
          // Recursion should stop as one of the indexers (i or j) is out of range 
          // terminate the process by ensuring any sequence lengths are padded 
          // with trailing '-' and matched up with the remaining trailing sequence 
          
            lstR.get(p).SeqA = SeqA.substring(0, i+1) + lstR.get(p).SeqA;             
            lstR.get(p).SeqB = SeqB.substring(0, j+1) + lstR.get(p).SeqB;
            return; 
          
        } 
        
        if (p > maxP) { maxP = p; }
          
        System.out.println("i=" + i + ", j=" + j + ", n=" + n + ", p=" + p + ", dir=" + direction); 
          
        tmp[i][j] = true;        
        n++; 
          
        bL = false; 
        bU = false; 
        TB2 = _TracebackScore(i, j); 
          
        switch (direction) 
        { 
        case  'l': // Copy character from one sequence pair item, insert gap into other 
            addResultCharPair(p, SeqA.charAt(i), '-', false);             
        break; 
        case 'd': // Copy character from each Sequence pair 
            addResultCharPair(p, SeqA.charAt(i), SeqB.charAt(j), false); 
        break; 
        case  'u': // Copy character from one sequence pair item, insert gap into other        
            addResultCharPair(p, '-', SeqB.charAt(j), false);           
        break; 
        default: // '*' (same as diagonal move)         
            addResultCharPair(p, SeqA.charAt(i), SeqB.charAt(j), false); 
        }   
  
        if ((TB2.Diagonal > TB2.Left) && (TB2.Diagonal > TB2.Up))  
        { 
            _TraceBack(i-1, j-1, n, p, 'd'); 
        } 
  
        if (TB2.Left > TB2.Diagonal)  { bL = true; } 
        if (TB2.Up   > TB2.Diagonal)  { bU = true; } 
  
        if (bL && bU)  
        {  //There becomes two pathways for the recursion 
           // to occur down, p and p+1 
            _TraceBack(i-1, j, n, p, 'l'); 
            addResultNewPath(n, p+1, false); 
            _TraceBack(i, j-1, n, p+1, 'u'); 
        } else
        {  // Recurse along same pathway, p 
          if (bL)  { _TraceBack(i-1, j, n, p, 'l'); } 
          if (bU)  { _TraceBack(i, j-1, n, p, 'u'); } 
        } 
      
    } 
  
    
    public void TraceBack() 
    { 
        List<Point> lstMax = new ArrayList<Point>(); 
        int max = 0;         
  
        // Obtain highest scoring cells from the matrix, store 
        // in arrMax for subsequent tracebacks starting at each 
        // cell item of arrMax 
        for (int j =_LenSeqB() - 1; j > 0; j--) 
        { 
            for (int i =_LenSeqA() - 1; i > 0; i--) 
            { 
                if (m[i][j] >= max) 
                { 
                  max = m[i][j]; 
                  lstMax.add( new Point(i,j) );            
                } 
            } 
        } 
  
        // Perform multiple Tracebacks on highest scoring cells in the arrMax array 
          
//        for (Point startP : lstMax) 
//        { 
//            //System.out.println(item); 
//            _TraceBack(startP.x, startP.y, 0, maxP, '*'); 
//        }
        
        _TraceBack(_LenSeqA()-1, _LenSeqB()-1, 0, maxP, '*');
  
  
  } 
  
  
  
} 