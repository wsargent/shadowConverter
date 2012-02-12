package com.tersesystems.outliner.xml.oml;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tersesystems.outliner.core.EccoConstants;
import com.tersesystems.outliner.core.ExportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.util.Utils;
import com.tersesystems.outliner.xml.AbstractXMLExporter;
import com.tersesystems.outliner.xml.shadow.ShadowConstants;
import com.tersesystems.outliner.xml.shadow.Tag;
import com.tersesystems.outliner.xml.shadow.TagManager;
import com.tersesystems.outliner.xml.shadow.TagSupport;


/**
 * Takes a bunch of items containing Shadow properties and creates an OML
 * document containing Ecco properties.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Apr 5, 2004
 */
public class ExportToOML extends AbstractXMLExporter implements TagSupport {
    private static final Logger sLogger = Logger.getLogger(ExportToOML.class);
    protected static final String NOTE = ShadowConstants.NOTE;
    protected static final String TITLE = ShadowConstants.TITLE;
    protected static final String HH_CREATE_TIME = ShadowConstants.HH_CREATE_TIME;
    protected static final String[] SHADOW_PROPERTIES = {
                                                            ShadowConstants.UNIQUE_ID,
                                                            ShadowConstants.DIRTY_CONTENT,
                                                            ShadowConstants.DIRTY_POSITION,
                                                            ShadowConstants.CHECKED,
                                                            ShadowConstants.EXPANDED,
                                                            ShadowConstants.EXPANDED_MEMO,
                                                            ShadowConstants.EXPANDED_LINKS,
                                                            ShadowConstants.PRIORITY,
                                                            ShadowConstants.PROGRESS,
                                                            ShadowConstants.AUTO_NUMBER,
                                                            ShadowConstants.ALARM_ID,
                                                            ShadowConstants.DISP_COLOUR,
                                                            ShadowConstants.DISP_OVERRIDE,
                                                        };
    protected static final String[] SHADOW_DATES = {
                                                       ShadowConstants.HH_START_TIME,
                                                       ShadowConstants.HH_FINISH_TIME,
                                                       ShadowConstants.HH_TARGET_TIME
                                                   };
    protected Map sPropertyMapping = new HashMap();
    protected TagManager mTagManager;

    /**
     *
     */
    public ExportToOML() {
        super();

        // Set up the property mapping.
        sPropertyMapping.put(ShadowConstants.HH_START_TIME,
                             EccoConstants.START_DATES);
        sPropertyMapping.put(ShadowConstants.HH_FINISH_TIME, EccoConstants.DONE);
        sPropertyMapping.put(ShadowConstants.HH_TARGET_TIME,
                             EccoConstants.DUE_DATES);
        sPropertyMapping.put(ShadowConstants.UNIQUE_ID,
                             EccoConstants.SHADOW_RECORD_ID);
    }

    /**
     * Gets the tag manager.
     *
     * @return Returns the tagManager.
     */
    public TagManager getTagManager() {
        return mTagManager;
    }

    /**
     *The tagManager to set.
     *
     * @param pTagManager The tagManager to set.
     */
    public void setTagManager(TagManager pTagManager) {
        mTagManager = pTagManager;
    }

    /* (non-Javadoc)
     * @see com.tersesystems.outliner.xml.AbstractXMLExporter#exportRoot(com.tersesystems.outliner.core.Item, org.w3c.dom.Document)
     */
    public Element exportRoot(Item pRoot, Document d) throws ExportException {
        // Check the preconditions...
        if (getTagManager() == null) {
            throw new ExportException("null tag manager");
        }

        if (d == null) {
            throw new ExportException("null document");
        }

        if (pRoot == null) {
            throw new ExportException("null pRoot");
        }

        sLogger.debug("exportRoot: pRoot = " + pRoot);

        //DocumentType docType = d.getDoctype();
        Element oml = d.createElement(OMLConstants.OML);
        d.appendChild(oml);

        oml.setAttribute(OMLConstants.VERSION, OMLConstants.OML_VERSION_NUMBER);

        // Head contains no elements
        //Element head = Utils.createElement(oml, OMLConstants.HEAD);
        Element body = Utils.createElement(oml, OMLConstants.BODY);

        List children = pRoot.getChildren();

        try {
            for (Iterator iter = children.iterator(); iter.hasNext();) {
                Item child = (Item) iter.next();
                exportChild(body, child);
            }
        } catch (DOMException e) {
            throw new ExportException(e);
        } catch (ParseException e) {
            throw new ExportException(e);
        }

        return oml;
    }

