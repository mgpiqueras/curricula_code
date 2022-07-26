package machine.teaching;


import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
 
//import org.jooq.lambda.Seq;
import java.util.Collection;
//import java.util.Collections;
import java.util.stream.IntStream;
//import java.util.*;
import java.util.stream.*;
import java.util.function.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manuel.GPiqueras
 */
public class Learner {
    // This arraylist contains the instructions employed by the learner
    private ArrayList<String> alphabet;
    // This is the number of instructions of the language on the learner's side 
    // which is not related to library calls
    private static final int NUM_PROPER_INSTRUCTIONS = 5;


    
//    // This is the maximum size of the programs generated by the learner
//    private int maxLengthPrograms;

//    // This is a List for the programs
//    private List<String> programs;
    Programs programs;
    
    // Null program
    public static final String NULL_PROGRAM = "NULL_PROGRAM";
    // A pencil which makes 'drawings'
    private Pencil pencil;
    
// A library of primitives which reflect background knowledge
    private Library library;
    
    /** 
     * At the beginning the library is empty, i.e., B={emptyset}, and we add the argument to the library.
     */
    public Learner(int maxLengthPrograms, List<String> primitives){
        this.library = new Library(primitives);
        
//        this.maxLengthPrograms = maxLengthPrograms;
        
    //this.library = library;
        //System.out.print("**** Maximum length programs: "+maxLengthPrograms);
        
        this.pencil = new Pencil();
        
        this.alphabet =  new ArrayList<String>();//Arrays.asList("U", "D", "R", "L", ")");
        this.alphabet.add("U");
        this.alphabet.add("D");
        this.alphabet.add("R");
        this.alphabet.add("L");
        this.alphabet.add(")");
        //System.out.print("**** Hemos inicializado la libreria.");
        //System.out.print("**** Agregamos a la libreria: ");
        if (library.getSize()>0){ 
            for (int i=0; i<library.getSize(); i++){
                LogService.logInfo("| "+library.getIdPrimitive(i, library.getSize())+
                        " = " + primitives.get(i) + " |");
                this.alphabet.add(library.getIdPrimitive(i, library.getSize()));
            }
        }
       
        //Recogemos los programas y los ordenamos
        List<Program> programList = (List<Program>) generatePrograms(maxLengthPrograms);
        Collections.sort(programList);
        
        //System.out.println("Ahora generamos los programas...");
        this.programs = new Programs(programList, library);
        
    
        // Print the programs
//        for (int i =0; i<this.programs.getSize(); i++){
//            System.out.println("$_>"+this.programs.getFolded(i));
//            //if(this.programs.getFolded(i).equals("R@1")) System.out.println("$$$$PROGRAM: R@1->"+ i);
//        }
//    
    
    }
    
    public List<String> getInstructionsLanguage() {
        return this.getAlphabet();
    }
    
    /** 
     * "Misha's function" will create a stream of combinations of its first argument with the given stream, where the mode of combination can be externally specified as well.
     * We have employed the solution given here: http://sebastian-millies.blogspot.com/2015/09/cartesian-products-with-kleisli.html.
     * 
     * @param <T>
     * @param <U>
     * @param <R>
     * @param streamSupplier
     * @param combiner
     * @return 
     */
    <T, U, R> Function<T, Stream<R>> crossJoin(Supplier<Stream<? extends U>> streamSupplier, BiFunction<? super T, ? super U, ? extends R> combiner) {
    return t -> streamSupplier.get().map(u -> combiner.apply(t, u));
    }

