package com.tersesystems.outliner.core;

/**
 * A bunch of constants which represent folders in Ecco.  Folders in
 * Ecco are also the properties that an item may have.  Folders may 
 * have different values... they can be strings, booleans, or dates.
 * 
 * @author wsargent
 * @version $Revision$ 
 * @since Apr 19, 2004
 */
public class EccoConstants
{
    /**
     * The Shadow Record ID folder.  We don't use the existing pilot record
     * ID folder because we might clobber something if it's in the Datebook
     * database with a different ID.
     */
    public static final String SHADOW_RECORD_ID = "Shadow Record ID";
    
    /**
     * The date of the expense.  A date property.
     */
    public static final String EXPENSE_DATE = "Expense Date";
    
    /**
     * Anniversary.  A date property.
     */
    public static final String ANNIVERSARY = "Anniversary";
    
    /**
     * The birthday time.  A date property.
     */
    public static final String BIRTHDAY = "Birthday";
    
    /**
     * The time of a call back.  A date property.
     */
    public static final String CALL_BACK = "Call Back";
    
    /**
     * The time a message was left.  A date property.
     */
    public static final String LEFT_MSG = "Left Msg";
    
    /**
     * The time last called.  A date property.
     */
    public static final String LAST_CALL = "Last Call";
    
    /**
     * The phone time long.  A date property.
     */
    public static final String PHONE_TIME_LOG = "Phone / Time Log";
    
    /**
     * The todo's folder.  Ecco will sync this to Palm.
     */
    public static final String TODOS = "To-Do's";
    
    /**
     * The someday folder.  A date property.
     */
    public static final String SOMEDAY_MAYBE = "Someday / Maybe";
    
    /**
     * The "Not" folder.  A date property.
     */
    public static final String NOT = "Not";
    
    /** 
     * The Ecco appointments folder.  Items placed in here will show up in the 
     * Palm calendar.
     */
    public static final String APPOINTMENTS = "Appointments";
    
    /** The ecco property of the item text.  This is what the user sees. */
    public static final String TEXT = "text";

    /** The Ecco folderId.  This is used when we want to display folders as part of OML. */
    public static final String FOLDER_ID = "folderId";

    /** The itemId.  This is used for Ecco's items. */
    public static final String ITEM_ID = "itemId";

    /** The Due Dates folder value of the item in Ecco. */
    public static final String DUE_DATES = "Due Dates";

    /** The Start Dates folder value of the item in Ecco. */
    public static final String START_DATES = "Start Dates";

    /** The Done folder value of the item in Ecco. */
    public static final String DONE = "Done";

    /** The Datestamp folder value of the item in Ecco. */
    public static final String DATE_STAMP = "Date Stamp";

    /**
     * The smart way to do this would be to encode folder information in the
     * OML so the folderType would be explicit.  But this oughta do for now.
     */
    public static final String[] DATE_PROPERTIES = 
                                      {
                                          DUE_DATES, START_DATES, DONE,
                                          DATE_STAMP, APPOINTMENTS,
                                          TODOS, PHONE_TIME_LOG,
                                          LAST_CALL, LEFT_MSG, CALL_BACK,
                                          NOT, SOMEDAY_MAYBE, BIRTHDAY,
                                          ANNIVERSARY, EXPENSE_DATE
                                      };

    /**
     * 
     */
    private EccoConstants()
    {
        super();
    }

}
