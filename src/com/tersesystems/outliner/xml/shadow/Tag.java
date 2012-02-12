package com.tersesystems.outliner.xml.shadow;

/**
 * A tag bean.
 * 
 * @author wsargent
 * @version $Revision$ 
 * @since Mar 26, 2004
 */
public class Tag
{
    private String mId;
    private String mName;

    /**
     * @param pId
     * @param pName
     * 
     */
    public Tag(String pId, String pName)
    {
        super();
        
        if (pName == null)
        {
            String msg = "pId is null";
            throw new IllegalArgumentException(msg);
        }
        
        if (pId == null)
        {
            String msg = "pId is null";
            throw new IllegalArgumentException(msg);
        }
        
        mId = pId;
        mName = pName;
    }
    
    public String getID()
    {
        return mId;
    }
    
    public String getName()
    {
        return mName;
    }
    
    public String toString()
    {
        return "Tag: id=" + mId + ", name=" + mName;
    }
}