    /**
     * This method generates all possible programs that can be created using the alphabet of this class (Learner), whose length are equal to argument n.
     * We have employed the solution given here: http://sebastian-millies.blogspot.com/2015/09/cartesian-products-with-kleisli.html
     * 
     * @param n the numbers of factors for the cartesian product.
     * @param orderedSet an ordered set that is the base of the cartesian product.
     * @return the nth-cartesian product of the ordered set given in the argument 
     */
    List<String> cartesian(int n, Collection<String> orderedSet) {
        
    BinaryOperator<Function<String,Stream<String>>> kleisli = (f,g) -> s -> f.apply(s).flatMap(g);    
    
    return orderedSet.stream()
           .flatMap( IntStream.range(1, n).boxed()
                     .map(_any -> crossJoin(orderedSet::stream, String::concat)) // create (n-1) appropriate crossJoin instances
                     .reduce(s -> Stream.of(s), kleisli)                   // compose them sequentially with >=>
                    )                                                      // flatMap the stream with the entire function chain
           .collect(Collectors.toList());
    }
    
    /**
     * This method generates all possible programs that can be created using the alphabet of this class (Learner).
     * 
     * @param maxLength The maximum lengths of the programs generated.
     * @return all the programs that can be built using the alphabet of this class of size smaller or equal than the argument maxLength  
     */
    public List<Program> generatePrograms(int maxLength){
        
        ArrayList<Program> result = new ArrayList<Program>();
        for (int i=1; i<=maxLength; i++){
            ArrayList<String> partialResult = (ArrayList<String>) cartesian(i, this.getAlphabet());
            for(int j=0; j<partialResult.size(); j++){
            	// Now we create a program and add it to the List
            	Program p = new Program (partialResult.get(j), this.library.getSize());
                result.add(p);
            }
        }
        
        // Print the programs
//        for(int i=0;i<result.size();i++) System.out.println(result.get(i));
        
        return result;
    }
    
    
     /**
     * The learner returns an example if it identifies a concept compatible with that concept; otherwise, it returns no concept ("this.NULL_PROGRAM").
     * @param ws A regular witness set, i.e., a witness set whose examples are all positive or negative
     * @return 
     */
    public int getConcept(WitnessSet ws){
        int result = -1;
//        System.out.println("Witness set: "+ws);
        ArrayList<ioExample> positiveExs = new ArrayList<ioExample>();
        ArrayList<ioExample> negativeExs = new ArrayList<ioExample>();
        
        //We get the examples
        ioExample[] exs = ws.getExamples();
        if(exs == null || exs.length == 0) return result;
        
        //Now we divide the examples into positive and negative
        for (ioExample ex : exs) {
            if (ex.getPositive()) {
                positiveExs.add(ex);        
            } else {
                negativeExs.add(ex);
            }
        }
        
        //Now we exclude the existence of incongruent witness sets such as {<E+>, <E->}
        for (int i=0; i<positiveExs.size(); i++){
            String positivePath = (positiveExs.get(i)).getPath();
            for (int j=0; j<negativeExs.size(); j++){
                String negativePath = (negativeExs.get(j)).getPath();
                if ( positivePath.equals(negativePath) ) {
                    return result;
                }
            }
        }
        
        // Now we get the concepts. We distinguish between regular and not regular witness sets
//        System.out.println("positiveExs.size(): "+positiveExs.size()); 
//        System.out.println("negativeExs.size(): "+negativeExs.size()); 
        if( positiveExs.size() == exs.length || negativeExs.size() == exs.length){
            //This is a regular witness set 
//        	int indexProgramIdentified = getConceptRegularWitnessSet(exs);
//        	if (indexProgramIdentified == -1) result = NULL_PROGRAM;
//        	else result = this.programs.getFolded(indexProgramIdentified);
        	result = getConceptRegularWitnessSet(exs);
        }else{
            //This is NOT a regular witness set
            ioExample[] pexs = new ioExample[positiveExs.size()];// positiveExs.toArray();  
            ioExample[] nexs = new ioExample[negativeExs.size()];// negativeExs.toArray();  
            for (int i=0; i<positiveExs.size(); i++) {
                pexs[i] = positiveExs.get(i);
            }
            for (int j=0; j<negativeExs.size(); j++) {
                nexs[j] = negativeExs.get(j);
            }
            
//          int indexProgramIdentified = getConceptNotRegularWitnessSet(pexs, nexs);
//        	if (indexProgramIdentified == -1) result = NULL_PROGRAM;
//        	else result = this.programs.getFolded(indexProgramIdentified);
            result = getConceptNotRegularWitnessSet(pexs, nexs);
        }
        
        return result;
    }
        
    
    /**
     * The learner returns a concept if it identifies a program compatible with the examples given; otherwise, it returns nothing (-1).
     * @param ioexs An array with the examples of a regular witness set, i.e., a witness set whose examples are all positive or negative
     * @param iStart The index of the position in the array of programs where the search starts
     * @return The index of the program identified in the array 'programs'
     */
    public int getConceptRegularWitnessSet(ioExample[] ioexs, int iStart){
    	int indexConcept = -1;
    	if(iStart==-1) {
//    		System.out.println("OJOOOOOO");
    		return -1;
    	}
    	//String concept = NULL_PROGRAM;
        ArrayList<Integer> conceptsIdentified = new ArrayList<Integer>();
        
        // We get the examples given in the arguments one by one
        for(int j=0; j<ioexs.length; j++){ 
            //concept = NULL_PROGRAM;
            indexConcept = -1;

          //Now we try with the programs stored in the learner
            for(int i=iStart;i<this.programs.getSize();i++) {
                // If the program contains two contiguous brackets then it is not valid
                if (this.programs.getFolded(i).contains("))")) continue;
                
                //System.out.println(programs.get(i));
                String concept = programs.getUnfolded(i);            
                boolean isCompatible = pencil.isExampleCompatible(ioexs[j], concept);

                if (isCompatible) {
                  //System.out.println("####--Program '"+concept+"' is compatible with example '"+ioexs[j].getPath()+"'!!!");
                  indexConcept = i;
                  break;
                }
            }
            conceptsIdentified.add(indexConcept);
        }   
        
//        System.out.println("#####Concepts identified:");
//        for (int m=0; m<conceptsIdentified.size(); m++) {
//        	String p = programs.getFolded(conceptsIdentified.get(m));
//        	System.out.println("##Concept("+m+")->"+p);
//        }
        
        // Now we check if all the programs identified coincide or not
        boolean coincideConcepts = true;
        for (int k=1; k<conceptsIdentified.size(); k++){
            if (!conceptsIdentified.get(k-1).equals(conceptsIdentified.get(k))){
                coincideConcepts = false;
            }
        }
        
        int maxIndex = -1;
        boolean maxValid = false;
        if (!coincideConcepts) {
        	maxIndex = Collections.max(conceptsIdentified);
        	for(int k=0; k<ioexs.length; k++){
        		String p = programs.getUnfolded(maxIndex);  
        		//System.out.println("Program: "+p);
            	maxValid = pencil.isExampleCompatible(ioexs[k], p);
            	//System.out.println("Is Valid? "+maxValid);
            	if(!maxValid) break; 
        	}
        	//System.out.println("maxVAlid trueeeee");
        }
        
        if (coincideConcepts) {
            //System.out.println("#####Devolvemos..." + conceptsIdentified.get(0));
        	return conceptsIdentified.get(0);
        } else if(maxValid) {
            //System.out.print("#####Devolvemos...");
            //System.out.println("-->"+maxIndex);
        	return maxIndex;
        } else {
        	return -1;
        }
    }
    
