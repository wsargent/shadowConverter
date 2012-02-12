package com.tersesystems.outliner.xml.shadow;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tersesystems.outliner.core.EccoConstants;
import com.tersesystems.outliner.core.ExportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.util.Utils;
import com.tersesystems.outliner.xml.AbstractXMLExporter;


/**
 * Takes a bunch of items in OML using Ecco properties and exports them to
 * Shadow Plan format.
 * <p>
 * Shadow Plan synchronization can be tricky to deal with.  In general, if 
 * there is an existing file on the handheld and you've exported a shadow file,
 * you want to go to the hotsync manager, and use the shadow conduit to set the
 * sychronization to resolve to "desktop over handheld" in the event of a 
 * collision.  This should result in the exported file being, well, exported even
 * if the handheld file has changed a bit since then.
 * </p>
 * 
 * @author wsargent
 * @version $Version$
 *
 * @since Mar 10, 2004
 */
public class ExportToShadow extends AbstractXMLExporter implements TagSupport
{
    private static final String ONE = "1";
    private static final String ZERO = "0";
    
    protected TagManager mTagManager;

    /**
     *
     */
    public ExportToShadow()
    {
        super();
    }

    /**
     * Gets the tag manager.
     *
     * @return Returns the tagManager.
     */
    public TagManager getTagManager()
    {
        return mTagManager;
    }

    /**
     * Sets the tag manager.
     *
     * @param pTagManager The tagManager to set.
     */
    public void setTagManager(TagManager pTagManager)
    {
        mTagManager = pTagManager;
    }

    /**
     * Sets the root element attributes to clobber the synchronized file.
     *
     * @param rootElement
     *
     */
    public void setRootAttributes(Element rootElement)       
    {
        // Set a couple of properties to clobber shadow plan...
        String uniqueTime = Utils.convertJavaDate(new Date());
        rootElement.setAttribute(ShadowConstants.UNIQUE_TIME, uniqueTime);
        rootElement.setAttribute(ShadowConstants.UPLOAD_FILE, ONE);
    }

    /**
     * Exports the links and the tags inside the item.
     *
     * @param pItemEl
     * @param pItem
     */
    protected void exportLinks(Element pItemEl, Item pItem)
    {
        /* linkEAB eabType="2" eabID="3162891368"/> */

        // There's a set of properties which are tags.  See if the 
        // item has any of these tags, and then map to an ID for
        // them.
        Tag[] possibleTags = mTagManager.getTags();

        for (int i = 0; i < possibleTags.length; i++)
        {
            Tag tag = possibleTags[i];

            if (pItem.hasProperty(tag.getName()))
            {
                String eabID = tag.getID();
                Element linkEl = Utils.createElement(pItemEl,
                                                     ShadowConstants.LINK_EAB);
                linkEl.setAttribute(ShadowConstants.EAB_ID, eabID);
                linkEl.setAttribute(ShadowConstants.EAB_TYPE,
                                    ShadowConstants.EAB_TAG);
            }
        }
    }

    /**
     * Sets the child title element on the XML item.
     *
     * @param pItem
     * @param itemEl
     *
     * @throws ExportException if null
     */
    protected void exportTitle(Item pItem, Element itemEl)
        throws ExportException
    {
        if (itemEl == null)
        {
            String msg = "itemEl is null";
            throw new ExportException(msg);
        }

        if (pItem == null)
        {
            String msg = "pItem is null";
            throw new ExportException(msg);
        }

        Element titleEl = Utils.createElement(itemEl, ShadowConstants.TITLE);
        Utils.setTextValue(titleEl, pItem.getString(EccoConstants.TEXT));
    }

