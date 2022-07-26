package machine.teaching;

import java.io.IOException;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
//import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogService {
	private static Logger logger;
	
	public LogService(String dir, String name) {
		if (logger==null) {
    		logger = Logger.getLogger("Log WainsCoats."); 
//    		String pathLog = ".//log//WCLog.log";
      	  	String pathLog = dir + "//"+ name;
    		try {      

      	      FileHandler fhandler = new FileHandler(pathLog);  
      	      logger.addHandler(fhandler);
      	      SimpleFormatter formatter = new SimpleFormatter();  
      	      fhandler.setFormatter(formatter);  

      	  	} catch (SecurityException e){  
      	      e.printStackTrace();  
      	  	} catch (IOException e){  
      	      e.printStackTrace();  
      	  	} 
    	}	
	}


	
	public static void logInfo(String message) {
		logger.info(message); 
	}
	
//	public static void main(String[] args) {
//	  Logger logger = Logger.getLogger("LogdeManuel");      
//	  String pathLog = ".//log//MiLog.log";
//	  try {      
//
//	      FileHandler fhandler = new FileHandler(pathLog);  
//	      logger.addHandler(fhandler);
//	      SimpleFormatter formatter = new SimpleFormatter();  
//	      fhandler.setFormatter(formatter);  
//	      logger.info("Probando info!");
//	      logger.warning("Probando el warning");
//
//	  } catch (SecurityException e){  
//	      e.printStackTrace();  
//	  } catch (IOException e){  
//	      e.printStackTrace();  
//	  }      
//  }
}