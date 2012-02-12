package com.tersesystems.outliner.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * A utility to get user messages out.
 * <p>
 * TODO create a map of UserMessages so we can return existing objects and cache locales.
 * </p>
 *
 * @version $Revision$
 * @author wsargent
 * @since Feb 27, 2005
 */
public class UserMessages {

    protected String mBundleName;
    protected ResourceBundle mResourceBundle;
    protected Locale mLocale;
    
    /**
     * Gets the user messages, initialized with the name "UserMessages" from the 
     * package given by pClass.  That is, foo.bar.MyClass will get the resource bundle
     * foo.bar.UserMessages.
     * 
     * @param pClass
     * @return the configured UserMessages object.
     */
    public static UserMessages getUserMessages(Class pClass) {
        Package classPackage = pClass.getPackage();
        String name = classPackage.getName() + ".UserMessages";
        
        return getUserMessages(name);
    }
    
    /**
     * The factory method for the user messages.     
     * @param pBundleName the bundle name to use.
     * 
     * @return the usermessages object.
     */
    public static UserMessages getUserMessages(String pBundleName) {
        return new UserMessages(pBundleName);
    }
    
    /**
     * Gets the user messages object with the given locale.
     * 
     * @param pLocale
     * @param pBundleName
     * @return the user messages object.
     */
    public static UserMessages getUserMessages(Locale pLocale, String pBundleName) {
        return new UserMessages(pLocale, pBundleName);
    }
    
    protected UserMessages(String pBundleName) {
        mBundleName = pBundleName;
        mResourceBundle = ResourceBundle.getBundle(mBundleName);
    }
    
    protected UserMessages(Locale pLocale, String pBundleName) {
        mBundleName = pBundleName;
        mLocale = pLocale;   
        mResourceBundle = ResourceBundle.getBundle(mBundleName, pLocale);
    }
    
    /**
     * @return Returns the bundleName.
     */
    public String getBundleName() {
        return mBundleName;
    }
    
    /**
     * @return Returns the locale.
     */
    public Locale getLocale() {
        return mLocale;
    }
    
    /**
     * @return Returns the resourceBundle.
     */
    public ResourceBundle getResourceBundle() {
        return mResourceBundle;
    }
    
    /**
     * Gets a user message from the code with no arguments.
     * 
     * @param pCode
     * @return the message for pCode.
     */
    public String getUserMessage(String pCode) {
        if (Utils.isEmpty(pCode)) {
            throw new IllegalArgumentException("null pCode");
        }
        String message = mResourceBundle.getString(pCode);
        
        return message;        
    }
    
    /**
     * Gets a user message with a parameter.
     * 
     * @param pCode
     * @param pParameter
     * @return the formatted user message.
     */
    public  final String getUserMessage(String pCode, Object pParameter) {        
        String message = getUserMessage(pCode);
        Object[] params = { pParameter };
        String formattedMessage = MessageFormat.format(message, params);        
        return formattedMessage;        
    }
    
    /**
     * Gets a user message with an array of parameters.
     * 
     * @param pCode
     * @param pParameters
     * @return the formatted user message.
     */
    public  final String getUserMessage(String pCode, Object[] pParameters) {
        String message = getUserMessage(pCode);        
        String formattedMessage = MessageFormat.format(message, pParameters);        
        return formattedMessage;
    }    
    
    /**
     * Gets a user message with a list of parameters.
     * 
     * @param pCode
     * @param pParameters
     * @return the formatted user message.
     */
    public  final String getUserMessage(String pCode, List pParameters) {
        String message = getUserMessage(pCode);      
        Object[] array = (pParameters != null) ? pParameters.toArray() : null;
        String formattedMessage = MessageFormat.format(message, array);        
        return formattedMessage;
    }
    
}
