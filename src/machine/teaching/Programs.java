/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.teaching;

import java.util.List;

/**
 *
 * @author Manuel.GPiqueras
 */
public class Programs {
	// WATCH OUT!! THESE PROPERTIES SHOULD BE CHANGED AND DEPEND ON THE NUMBER OF INSTRUCTIONS AND THE SIZE OF THE LIBRARY, RESPECTIVELY!!
	public static int NUM_BITS_PER_INSTRUCTION = 3;
	public static int BITS_RESERVED_FOR_LIBRARY_CALLS;
	
    // This is the maximum size of the programs generated by the learner
    //private int maxLengthPrograms;
    // This is a List for the programs
    private List<Program> programs;
    // This is the library that generated the programs
    private Library library;
    
    
     public Programs(List<Program> programs, Library library){
         //this.maxLengthPrograms = maxLengthPrograms;
         this.programs = programs;
         this.library = library;
         BITS_RESERVED_FOR_LIBRARY_CALLS = library.getNumberOfBits();
     }
     
     public int getSize(){
         if (programs!=null && programs.size()>0) {
                return programs.size();
         } else {
                return 0;
         }
     }
     
     /**
     *
     */
    public Program get(int i){
        Program result = null;
        
        if (programs!=null && programs.size()>0) {

           result = (Program) programs.get(i);
//           System.out.println("Result folded: programs.get("+i+") -->"+result);
           
        }
        return result;
    }

     /**
      *
      */
     public String getFolded(int i){
         String result = "";
         
         if (programs!=null && programs.size()>0) {

            result = (String) (programs.get(i)).getFoldedProgram();
//            System.out.println("Result folded: programs.get("+i+") -->"+result);
            
         }
         return result;
     }
     
     
     /**
      *
      */
     public String getUnfolded(int i){
         String result = "";
//         System.out.println("Result: get("+i+")");
         
         if (programs!=null && programs.size()>0) {

            result = (String) (programs.get(i)).getFoldedProgram();
//            System.out.println("Result: programs.get("+i+") -->"+result);
            
         }
         
         while (result.contains("@")){
             String target;
             int beginning = result.indexOf("@");
             target = result.substring(beginning, 1+beginning+library.getNumberOfBits());
//             System.out.println("target: "+target);
            
             int binary = -1;
             if( library.getSize()>1 ){
                 binary = Integer.valueOf(target.substring(1));
             }  
//             System.out.println("binary: "+binary);
             if (binary == -1){
                 result = result.replace(target, library.get(0));
             } else {
                 result = result.replace(target, library.get(Library.BinaryToDecimal(binary)));
             }
//             System.out.println("Result.replace -> "+result);
             
         }
         
         return result;
     }
     
     public static int getBits(Program program) {
 		int numBits = -1;
 		//System.out.println("Library size:"+library.getSize());
 		
 		if(program.getLibrarySize()<2) {
 			numBits = NUM_BITS_PER_INSTRUCTION * program.getFoldedProgram().length(); 
 		}else {
 			
 			int libraryCalls = (int) program.getFoldedProgram().chars().filter(ch -> ch == '@').count();
 			
 			// We add the bits dedicated to each '@' instruction
 			numBits = libraryCalls * NUM_BITS_PER_INSTRUCTION;
 			
 			// We now add the bits necessary to identify a primitive in the library
 			int numBits2 = numBits + (libraryCalls * BITS_RESERVED_FOR_LIBRARY_CALLS);
 			
 			// We now add the bits dedicated to the rest of the instructions
 			int numRestInstructions = program.getFoldedProgram().length() - (libraryCalls + (libraryCalls* BITS_RESERVED_FOR_LIBRARY_CALLS));
 			numBits  = numBits2 + (numRestInstructions * NUM_BITS_PER_INSTRUCTION);
 			
 		}
 		
 		return numBits;
 	}
     
     public boolean isPrevious(int program1, int program2){
         
         return false;
     }
     
     
     public static void main (String[] args){
         
//      String result = "@000UUU@001DDD@001@101RRR@111)";
//      String result = "";
//    	String result = "RURDR@1";

//        System.out.println("Library substitution for: "+result);
//        while (result.contains("@")){
//             String target;
//             int beginning = result.indexOf("@");
//             System.out.println("Beginning: "+beginning);
//             target = result.substring(beginning, 1+beginning+3);
//             System.out.println("Target: "+target);
//             int binary = Integer.valueOf(target.substring(1));
//             System.out.println("binary: "+binary);
//             System.out.println("Replace by :"+ Library.BinaryToDecimal(binary));
//             result = result.replace(target, "_i_");
//             
//        }
//        
//        System.out.println("Result: "+result);
        
    	Programs.BITS_RESERVED_FOR_LIBRARY_CALLS = (int) Math.ceil( (Math.log(3) / Math.log(2)) );
    	 
    	int numBits = -1;
  		String program = "RDR@01";
//  		String program2 = "R)URD)";
  		
  			int libraryCalls = (int) program.chars().filter(ch -> ch == '@').count();
  			
  			// We add the bits dedicated to each '@' instruction
  			numBits = libraryCalls * NUM_BITS_PER_INSTRUCTION;
  			System.out.println("$$ numBits:"+numBits);
  			
  			// We now add the bits necessary to identify a primitive in the library
  			int numBits2 = numBits + (libraryCalls * BITS_RESERVED_FOR_LIBRARY_CALLS);
  			System.out.println("$$ numBits2:"+numBits2);
  			
  			
  			// We now add the bits dedicated to the rest of the instructions
  			int numRestInstructions = program.length() - (libraryCalls + (libraryCalls* BITS_RESERVED_FOR_LIBRARY_CALLS));
  			int numBits3 = numBits2 + (numRestInstructions * NUM_BITS_PER_INSTRUCTION);
  			System.out.println("$$ numBits3:"+numBits3);
  			

        
//        System.out.println("ConceptAux 'RURDR@1' #bits-->"+learner.getPrograms().getBits("RURDR@1"));
//        System.out.println("Concept 'R)URD)' #bits-->"+learner.getPrograms().getBits("R)URD)"));
        
         
     }
}