    /**
     * Sets all the attributes on the XML item.
     *
     * @param pItem
     * @param itemEl
     *
     * @throws DOMException
     * @throws ExportException
     */
    protected void exportAttributes(Item pItem, Element itemEl)
        throws DOMException, ExportException
    {
        if (itemEl == null)
        {
            String msg = "itemEl is null";
            throw new ExportException(msg);
        }

        if (pItem == null)
        {
            String msg = "pItem is null";
            throw new ExportException(msg);
        }

        // The exported data is always dirty, by definition.  We run into
        // problems with the handheld not picking up data if we don't define this:
        // see http://groups.yahoo.com/group/shadow-discuss/message/19339
        if (pItem.hasProperty(EccoConstants.SHADOW_RECORD_ID))
        {
            String uniqueId = pItem.getString(EccoConstants.SHADOW_RECORD_ID);
            itemEl.setAttribute(ShadowConstants.UNIQUE_ID, uniqueId);
            itemEl.setAttribute(ShadowConstants.DIRTY_CONTENT, ShadowConstants.ONE);
            itemEl.setAttribute(ShadowConstants.DIRTY_POSITION, ShadowConstants.ONE);            
        } else
        {
            itemEl.setAttribute(ShadowConstants.UNIQUE_ID, ZERO);
        }
        
        /*
           itemEl.setAttribute(Constants.DIRTY_CONTENT,
                               String.valueOf(pItem.getString(Constants.DIRTY_CONTENT)));
           itemEl.setAttribute(Constants.DIRTY_POSITION,
                               String.valueOf(pItem.getString(Constants.DIRTY_POSITION)));
           itemEl.setAttribute(Constants.DELETED,
                               String.valueOf(pItem.getBoolean(Constants.DELETED)));
         */
        /*
           itemEl.setAttribute(Constants.EXPANDED,
                               yesNo(pItem.getBoolean(Constants.EXPANDED)));
           itemEl.setAttribute(Constants.EXPANDED_MEMO,
                               yesNo(pItem.getBoolean(Constants.EXPANDED_MEMO)));
           itemEl.setAttribute(Constants.EXPANDED_LINKS,
                               yesNo(pItem.getBoolean(Constants.EXPANDED_LINKS)));
           itemEl.setAttribute(Constants.PROGRESS,
                               String.valueOf(pItem.getInteger(Constants.PROGRESS)));
           itemEl.setAttribute(Constants.AUTO_NUMBER,
                               String.valueOf(pItem.getInteger(Constants.AUTO_NUMBER)));
           itemEl.setAttribute(Constants.ALARM_ID,
                               String.valueOf(pItem.getInteger(Constants.ALARM_ID)));
         */
        String localID = null;

        if (pItem.hasProperty(EccoConstants.ITEM_ID))
        {
            localID = pItem.getString(EccoConstants.ITEM_ID);
            itemEl.setAttribute(ShadowConstants.DISP_OVERRIDE, ShadowConstants.CHECKLIST_OVERRIDE);
        } else if (pItem.hasProperty(EccoConstants.FOLDER_ID))
        {
            localID = pItem.getString(EccoConstants.FOLDER_ID);

            // Folders are always expanded.
            itemEl.setAttribute(ShadowConstants.EXPANDED, ShadowConstants.YES);
            
            // Folders are always notes.
            itemEl.setAttribute(ShadowConstants.DISP_OVERRIDE, ShadowConstants.NOTE_OVERRIDE);
        }

        if (! Utils.isEmpty(localID))
        {
            itemEl.setAttribute(ShadowConstants.LOCAL_ID, localID);
        }

        /*
           itemEl.setAttribute(Constants.DISP_COLOUR,
                               String.valueOf(pItem.getInteger(Constants.DISP_COLOUR)));
        
           Boolean dispBold = pItem.getBoolean(Constants.DISP_BOLD);
           itemEl.setAttribute(Constants.DISP_BOLD, yesNo(dispBold));
        
           itemEl.setAttribute(Constants.DISP_OVERRIDE,
                               pItem.getString(Constants.DISP_OVERRIDE));
         */
    }

    /**
     * Exports the palm date element.
     *
     * @param itemEl
     * @param pElementName
     * @param pTextValue
     *
     * @throws ExportException if null
     */
    protected void exportPalmDate(Element itemEl, String pElementName,
                                String pTextValue) throws ExportException
    {
        if (itemEl == null)
        {
            String msg = "itemEl is null";
            throw new ExportException(msg);
        }

        if (pElementName == null)
        {
            String msg = "pElementName is null";
            throw new ExportException(msg);
        }

        Element timeEl = Utils.createElement(itemEl, pElementName);
        String palmTime = pTextValue;
        Utils.setTextValue(timeEl, palmTime);
    }

