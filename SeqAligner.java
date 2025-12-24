public class SeqAligner {
	
	static TNWSeqAlign NW;
	static TSWSeqAlign SW;
	

public static void main(String[] args) {
    String File1 = "", File2 = "";

    // Internal Test Sequences (used if no files specified)
    String SeqA = "ABCNYRCKLCRPMNP";
    String SeqB = "AYCYNRCCRBPM";

    // Show usage help
    if (args.length == 0 || (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help")))) {
        System.out.println("Needleman-Wunsch and Smith-Waterman Aligner");
        System.out.println("Developed by Dr Jamie Alnasir (2013, during Ph.D.)\n");
        System.out.println("Usage: java NWAlign <seq1.fasta> <seq2.fasta>");
        System.out.println("If no arguments are provided, default test sequences will be used.\n");
    }
    
    if ( !( (args.length == 0) || (args.length == 2) ) )
    {
        return;
    }

    // Load sequences from files
    if (args.length == 2) {
        File1 = args[0];
        File2 = args[1];

        TNWSeqAlign.TFastaFile SeqFile1 = new TNWSeqAlign.TFastaFile();
        TNWSeqAlign.TFastaFile SeqFile2 = new TNWSeqAlign.TFastaFile();

        SeqFile1.LoadFile(File1);
        SeqFile2.LoadFile(File2);

        SeqA = SeqFile1.Seq;
        SeqB = SeqFile2.Seq;
    }

    System.out.println("NEEDLEMAN-WUNSCH");

    NW = new TNWSeqAlign(SeqA, SeqB);

    NW.ExecSimilarScore();
    System.out.println("Similarity");
    PrintM(NW);

    NW.ExecScore();
    System.out.println("Matrix Scoring");
    PrintM(NW);

    System.out.println("Traceback");
    NW.TraceBack();
    System.out.println("");
    PrintM(NW);

    System.out.println("Results:\n");

    for (int x = 0; x < NW.lstR.size(); x++) {
        NW.lstR.get(x).MatchPairLen();
        System.out.println(NW.lstR.get(x).SeqA);
        System.out.println(NW.lstR.get(x).SandwichStr());
        System.out.println(NW.lstR.get(x).SeqB);
        System.out.println();
    }

    System.out.println("SMITH-WATERMAN");

    SW = new TSWSeqAlign(SeqA, SeqB);

    SW.ExecScore();
    System.out.println("Matrix Scoring");
    PrintM(SW);

    System.out.println("Traceback");
    SW.TraceBack();
    System.out.println();
    PrintM(SW);
}

	
	public static void PrintM(TSeqAligner SA)
	{
	
		for (int j = 0; j < SA._LenSeqB(); j++)
		{		  			
		  for (int i=0; i < SA._LenSeqA(); i++)
		  {
			  if (SA.tmp[i][j])
			  {
				  System.out.print(SA.m[i][j] + "*");
			  } else
			  {
				  System.out.print(SA.m[i][j] + " ");  
			  }			  			  
		  }
		  System.out.println("");
		}		
		System.out.println("");		
	}

}
