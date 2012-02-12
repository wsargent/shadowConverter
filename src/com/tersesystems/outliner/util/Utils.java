package com.tersesystems.outliner.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


/**
 * Utilities for parsing XML.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Feb 6, 2004
 */
public class Utils
{
    
    /**
     * The palm epoch offset compared to the Java epoch.
     */
    public static final long PALM_EPOCH_OFFSET = 2082844800;
    
    /**
     * An RFC 822 date format, defined as "EE, d MMM yyyy HH:mm:ss z".
     */
    public static final SimpleDateFormat RFC822_FORMAT = new SimpleDateFormat("EE, d MMM yyyy HH:mm:ss z");
    
    /**
     * Makes sure this class can never be instantiated.
     */
    private Utils()
    {
        super();
    }

    /**
     * Creates and returns a new child element that has been appended to the
     * parent element.
     *
     * @param pParent The parent element to create and attach the new element
     *        to.
     * @param pElementName the name of the element.
     *
     * @return the new element.
     *
     * @throws IllegalArgumentException if a required parameter is null.
     */
    public static final Element createElement(Element pParent,
                                              String pElementName)
    {
        if (pParent == null)
        {
            throw new IllegalArgumentException("null pParent");
        }

        if (pElementName == null)
        {
            throw new IllegalArgumentException("null pElementName");
        }

        Document d = pParent.getOwnerDocument();
        Element element = d.createElement(pElementName);
        pParent.appendChild(element);

        return element;
    }

    /**
     * Sets the contents of pElement to a text node with the value of
     * pTextValue.
     *
     * @param pElement
     * @param pTextValue
     *
     * @throws IllegalArgumentException if a required parameter is null.
     */
    public static final void setTextValue(Element pElement, String pTextValue)
    {
        if (pElement == null)
        {
            String msg = "pElement is null";
            throw new IllegalArgumentException(msg);
        }

        if (pTextValue == null)
        {
            String msg = "pTextValue is null";
            throw new IllegalArgumentException(msg);
        }

        Document d = pElement.getOwnerDocument();
        Text textNode = d.createTextNode(pTextValue);

        pElement.appendChild(textNode);
    }

    /**
     * Encodes the String so that it doesn't contain any illegal characters
     * (like any characters which are 'special' like french accents).   This
     * method is copied from Bookie.
     *
     * @param pString the string to be encoded.
     *
     * @return the encoded string.
     */
    public static final String encode(String pString)
    {
        if (pString == null)
        {
            return null;
        }

        StringBuffer buf = new StringBuffer();
        char[] chars = pString.toCharArray();

        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];

