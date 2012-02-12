package com.tersesystems.outliner.xml.shadow;

/**
 * Constants for the Shadow package.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Apr 4, 2004
 */
public class ShadowConstants
{
    public static final String ONE = "1";
    
    public static final String ALARM_ID = "alarmID";
    public static final String AUTO_NUMBER = "autoNumber";
    public static final String CHECKED = "checked";
    public static final String DELETED = "deleted";
    public static final String DIRTY_CONTENT = "dirtyContent";
    public static final String DIRTY_POSITION = "dirtyPosition";
    public static final String DISP_BOLD = "dispBold";
    public static final String DISP_COLOUR = "dispColour";
    public static final String DISP_OVERRIDE = "dispOverride";
    public static final String EXPANDED = "expanded";
    public static final String EXPANDED_LINKS = "expandedLinks";
    public static final String EXPANDED_MEMO = "expandedMemo";
    public static final String HH_CREATE_TIME = "hhCreateTime";
    public static final String HH_FINISH_TIME = "hhFinishTime";
    public static final String HH_START_TIME = "hhStartTime";
    public static final String HH_TARGET_TIME = "hhTargetTime";
    public static final String ITEM = "item";
    public static final String LOCAL_ID = "localID";
    public static final String NO = "no";
    public static final String PRIORITY = "priority";
    public static final String PROGRESS = "progress";
    public static final String SHADOW_PLAN_FILE = "ShadowPlanFile";
    public static final String UNIQUE_ID = "uniqueID";
    public static final String UNIQUE_TIME = "uniqueTime";
    public static final String UPLOAD_FILE = "uploadFile";
    public static final String YES = "yes";
    public static final String TITLE = "title";
    public static final String NOTE = "note";
    public static final int AUTO_NUMBER_NONE = 1;
    public static final int AUTO_NUMBER_ROMAN = 6;
    public static final int DISPLAY_CHECKLIST = 0;
    public static final int DISPLAY_NOTE = 1;
    
    /**
     * The value to set on DISP_OVERRIDE attribute when we want a checklist item.
     */
    public static final String CHECKLIST_OVERRIDE = "0:0";
    
    /** The value to set on DISP_OVERRIDE when we want a note. */
    public static final String NOTE_OVERRIDE = "1:0";

    // Note: display constants below this point are a guess...
    public static final int DISPLAY_TASKLIST = 2;
    public static final int DISPLAY_CUSTOM = 3;
    public static final int DISPLAY_FLAT = 4;
    public static final String EAB_TAG = "2";
    public static final String EAB_TYPE = "eabType";
    public static final String EAB_ID = "eabID";
    public static final String LINK_EAB = "linkEAB";

    private ShadowConstants()
    {
    }
}
