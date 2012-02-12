package com.tersesystems.outliner.xml.shadow;

import java.io.File;
import java.util.prefs.Preferences;

import com.tersesystems.outliner.util.Utils;


/**
 * The preferences for the shadow converter.  This class makes heavy use of the
 * java.util.preferences package.  This class enables persistent storage of
 * preferences outside of the application itself.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Mar 23, 2004
 */
public class ShadowPreferences
{
    private static final String SHADOW_VERSION = "shadowVersion";
    private static final String PALM_DIRECTORY = "palmDirectory";
    private static final String PALM_ACCOUNT_NAME = "palmAccount";
    private static final String CONVERTER_PREFS_PATH = "com.tersesystems.shadowConverter";
    private static Preferences sPreferences = Preferences.userRoot().node(CONVERTER_PREFS_PATH);

    /**
     *
     */
    public ShadowPreferences()
    {
        super();
    }

    /**
     * Gets the full directory of the shadow XML files, using
     * the default palm account name.
     *
     * @return the shadow directory as a string.
     */
    public static String getShadowDirectory()
    {
        return getShadowDirectory(getPalmAccountName());
    }
    
    /**
     * Gets the directory of the shadow XML files, given the user name.
     * @param pAccountName
     * 
     * @return the shadow directory as a string.
     */
    public static String getShadowDirectory(String pAccountName)
    {
        if (Utils.isEmpty(pAccountName)) 
        {
            throw new IllegalArgumentException("No valid palm account name found.");
        }
        
        StringBuffer b = new StringBuffer();
        b.append(getPalmDirectory());
        b.append(File.separator);
        b.append(pAccountName);
        b.append(File.separator);
        b.append(getShadowVersion());
        
        return b.toString();
    }
    
    /**
     * Gets the version of the shadow files, returning a sensible default.
     * 
     * @return the version of the shadow files
     */
    public static String getShadowVersion() 
    {
        return sPreferences.get(SHADOW_VERSION, "ShadowPlan400");    
    }

    /**
     * Gets the palm directory, returning a sensible default.
     *
     * @return the palm directory
     */
    public static String getPalmDirectory()
    {
        return sPreferences.get(PALM_DIRECTORY, "C:/Program Files/Palm");
    }

    /**
     * Sets the palm directory to the value of pName.
     *
     * @param pName
     */
    public static void setPalmDirectory(String pName)
    {
        sPreferences.put(PALM_DIRECTORY, pName);
    }

    /**
     * Gets the palm account name, returning null as a default.
     *
     * @return the palm account name
     */
    public static String getPalmAccountName()
    {
        return sPreferences.get(PALM_ACCOUNT_NAME, null);
    }

    /**
     * Sets the palm account name.
     *
     * @param pName
     */
    public static void setPalmAccountName(String pName)
    {
        sPreferences.put(PALM_ACCOUNT_NAME, pName);
    }

    /**
     * Gets the file containing the shadow tags.
     * 
     * @param pAccount
     * @return the file containing the shadow tags.
     */
    public static File getShadowTags(String pAccount)
    {
        String shadowDir = getShadowDirectory(pAccount);
        return new File(shadowDir, "ShadowTags.XML");
    }
}
