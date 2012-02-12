package com.tersesystems.outliner.xml.shadow;


/**
 * 
 * 
 * @author wsargent
 * @version $Revision$ 
 * @since Mar 26, 2004
 */
public interface TagSupport
{
    /**
     * Gets the tag manager.
     * 
     * @return the tag manager.
     */
    public TagManager getTagManager();
    
    /**
     * Sets the tag manager.
     * 
     * @param pTagManager
     */
    public void setTagManager(TagManager pTagManager);
}
