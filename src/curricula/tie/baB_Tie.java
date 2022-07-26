package curricula.tie;

import java.util.ArrayList;
import java.util.List;

import machine.teaching.Learner;
import machine.teaching.LogService;
import machine.teaching.Teacher;

public class baB_Tie {

	public static void main(String[] args) {
		// We intialize Logger
    	LogService logService = new LogService(".//log", "baB_Tie.log");
    	// System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");
    	
        // We define the following programs, which are the first program of the concepts that they define        
        String p_a = "RURD)";
        String p_b = "RRURD)";
        String p_c = "R)URD)";
        List<String> concepts = new ArrayList<String>();
//        concepts.add(p_a);
//        concepts.add(p_b);
        concepts.add(p_c);
     
        LogService.logInfo("Concepts to identify: ");
        for (int i=0; i<concepts.size(); i++){
             if (concepts.get(i).equals("RURD)")) {
            	 LogService.logInfo("| p_a = RURD) |");
            } else if (concepts.get(i).equals("RRURD)")){
            	LogService.logInfo("| p_b = RRURD) |");
            } else {
            	LogService.logInfo("| p_c = R)URD) |");
            }
        }
        LogService.logInfo(".");
        
        /** Uncomment the programs that you want to add to the library. */
        ArrayList<String> library = new ArrayList<String>();
        library.add(p_b);
        library.add(p_a);
//        library.add(p_c);
        LogService.logInfo("Library provided: ");
        for (int i=0; i<library.size(); i++){
            if (library.get(i).equals("RURD)")) {
            	LogService.logInfo("| p_a = RURD) |");
            } else if (library.get(i).equals("RRURD)")){
            	LogService.logInfo("| p_b = RRURD) |");
            } else {
            	LogService.logInfo("| p_c = R)URD) |");
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
