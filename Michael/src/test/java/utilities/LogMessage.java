package utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogMessage {
	private FileHandler handler = null;

	  private static Logger logger = Logger.getLogger(Logger.class.getName());

	  /**
	   * 
	   * @param pattern: file name and location
	   */
	  public LogMessage(String pattern) {
	    try {
	      handler = new FileHandler(pattern, 100000, 1);
	      logger.addHandler(handler);
	    } catch (IOException ioe) {
	      ioe.printStackTrace();
	    }
	  }

	  /**
	   * 
	   * @param message
	   * @param type mesaage: level e.g INFO, SEVERE
	   * @param className: calling class
	   * @param methodName: calling method
	   */
	  public void logMessage(String message, Level type, String className, String methodName) {
	    LogRecord record = new LogRecord(type, message);
	    record.setSourceClassName(className);
	    record.setSourceMethodName(methodName);
	    logger.log(record);
	   
	  }
	  
	  public void closeLogger(){
		  handler.flush();
		  handler.close();
	  }

}
