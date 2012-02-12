package com.tersesystems.outliner.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple bean containing item values.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Feb 6, 2004
 */
public class Item
{
    protected List mChildren = new ArrayList();
    protected Map mProperties = new HashMap();

    public Item()
    {       
    }

    /**
     * Gets the children items in the item.
     *
     * @return  the children items in the item.
     */
    public List getChildren()
    {
        return mChildren;
    }

    public String toString()
    {
        StringBuffer b = new StringBuffer("Item");        
        b.append(" properties: [");
        b.append(mProperties.toString());        
        b.append("]");
        b.append(", children: ").append(mChildren).append("");
        
        return b.toString();
    }

    /**
     * Gets the value of pKey, if it exists in the bean.
     * 
     * @param pKey
     * @return the value of pKey, if it exists in the bean.
     */
    public Object get(String pKey)
    {
        if (! mProperties.containsKey(pKey))
        {
            String msg = "property " + pKey + " not found";
            throw new PropertyNotFoundException(msg);
        }

        return mProperties.get(pKey);
    }

    public void set(String pKey, Object pValue)
    {
        mProperties.put(pKey, pValue);
    }

    /**
     * Sets an integer with the name pKey with the value pValue.
     *
     * @param pKey
     * @param pValue
     */
    public void setInteger(String pKey, int pValue)
    {
        mProperties.put(pKey, new Integer(pValue));
    }

    /**
     * Gets the value of pKey as an Integer, if possible.
     * 
     * @param pKey
     * @return the value of pKey as an Integer, if possible.
     */
    public Integer getInteger(String pKey)
    {
        if (! mProperties.containsKey(pKey))
        {
            String msg = "property " + pKey + " not found";
            throw new PropertyNotFoundException(msg);
        }

        return (Integer) mProperties.get(pKey);
    }

    /**
     * Sets a boolean as having the name pKey.
     *
     * @param pKey
     * @param pValue
     */
    public void setBoolean(String pKey, boolean pValue)
    {
        mProperties.put(pKey, Boolean.valueOf(pValue));
    }

    /**
     * Gets the value of pKey as a Boolean, if possible.
     * 
     * @param pKey
     * @return the value of pKey as a Boolean, if possible.
     */
    public Boolean getBoolean(String pKey)
    {
        if (! mProperties.containsKey(pKey))
        {
            String msg = "property " + pKey + " not found";
            throw new PropertyNotFoundException(msg);
        }

        return (Boolean) mProperties.get(pKey);
    }

    public void setDate(String pKey, Date pDate)
    {
        mProperties.put(pKey, pDate);
    }

    /**
     * Gets the value of pKey as a date, if possible.
     * 
     * @param pKey
     * @return the value of pKey as a date, if possible.
     */
    public Date getDate(String pKey)
    {
        if (! mProperties.containsKey(pKey))
        {
            String msg = "property " + pKey + " not found";
            throw new PropertyNotFoundException(msg);
        }

        return (Date) mProperties.get(pKey);
    }

    /**
     * Gets the value of pKey as a string, if possible.
     * 
     * @param pKey
     * @return the value of pKey as a string, if possible.
     */
    public String getString(String pKey)
    {
        if (! mProperties.containsKey(pKey))
        {
            String msg = "property " + pKey + " not found";
            throw new PropertyNotFoundException(msg);
        }

        Object value = mProperties.get(pKey);
        // If it's null, then don't do anything.
        if (value == null) {
            return null;
        }
        
        // Otherwise, do the best conversion to string that we can.
        return String.valueOf(value);
    }

    /**
     * Sets a string as having the name pKey.
     *
     * @param pKey
     * @param pString
     */
    public void setString(String pKey, String pString)
    {
        mProperties.put(pKey, pString);
    }

    /**
     * Returns true if the property exists in the bean, otherwise false.
     *
     * @param pString
     *
     * @return true if the property exists in the bean, otherwise false.
     */
    public boolean hasProperty(String pString)
    {
        return mProperties.containsKey(pString);
    }

    /**
     * Gets the property names in the item.
     *
     * @return  the property names in the item.
     */
    public String[] getPropertyNames()
    {
        String[] propertyNames = (String[]) mProperties.keySet().toArray(new String[0]);

        return propertyNames;
    }
}