    /**
     * The learner returns a concept if it identifies a program compatible with the examples given; otherwise, it returns nothing (-1).
     * @param ioexs An array with the examples of a regular witness set, i.e., a witness set whose examples are all positive or negative
     * @return The index of the program identified in the array 'programs'
     */
    public int getConceptRegularWitnessSet(ioExample[] ioexs){
    	return getConceptRegularWitnessSet(ioexs, 0);
    }
    
    /**
     * The learner returns an example for a witness set which has positive and negative examples, i.e., a witness set which is not regular.
     * If the learner identifies a concept compatible with any positive example, then it checks whether that concept is compatible
     * with any given negative example. In such a case, the learner continues its search for another concept compatible with the positive example,
     * that is not compatible with the path expressed by any negative example given; otherwise, it returns no concept (index '-1').
     * @param positiveExs An array with the positive examples of a witness set. 
     * @param negativeExs An array with the negative examples of a witness set. 
     * @return The index in the array 'programs' of the program identified: a program identified by a not regular witness set whose positive and negative examples are in the arguments. 
     * If it does not identify a concept it returns "-1".
     */
    public int getConceptNotRegularWitnessSet(ioExample[] positiveExs, ioExample[] negativeExs){
     
     int iPositive = getConceptRegularWitnessSet(positiveExs);
     //if (iPositive ==-1) return -1;
	 int iNegative = getConceptRegularWitnessSet(negativeExs, iPositive);
	 
	 while ( (iPositive != iNegative) && (iPositive<(programs.getSize()-1)) ){ 	
		 iPositive = getConceptRegularWitnessSet(positiveExs, iPositive+1);
		 if(iPositive == -1) break;
		 iNegative = getConceptRegularWitnessSet(negativeExs, iPositive);
	 }
	 
	 if (iPositive == iNegative) return iPositive;
	 else return -1;
    }

    
    
    
    /**
     * @return the alphabet
     */
    public List<String> getAlphabet() {
        return alphabet;
    }