    /**
     * Exports the children of the outline.
     *
     * @param pParent
     * @param pItem
     *
     * @throws ExportException if the item tree cannot be built.
     * @throws DOMException if the document cannot be created
     * @throws ParseException if a date cannot be parsed.
     */
    protected void exportChild(Element pParent, Item pItem)
                        throws ExportException, DOMException, ParseException {
        sLogger.debug("exportChild: pParent = " + pParent + ", item = " + pItem);

        Element outline = Utils.createElement(pParent, OMLConstants.OUTLINE);

        // If there's a note in Shadow, then add it as data to OML.
        if (pItem.hasProperty(NOTE)) {
            String note = pItem.getString(NOTE);

            if (! Utils.isEmpty(note)) {
                Element data = Utils.createElement(outline, OMLConstants.DATA);
                Utils.setTextValue(data, note);
            }
        }

        // Set the outline
        String title = pItem.getString(TITLE);
        outline.setAttribute(OMLConstants.TEXT, title);

        exportDates(pItem, outline);

        exportShadowData(pItem, outline);

        // Resolves the links from the shadow tags file to their real names.
        exportTags(pItem, outline);

        List children = pItem.getChildren();

        for (Iterator iter = children.iterator(); iter.hasNext();) {
            Item item = (Item) iter.next();
            exportChild(outline, item);
        }
    }

    /**
     * Exports the properties of the shadow item as "item" elements.
     *
     * @param pItem
     * @param outline
     */
    protected void exportShadowData(Item pItem, Element outline) {
        sLogger.debug("exportShadowData: pItem = " + pItem + ", outline = "
                      + outline);

        for (int i = 0; i < SHADOW_PROPERTIES.length; i++) {
            String propertyName = SHADOW_PROPERTIES[i];

            if (pItem.hasProperty(propertyName)) {
                String propertyValue = pItem.getString(propertyName);
                exportItem(outline, propertyName, propertyValue);
            }
        }
    }

    /**
     * Exports the tags as item name/value pairs.
     *
     * @param pItem
     * @param outline
     */
    protected void exportTags(Item pItem, Element outline) {
        sLogger.debug("exportTags: pItem = " + pItem + ", outline = " + outline);

        TagManager tagManager = getTagManager();
        Tag[] possibleTags = tagManager.getTags();

        for (int i = 0; i < possibleTags.length; i++) {
            Tag tag = possibleTags[i];
            String name = tag.getName();

            if (pItem.hasProperty(name)) {
                String value = pItem.getString(name);
                exportItem(outline, name, value);
            }
        }
    }

    /**
     * Gets the dates in the shadow file and exports the appropriate dates in
     * OML.
     *
     * @param pItem
     * @param outline
     *
     * @throws DOMException
     * @throws ParseException
     */
    protected void exportDates(Item pItem, Element outline)
                        throws DOMException, ParseException {
        // Created is different -- it's an attribute.
        if (pItem.hasProperty(HH_CREATE_TIME)) {
            Date date = pItem.getDate(HH_CREATE_TIME);

            String formattedDate = Utils.formatDateTime(date);
            outline.setAttribute(OMLConstants.CREATED, formattedDate);
        }

        for (int i = 0; i < SHADOW_DATES.length; i++) {
            String datePropertyName = SHADOW_DATES[i];

            if (pItem.hasProperty(datePropertyName)) {
                Date date = pItem.getDate(datePropertyName);

                String formattedDate = Utils.formatDateTime(date);
                exportItem(outline, datePropertyName, formattedDate);
            }
        }
    }

    /**
     * Creates an item name and value in the current outline.
     *
     * @param outline the parent XML element.
     * @param pItemName the item's property name.
     * @param pItemValue the item's property value.
     *
     * @throws DOMException
     */
    protected void exportItem(Element outline, String pItemName,
                              String pItemValue) throws DOMException {
        sLogger.debug("exportItem: outline = " + outline + ", pItemName = "
                      + pItemName + ", pItemValue = " + pItemValue);

        Element item = Utils.createElement(outline, OMLConstants.ITEM);
        String eccoPropertyName = getEccoPropertyName(pItemName);

        item.setAttribute(OMLConstants.NAME, eccoPropertyName);
        Utils.setTextValue(item, pItemValue);
    }

    /**
     * Returns the appropriate Ecco folder given the Shadow property name.
     * Returns the original name if no mapping is found.
     * 
     * @param pShadowPropertyName the shadow name that we want to find an analogue to.      
     * 
     * @return the ecco property name, or the original pShadowPropertyName if no name is found.
     */
    protected String getEccoPropertyName(String pShadowPropertyName) {
        String modifiedName = (String) sPropertyMapping.get(pShadowPropertyName);

        if (Utils.isEmpty(modifiedName)) {
            return pShadowPropertyName;
        }

        return modifiedName;
    }

    /**
     * Sets the transformer to output the right document type.
     *
     * @param transformer the transformer to use on the DOM tree.
     */
    protected void initializeTransformer(Transformer transformer) {
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                                      OMLConstants.OML_SYSTEMID);
    }
}