    /**
     * Returns yes if true, no if false.
     *
     * @param pBool
     *
     * @return  yes if true, no if false.
     */
    protected String yesNo(Boolean pBool)
    {
        if (pBool == null)
        {
            return ShadowConstants.NO;
        }

        return (pBool.booleanValue()) ? ShadowConstants.YES : ShadowConstants.NO;
    }

    /**
     * Exports the child, then all the children of that child.
     *
     * @param pParent
     * @param pItem
     *
     * @throws ExportException if the document cannot be generated.
     */
    protected void exportChild(Element pParent, Item pItem)
        throws ExportException
    {
        if (pParent == null)
        {
            throw new ExportException("null pParent");
        }

        if (pItem == null)
        {
            throw new ExportException("null pItem");
        }

        Element itemEl = Utils.createElement(pParent, ShadowConstants.ITEM);
        exportAttributes(pItem, itemEl);
        exportTitle(pItem, itemEl);

        //exportNote(pItem, itemEl);
        // Get the constants from OML
        Date createDate;

        if (pItem.hasProperty(EccoConstants.DATE_STAMP))
        {
            createDate = pItem.getDate(EccoConstants.DATE_STAMP);
        } else
        {
            createDate = new Date();
        }

        // Do some converting of Ecco dates to Shadow dates.
        String createTime = Utils.convertJavaDate(createDate);
        exportPalmDate(itemEl, ShadowConstants.HH_CREATE_TIME, createTime);

        if (pItem.hasProperty(EccoConstants.START_DATES))
        {
            String startTime = Utils.convertJavaDate(pItem.getDate(EccoConstants.START_DATES));
            exportPalmDate(itemEl, ShadowConstants.HH_START_TIME, startTime);
        }

        if (pItem.hasProperty(EccoConstants.DONE))
        {
            String finishTime = Utils.convertJavaDate(pItem.getDate(EccoConstants.DONE));
            exportPalmDate(itemEl, ShadowConstants.HH_FINISH_TIME, finishTime);
            itemEl.setAttribute(ShadowConstants.CHECKED, ShadowConstants.YES);
        }

        if (pItem.hasProperty(EccoConstants.DUE_DATES))
        {
            String targetTime = Utils.convertJavaDate(pItem.getDate(EccoConstants.DUE_DATES));
            exportPalmDate(itemEl, ShadowConstants.HH_TARGET_TIME, targetTime);
        }

        exportLinks(itemEl, pItem);

        // Export the children.
        List children = pItem.getChildren();

        for (Iterator iter = children.iterator(); iter.hasNext();)
        {
            Item childItem = (Item) iter.next();
            exportChild(itemEl, childItem);
        }
    }

    /**
     * Exports the folder information to a note on the item.
     *
     * @param pItem
     * @param itemEl
     */
    protected void exportNote(Item pItem, Element itemEl)
    {
        Element noteElement = Utils.createElement(itemEl, ShadowConstants.NOTE);

        StringBuffer b = new StringBuffer();
        String[] propertyNames = pItem.getPropertyNames();

        for (int i = 0; i < propertyNames.length; i++)
        {
            String propertyName = propertyNames[i];
            Object value = pItem.get(propertyName);
            b.append(propertyName);
            b.append(": ");
            b.append(value);
            b.append("\n");
        }

        String noteValue = b.toString();
        Utils.setTextValue(noteElement, noteValue);
    }

    /* (non-Javadoc)
     * @see com.tersesystems.outliner.xml.AbstractXMLExporter#exportRoot(com.tersesystems.outliner.core.Item, org.w3c.dom.Document)
     */
    public Element exportRoot(Item pRoot, Document d) throws ExportException
    {
        Element rootElement = d.createElement(ShadowConstants.SHADOW_PLAN_FILE);

        setRootAttributes(rootElement);

        d.appendChild(rootElement);

        List children = pRoot.getChildren();

        for (Iterator iter = children.iterator(); iter.hasNext();)
        {
            Item child = (Item) iter.next();
            exportChild(rootElement, child);
        }

        return rootElement;
    }
}