    /**
     * @return the NUM_PROPER_INSTRUCTIONS
     */
    public static int getNUM_PROPER_INSTRUCTIONS() {
        return NUM_PROPER_INSTRUCTIONS;
    }
    
    public Programs getPrograms() {
    	return this.programs;
    }

    
    public static void main (String[] args){
    	
    	LogService logService = new LogService(".//log", "dacB_b.txt");
    	
    	// We define the following programs, which are the first program of the concepts that they define        
        String p_a = "RURD)";
        String p_b = "RRURD)";
//        String p_ba = "R@1";
        String p_c = "R)URD)";
        String p_d = "RD)";

        List<String> concepts = new ArrayList<String>();
        concepts.add(p_a);
        concepts.add(p_b);
//        concepts.add(p_ba);
        concepts.add(p_c);
        concepts.add(p_d);
        
        LogService.logInfo("Concepts to identify: ");
        for (int i=0; i<concepts.size(); i++){
             if (concepts.get(i).equals("RURD)")) {
            	 LogService.logInfo("| p_a = RURD) |");
            } else if (concepts.get(i).equals("RRURD)")){
            	LogService.logInfo("| p_b = RRURD) |");
            } else if (concepts.get(i).equals("R)URD)")){
            	LogService.logInfo("| p_d = R)URD) |");
            } else {
            	LogService.logInfo("| p_c = RD) |");
            }
        }
        LogService.logInfo(".");
        
        /** Uncomment the programs that you want to add to the library. */
        ArrayList<String> primitives = new ArrayList<String>();
//        primitives.add(p_c);
//        primitives.add(p_d);
//        primitives.add(p_a);
//        primitives.add(p_c);
        
        LogService.logInfo("Library provided: ");
        for (int i=0; i<primitives.size(); i++){
            if (primitives.get(i).equals("RURD)")) {
            	LogService.logInfo("| p_a = RURD) |");
            } else if (primitives.get(i).equals("RRURD)")){
            	LogService.logInfo("| p_b = RRURD) |");
            } else if (primitives.get(i).equals("R)URD)")){
            	LogService.logInfo("| p_c = R)URD) |");
            } else {
            	LogService.logInfo("| p_d = RD) |");
            }
        }
        LogService.logInfo(".");
       
        
        LogService.logInfo("We instantiate the learner.");
        //Learner learner = new Learner(6, primitives);
        Learner learner = new Learner(6, primitives);
        
        
//        ioExample[] ioexs1 = new ioExample[1];
//        ioexs1[0] = new ioExample("ENESEEE", true);
//        ioexs1[1] = new ioExample("E", false);
//        ioexs1[2] = new ioExample("EE", true);
//        WitnessSet w1 = new WitnessSet(ioexs1);
//        int i1 = learner.getConcept(w1);
//        LogService.logInfo("Id program--->"+i1);
//      	LogService.logInfo("The learner identifes concept '"+learner.programs.getFolded(i1)+"' on input :"+w1);
//        
//        ioExample[] ioexs2 = new ioExample[2];
//        ioexs2[0] = new ioExample("EENESEE", true);
//        ioexs2[1] = new ioExample("EEE", false);
//        WitnessSet w2 = new WitnessSet(ioexs2);
//        int i2 = learner.getConcept(w2);
//        LogService.logInfo("Id program--->"+i2);
//      	LogService.logInfo("The learner identifes concept '"+learner.programs.getFolded(i2)+"' on input :"+w2);


        ioExample[] ioexs3 = new ioExample[2];        
//        ioexs3[0] = new ioExample("EENE", true); 
//        ioexs3[1] = new ioExample("EEE", false);

        ioexs3[0] = new ioExample("WW", true);
        ioexs3[1] = new ioExample("N", true);
        
        //CREO QUE PARA EVITAR ESTO NECESITAMOS QUE LOS EJEMPLOS POSITIVOS SE PUEDAN PONER COMO SUBCADENAS ENTRE S�

        
        WitnessSet w3 = new WitnessSet(ioexs3);
        int i3 = learner.getConcept(w3);
        LogService.logInfo("Id program--->"+i3);
      	LogService.logInfo("The learner identifes concept '"+learner.programs.getFolded(i3)+"' on input :"+w3+"; Unfolded:"+learner.programs.getUnfolded(i3)+".");

//        ioExample[] ioexs4 = new ioExample[2];
//        ioexs4[0] = new ioExample("EENESENES", true); 
//        ioexs4[1] = new ioExample("W", false);
//        WitnessSet w4 = new WitnessSet(ioexs4);
//        int i4 = learner.getConcept(w4);
//        System.out.println("The learner identifes concept '"+learner.programs.getFolded(i4)+"' on input :"+w4);
        
//        
////        System.out.println("ConceptAux 'RURDR@1' #bits-->"+learner.getPrograms().getBits("RURDR@1"));
////        System.out.println("Concept 'R)URD)' #bits-->"+learner.getPrograms().getBits("R)URD)"));
////        
//        
//        ioExample[] ioexs5 = new ioExample[1];
//        ioexs5[0] = new ioExample("ENES", true); 
//        WitnessSet w5 = new WitnessSet(ioexs5);
//        int i5 = learner.getConcept(w5);
//        System.out.println("The learner identifes concept '"+learner.programs.getFolded(i5)+"' on input :"+w5);
//         
//        ioExample[] ioexs6 = new ioExample[1];
//        ioexs6[0] = new ioExample("ENESEN", true); 
//        WitnessSet w6 = new WitnessSet(ioexs6);
//        int i6 = learner.getConcept(w6);
//        LogService.logInfo("The learner identifes concept '"+learner.programs.getFolded(i6)+"' on input :"+w6);
//      
//        Pencil pencil = new Pencil();
//        String p1 = "R)RURD)";
//        String p2 = "R)URD)";
//        
//        
//        boolean p1_compatible = pencil.isExampleCompatible(ioexs3[0], p1);
//        System.out.println("Is "+p1+" compatible with+"+ioexs3[0]+"? "+p1_compatible);
//        boolean p2_compatible = pencil.isExampleCompatible(ioexs3[0], p2);
//        System.out.println("Is "+p2+" compatible with+"+ioexs3[0]+"? "+p2_compatible);
        

    }
}