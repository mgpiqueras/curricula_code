package curricula.four.BBBi;


import java.util.ArrayList;
import java.util.List;

import machine.teaching.Learner;
import machine.teaching.LogService;
import machine.teaching.Teacher;

public class adcB_Four_b {
	public static void main(String[] args) {
		// We initialize Logger
		LogService logService = new LogService(".//log", "adcB_Four_b.txt");
    	
    	// We define the following programs, which are the first program of the concepts that they define        
        String p_a = "RURD)";
        String p_b = "RRURD)";
        String p_c = "R)URD)";
        String p_d = "RD)";
    	// System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");
    	
        
        List<String> concepts = new ArrayList<String>();
//        concepts.add(p_a);
        concepts.add(p_b);
//        concepts.add(p_c);
//        concepts.add(p_d);
        
     
        LogService.logInfo("Concepts to identify: ");
        for (int i=0; i<concepts.size(); i++){
             if (concepts.get(i).equals("RURD)")) {
            	 LogService.logInfo("| p_a = RURD) |");
            } else if (concepts.get(i).equals("RRURD)")){
            	LogService.logInfo("| p_b = RRURD) |");
            } else if (concepts.get(i).equals("R)URD)")){
            	LogService.logInfo("| p_c = R)URD) |");
            } else {
            	LogService.logInfo("| p_d = RD) |");
            }
        }
        LogService.logInfo(".");
        

        /** Uncomment the programs that you want to add to the library. */
        ArrayList<String> library = new ArrayList<String>();
        library.add(p_a);
        library.add(p_d);
        library.add(p_c);
        
        LogService.logInfo("Library provided: ");
        for (int i=0; i<library.size(); i++){
            if (library.get(i).equals("RURD)")) {
            	LogService.logInfo("| p_a = RURD) |");
            } else if (library.get(i).equals("RRURD)")){
            	LogService.logInfo("| p_b = RRURD) |");
            } else if (library.get(i).equals("R)URD)")){
            	LogService.logInfo("| p_c = R)URD) |");
            } else {
            	LogService.logInfo("| p_d = RD) |");
            }
        }
        LogService.logInfo(".");
        
   
        LogService.logInfo("We instantiate the teacher.");
        int maxSizeExamples = 9;
        Teacher teacher = new Teacher(maxSizeExamples);
        LogService.logInfo("We instantiate the learner.");
        Learner learner = new Learner(6, library);
        
        LogService.logInfo("Concepts identified by the learner:");
        teacher.provideExamples(learner, concepts);
        LogService.logInfo(".END");	
	}

}