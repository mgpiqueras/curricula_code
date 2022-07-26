/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.teaching;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.ArrayList;

//import org.jooq.lambda.Seq;


/**
 *
 * @author Manuel.GPiqueras
 */
public class Teacher2 {

private List<String> compass;
private List<String> paths;
private List<WitnessSet> witnessSets;





    /**
     * The teacher generates, within the constructor, examples of size between min/max args. 
     * Note that "E,," is the path "E" or "E,W" gives two paths "E" and "W". In other words,
     * a comma replaces two or more subsequent commas ",,...,", and a comma splits the path
     * into two parts. 
     * @param minSizeExamples The minimum length of the examples generated; note that commas are considered too.
     * @param maxSizeExamples The maximum length of the examples generated; note that commas are considered too.
     */
    public Teacher2(int maxSizeExamples) {
        
    	
        this.compass = Arrays.asList("N", "S", "E", "W", ",");
        
//        this.paths = Seq.rangeClosed(minSizeExamples, maxSizeExamples)
//            .flatMap(length ->
//                Seq.rangeClosed(1, length - 1)
//                    .foldLeft(Seq.seq(compass), (s, i) ->
//                        s.crossJoin(Seq.seq(compass))
//                            .map(t -> t.v1 + t.v2)))
//            .toList();//.forEach(System.out::println);
        
        this.paths = generatePaths(maxSizeExamples);
        
        // We have to change that to generate witness sets
        this.witnessSets = new ArrayList<>();
        
        for(int i=0;i<this.paths.size();i++){
//            System.out.println("@@@");
            String str = this.paths.get(i);
//            System.out.println("**str:"+str);
            while(str.contains(",,")){
                str = str.replace(",,", ",");
            }       
            if(str.startsWith(",")) str = str.substring(1);
            if(str.length()==0) continue;
            String[] arrOfStr = str.split(",");
//            System.out.println("@@@");
            // We create all the positive examples
            ioExample[] exsPositive = new ioExample[arrOfStr.length];
            for(int j=0; j<arrOfStr.length; j++){
                exsPositive[j] = new ioExample(arrOfStr[j], true);
            }
            WitnessSet wsPositive = new WitnessSet(exsPositive);
//            System.out.println("wsPositive: "+wsPositive);
            this.witnessSets.add(wsPositive);   
            
            // We build all the 'subsets' of the set of possible examples to make negative examples
            SubSets aux = new SubSets();
            List<Integer> list = new ArrayList<Integer>();
            for (int k=0; k<arrOfStr.length; k++){
                list.add(k);
            } 
            //We generate the 'subsets'
            List<List<Integer>> out = aux.allSubSets(list);
//            System.out.println(out);
            
            for (int m=0; m<out.size(); m++){
//                System.out.println("m: "+m);
                ioExample[] exsNegative = new ioExample[arrOfStr.length];
//                System.out.println("arrOfStr.length:"+arrOfStr.length);
                for (int n=0; n<arrOfStr.length; n++){
//                   System.out.println("&&&& n:"+n);
                   exsNegative[n] = new ioExample(arrOfStr[n], true);                   
                }
//                System.out.println("&&&& m:"+m);
                // Now we recover the distributions of the negative examples
                for (Integer q : out.get(m)) {
//                    System.out.println("**q:"+q);
                    exsNegative[q].setPositive(false);
                }
                
                // Finally, we generate the ArryaList<ioExample> and we put it in ioExamples            
                WitnessSet wsNegative = new WitnessSet(exsNegative);
//                System.out.println("wsNegative: "+wsNegative);
                this.witnessSets.add(wsNegative);   
//                System.out.println("....");
            }  
        }
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
     * This method generates all possible programs that can be created using the alphabet of this class (Teacher).
     * 
     * @param maxLength The maximum lengths of the programs generated.
     * @return all the examples that can be built using the alphabet of this class of size smaller or equal than the argument maxLength  
     */
    public List<String> generatePaths(int maxLength){
        
        ArrayList<String> result = new ArrayList<String>();
        for (int i=1; i<=maxLength; i++){
            ArrayList<String> partialResult = (ArrayList<String>) cartesian(i, this.compass);
            for(int j=0; j<partialResult.size(); j++){
            	// Now we create a program and add it to the List
//            	Program p = new Program (partialResult.get(j), this.library.getSize());
                result.add(partialResult.get(j));
            }
        }
        
        // Print the programs
//        for(int i=0;i<result.size();i++) System.out.println(result.get(i));
        
        return result;
    }

    
    
    /**
     * We provide the examples to the learner which may identify the concepts given as arguments.
     * @param learner
     * @param concepts 
     */
    public void provideExamples(Learner learner, List<String> concepts){
        for(int i=0;i<this.witnessSets.size();i++){ 
           int indexConcept = learner.getConcept(this.witnessSets.get(i));
           String conceptFolded = Learner.NULL_PROGRAM;
    	   String conceptUnfolded = Learner.NULL_PROGRAM;
    	   int bits = -1;
           if( indexConcept != -1 ) {
        	   conceptFolded = learner.getPrograms().getFolded(indexConcept);
        	   conceptUnfolded = learner.getPrograms().getUnfolded(indexConcept);
        	   bits = Programs.getBits(learner.getPrograms().get(indexConcept));
           }
          
           int sizeBitsWitnessSet = witnessSets.get(i).getSizeBits();
           
           if (sizeBitsWitnessSet > bits && bits !=-1) {
//        	   LogService.logInfo("OK!");
//        	   String m1 = witnessSets.get(i).toString()+", with bitsWS:"+sizeBitsWitnessSet+" ==> '"+conceptFolded+"', with #bitsP='"+bits+"' and Unfolded is '"+conceptUnfolded+"'";
//         	   LogService.logInfo(m1);
           
           } else if (bits == -1) {
        	   LogService.logInfo("NO PROGRAM IDENTIFIED");
        	   String m2 = witnessSets.get(i).toString()+", with bitsWS:"+sizeBitsWitnessSet+" ==> '"+conceptFolded+"', with #bitsP='"+bits+"' and Unfolded is '"+conceptUnfolded+"'";
         	   LogService.logInfo(m2);
         	   
           } else if (sizeBitsWitnessSet <= bits) {
        	   
        	   LogService.logInfo("@@@@   WARNING: WITNESS SET <= PROGRAM   @@@@");
        	   String m3 = witnessSets.get(i).toString()+", with bitsWS:"+sizeBitsWitnessSet+" ==> '"+conceptFolded+"', with #bitsP='"+bits+"' and Unfolded is '"+conceptUnfolded+"'";
         	   LogService.logInfo(m3);
           }
           //System.out.println(witnessSets.get(i).toString()+" ==> "+conceptFolded+" and Unfolded is: "+conceptUnfolded);
//     	   String message = witnessSets.get(i).toString()+" ==> '"+conceptFolded+"', with #bits='"+bits+"' and Unfolded is '"+conceptUnfolded+"'";
//     	   LogService.logInfo(message);
           
           
//////           System.out.println(witnessSets.get(i).toString()+" ==> "+concept);
//           if (concepts!=null){
//               for(int j=0; j<concepts.size(); j++){
//                   if ( conceptFolded.equals(concepts.get(j)) || conceptUnfolded.equals(concepts.get(j)) ){
//                       System.out.println(witnessSets.get(i).toString()+" ==> "+conceptFolded+" and Unfolded is: "+conceptUnfolded);
//                 	   String message = witnessSets.get(i).toString()+" ==> '"+conceptFolded+"', with #bits='"+bits+"' and Unfolded is '"+conceptUnfolded+"'";
//                 	   LogService.logInfo(message);
//                   }
//               }
//            } 
        }
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	// Inicializamos el Logger
    	
    	
    	LogService logService = new LogService(".//log", "Teacher2");
    	// System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");
    	
        // We define the following programs, which are the first program of the concepts that they define        
        String p_a = "RURD";
        String p_b = "RRURD";
        String p_ba = "R@1";
        String p_c = "R)URD)";
        List<String> concepts = new ArrayList<String>();
//        concepts.add(p_a);
//        concepts.add(p_b);
//        concepts.add(p_ba);
//        concepts.add(p_c);
     
        LogService.logInfo("Concepts to identify: ");
        for (int i=0; i<concepts.size(); i++){
             if (concepts.get(i).equals("RURD")) {
            	 LogService.logInfo("| p_a = RURD |");
            } else if (concepts.get(i).equals("RRURD")){
            	LogService.logInfo("| p_b = RRURD |");
            } else if (concepts.get(i).equals("R@1")){
            	LogService.logInfo("| p_b = R@1 |");
            } else {
            	LogService.logInfo("| p_c = R)URD) |");
            }
        }
        LogService.logInfo(".");
        
        /** Uncomment the programs that you want to add to the library. */
        ArrayList<String> library = new ArrayList<String>();
//        library.add(p_a);
//        library.add(p_b);
//        library.add(p_c);
        LogService.logInfo("Library provided: ");
        for (int i=0; i<library.size(); i++){
            if (library.get(i).equals("RURD")) {
            	LogService.logInfo("| p_a = RURD |");
            } else if (library.get(i).equals("RRURD")){
            	LogService.logInfo("| p_b = RRURD |");
            } else {
            	LogService.logInfo("| p_c = R)URD) |");
            }
        }
        LogService.logInfo(".");
        
   
        LogService.logInfo("We instantiate the teacher.");
        int maxSizeExamples = 9;
//        int minSizeExamples = 1;
////      //  The argument given is the max size of the examples generated in the Teacher constructor
//        Teacher teacher = new Teacher(9);

//      //  The teacher generates, within the constructor, examples of size between min/max args. 
//      //  Note that "E,," is the path "E" or "E,W" gives two paths "E" and "W". In other words,
//      //  a comma replaces two or more subsequent commas ",,...,", and a comma splits the path
//      //  into two parts.
        Teacher2 teacher = new Teacher2(maxSizeExamples);
        LogService.logInfo("We instantiate the learner.");
        Learner learner = new Learner(6, library);
        
        LogService.logInfo("Concepts identified by the learner:");
        teacher.provideExamples(learner, concepts);
        LogService.logInfo(".END");
        
    }//main  
}//class Teacher
