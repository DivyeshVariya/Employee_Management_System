package com.info.ems.constants;

public final class Constants {

	  public static final String EMPLOYEE_ID = "employeeId";
	  public static final String DATA = "data";
	  public static final String ID = "id";
	  public static final String DELETED = "deleted";

	  // Exceptions
	  public static final String CLASS = "class";
	  public static final String METHOD = "method";

	  public static final String ERROR = "ERROR";
	  
	  /* ------------------------------------- SystemNotification --------------------------------------- */

	    public static final String SYSTEM_AUIDT_LOG_TOPIC = "SYSTEM_AUIDT_LOG";

	    // hour * minutes * seconds * milliseconds
	    public static final String SYSTEM_AUIDT_LOG_RETENTION = String.valueOf(60000);

	    // hour * minutes * seconds * milliseconds
	    public static final String SYSTEM_AUIDT_LOG_SEGMENT_RETENTION = String.valueOf(60000);

	  private Constants() {}
	}