            // Escape ampersands           
            //            if (c == '&')
            //            {
            //                buf.append("&amp;");
            //            } else
            if (c > 0x80)
            {
                // If the character is outside of US-ASCII range,
                // then we have to hack around XML and encode
                // the character as an XML literal, in the form
                // &#xx;     
                buf.append("&#");
                buf.append((int) c);
                buf.append(";");
            } else if (c < 0x1F) // XML doesn't allow these characters (#00-#1F) 
            {
                // (except #x9, #xA and #xD) 
                if ((c == 0x9) || (c == 0xA) || (c == 0xD))
                {
                    buf.append(c);
                } else
                {
                    // just write out a space...
                    buf.append(" ");
                }
            } else
            {
                buf.append(c);
            }
        }

        return buf.toString();
    }

    /**
     * Gets the first child element with the name pChildName.  Note that this
     * operation is case-sensitive.
     *
     * @param pElement
     * @param pChildName
     *
     * @return the first child element with the name pChildName.
     */
    public static final Element getChild(Element pElement, String pChildName)
    {
        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                if (nodeName.equals(pChildName))
                {
                    return element;
                }
            }
        }

        return null;
    }

    /**
     * Gets all child elements with the name pChildName.  Note that this
     * operation is case-sensitive.
     *
     * @param pElement
     * @param pChildName
     *
     * @return all child elements with the name pChildName.
     */
    public static final ArrayList getChildren(Element pElement,
                                              String pChildName)
    {
        ArrayList elements = new ArrayList();
        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                if (nodeName.equals(pChildName))
                {
                    elements.add(element);
                }
            }
        }

        return elements;
    }

    /**
     * Gets the child property text.
     *
     * @param pElement
     * @param pElementName
     *
     * @return  the child property text.
     */
    public static final String getChildText(Element pElement,
                                            String pElementName)
    {
        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                if (nodeName.equals(pElementName))
                {
                    String text = getTextValue(element);

                    return text;
                }
            }
        }

        return null;
    }

    /**
     * Gets the text value of the element.
     *
     * @param pText
     *
     * @return the text value of the element.
     */
    public static final String getTextValue(Element pText)
    {
        NodeList children = pText.getChildNodes();
        StringBuffer b = new StringBuffer();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.TEXT_NODE)
            {
                b.append(node.getNodeValue());
            }

            if (node.getNodeType() == Node.CDATA_SECTION_NODE)
            {
                b.append(node.getNodeValue());
            }
        }

        return b.toString().trim();
    }

    /**
     * Removes all children from the current element.
     *
     * @param pElement the element that has children we don't want.
     */
    public static final void removeAllChildren(Element pElement)
    {
        if (pElement == null)
        {
            return;
        }

        NodeList list = pElement.getChildNodes();

        for (int i = 0; i < list.getLength(); i++)
        {
            Node child = list.item(i);
            pElement.removeChild(child);
        }
    }

    /**
     * Converts a palm date to a Java date.
     * <p>
     * If the string is null or 0, then null is returned.
     *
     * @param pPalmTimeSinceEpoch
     *
     * @return a Java date
     */
    public static final Date convertPalmDate(String pPalmTimeSinceEpoch)
    {
        if (Utils.isEmpty(pPalmTimeSinceEpoch)) 
        {
            return null;    
        }
                 
        // There is no time assigned if the value is zero.
        if ("0".equals(pPalmTimeSinceEpoch))
        {
            return null;
        }
        
        long l = Long.parseLong(pPalmTimeSinceEpoch);
        long tse = (l - PALM_EPOCH_OFFSET) * 1000L;
        
        // But wait, we're not done yet!  The Java date has to be merged 
        // into the appropriate timezone and daylight savings time! 
        TimeZone tz = TimeZone.getDefault();
        long rawOffset = tz.getRawOffset();
        Date totalTime = new Date(tse - rawOffset);
        
        // I know this is a mess...
        if (tz.useDaylightTime() && tz.inDaylightTime(totalTime))
        {
            long daylightOffset = tz.getDSTSavings();
            totalTime = new Date(tse - (rawOffset + daylightOffset));   
        }
        return totalTime;
    }

    /**
     * Converts a Java date into a Palm date.
     *
     * @param pDate the java date.
     *
     * @return the date in palm epoch format.
     */
    public static final String convertJavaDate(Date pDate)
    {
        if (pDate == null)
        {
            return "0";
        }

        long timeSinceEpoch = pDate.getTime();
        long msecs = (timeSinceEpoch / 1000L) + PALM_EPOCH_OFFSET;

        return String.valueOf(msecs);
    }

    /**
     * Trims the whitespace (if any) and beginning and ending quotes (if any)
     * from the string.
     *
     * @param creationDate
     *
     * @return the modified string.
     */
    public static final String trimQuotes(String creationDate)
    {        
        String str = creationDate.trim();

        // Don't bother if empty.
        if (Utils.isEmpty(str))
        {
            return str;
        }

        int size = str.length() - 1;
        if (str.charAt(0) == '\"' && str.charAt(size) == '\"')
        {
            str = str.substring(1, size);
        }

        return str;
    }

    /**
     * Parses out an RFC 822 date to return a java date object.
     *
     * @param pDateTime
     *
     * @return a formatted date.
     *
     * @throws ParseException if the date cannot be parsed.
     */
    public static final Date parseDateTime(String pDateTime)
        throws ParseException
    {
        Date date = RFC822_FORMAT.parse(pDateTime);

        return date;
    }
    
    /**
     * Formats the date/time as EE, d MMM yyyy HH:mm:ss z.
     *
     * @param pDate the date to process.
     *
     * @return the RFC 822 formatted date.
     *
     * @throws ParseException if the date cannot be parsed.
     */
    public static final String formatDateTime(Date pDate)
        throws ParseException
    {
        String dateString = RFC822_FORMAT.format(pDate);

        return dateString;
    }    

    /**
     * Returns true if the string is null or empty, false otherwise.
     *
     * @param pString
     *
     * @return true if the string is null or empty, false otherwise.
     */
    public static final boolean isEmpty(String pString)
    {
        if (pString == null)
        {
            return true;
        }

        if (pString.length() == 0)
        {
            return true;
        }

        if (pString.trim().length() == 0)
        {
            return true;
        }

        return false;
    }
}
