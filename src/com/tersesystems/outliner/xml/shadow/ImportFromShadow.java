package com.tersesystems.outliner.xml.shadow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tersesystems.outliner.core.ImportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.core.ItemImporter;
import com.tersesystems.outliner.util.Utils;


/**
 * This class takes a Shadow XML file and converts it into a nested list of
 * Item beans.
 * 
 * <p>
 * We have have specifically designed the system to make it easy for the
 * various components to be swapped in and out. You are free to work with
 * Shadow Desktop XML documents provided you too follow the rules outlined
 * here. Shadow Desktop (and conduit) skip a lot of the hubub; only some
 * encodings are understood and many fancy XML tricks are discarded. The usual
 * still applies, of course.. spacing and newlines and such do not apply so
 * you can be free form, but Shadow will just clobber your spacing on saves.
 * Encoding supports are currently: ISO-8859-1, UTF-8, and CP1252. The CP1252
 * is actually Palm's varient of CP1252 which is identical except for a couple
 * of pass-thru characters which don't display on the handheld. Default values
 * are assumed if the fields are not included; as such, you can write very
 * stripped XML and the desktop will likely interpret it anyway. However, when
 * the desktop or conduit write, they tend to write out most of their fields
 * (including the default values). Perhaps in the future, none-default values
 * will go unwritten, so that new default values can take next iteration..
 * </p>
 * 
 * <p>
 * Note/memos are still limited to 4k, even though you can technically put as
 * much as you like into the XML. The excess will be truncated on read/write.
 * Shadow Desktop will limit user input.. I'd rather not limit the carrier,
 * but just limit data entry. The size limit is subject to change as Palm OS
 * grows and the average user unit increases in power. Likewise, the filenames
 * are limited in size to a total of 32 characters (not including the ".XML",
 * but including the "ShadP-" at the front. (The "ShadP-" comes from the
 * handheld side and is used there to make sure filenames are unique to
 * Shadow, even if the user has another file with the same name (since it
 * wouldn't have ShadP- at the front). The desktop keeps the ShadP- present so
 * that the filename is the same on both sides, so as not to confuse the user
 * who may be panicking during a backup restoration :)
 * </p>
 * 
 * <p>
 * Content is interpreted in the encoding defined in the encoding meta-tag.
 * UTF-8 is assumed if no encoding is specified. Content goes through some
 * translations; for instance, some basic trouble characters are "escaped"
 * into a weird series of characters defined one long night; they represent
 * dynamic characters that have changed in some Palm OS versions, etc. These
 * will soon be eliminated for standard XML entities.
 * </p>
 * 
 * <p>
 * Times are stored in terms of Palm Epoch, instead of Unix Epoch like the
 * standard C libraries use. Palm Epoch is Jan 1st, 1904, whereas Unix-epoch
 * is Jan 1st, 1970. So in theory, you can add/subtract a constant that is the
 * number of seconds between 1970 and 1904 to convert between the two. (Try
 * 2082844800). The list header is not currently stored in the XML files, and
 * goes undefined at this time. The outermost XML tag must be ShadowPlanFile.
 * Order for attributes is irrelevant.
 * </p>
 * 
 * <p>
 * Items are listed in-order in the XML file; children items are simply nested
 * inside of their parent item; style dictates the child item to be the last
 * thing inside of its parent item. The minimum Shadow XML file is the
 * following (it assumed default values for virtually everything):
 * </p>
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Feb 6, 2004
 */
public class ImportFromShadow implements ItemImporter, TagSupport
{
    private static final Logger sLogger = Logger.getLogger(ImportFromShadow.class);        
    
    protected static final String TAG_CONSTANT = "2";
    protected static final String EAB_TYPE = "eabType";
    protected static final String EAB_ID = "eabID";
    protected static final String LINK_EAB = "linkEAB";
    protected TagManager mTagManager;

    /**
     * The constructor.
     */
    public ImportFromShadow()
    {
        super();
    }

    /**
     * Imports a shadowXML document from the URL pURL, and converts it into an item tree.
     *
     * @param pURL the URL of the shadowXML document.
     *
     * @return the root item tree.
     *
     * @throws ImportException If the document cannot be imported.
     */
    public Item importItem(URL pURL) throws ImportException
    {
        sLogger.debug("importItem: url = " + pURL);
                
        if (pURL == null)
        {
            throw new ImportException("null pURL");
        }
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        try
        {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            File file = new File(pURL.getFile());
            Document doc = builder.parse(file);

            Element element = doc.getDocumentElement();
            Item rootItem = new Item();
            rootItem.setString(ShadowConstants.TITLE, "");
            parseContainer(element, rootItem);

            return rootItem;
        } catch (ParserConfigurationException e)
        {
            throw new ImportException(e);
        } catch (SAXException e)
        {
            throw new ImportException(e);
        } catch (IOException e)
        {
            throw new ImportException(e);
        }
    }

    /**
     * Traverses the tree, building up items from elements.
     *
     * @param pElement
     * @param pParentItem
     *
     * @throws ImportException if the document cannot be imported.
     */
    protected void parseContainer(Element pElement, Item pParentItem)
        throws ImportException
    {
        sLogger.debug("parseContainer: pElement = " + pElement + ", pParentItem = " + pParentItem);
        
        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                if (nodeName.equals(ShadowConstants.ITEM))
                {
                    Item item = parseItem(element);
                    if (item == null)
                    {
                        sLogger.debug("parseContainer: null item returned from element " + element);
                        continue;
                    }
                    
                    parseContainer(element, item);
                    pParentItem.getChildren().add(item);
                }
            }
        }
    }

    /**
     * Parses out the text in an item.
     *
     * @param element
     *
     * @return the shadow item corresponding to the element.
     *
     * @throws ImportException if the document cannot be imported.
     */
    protected Item parseItem(Element element) throws ImportException
    {
        sLogger.debug("parseItem: pElement = " + element);        
        
        // If the item has been deleted, then don't add it to the list.
        String deleted = element.getAttribute(ShadowConstants.DELETED);

        if (ShadowConstants.ONE.equals(deleted))
        {
            sLogger.debug("parseItem: deleted item, returning null..."); 
            return null;
        }

        Item item = new Item();

        // Set the title element.
        String title = Utils.getChildText(element, ShadowConstants.TITLE);
        //System.out.println("title = " + title);
        item.setString(ShadowConstants.TITLE, title);
        
        // Set the dates.
        parsePalmTime(element, item, ShadowConstants.HH_CREATE_TIME);
        parsePalmTime(element, item, ShadowConstants.HH_TARGET_TIME);
        parsePalmTime(element, item, ShadowConstants.HH_START_TIME);
        parsePalmTime(element, item, ShadowConstants.HH_FINISH_TIME);

        // Set any links.
        parseLinks(element, item);

        // Set the note element.
        String note = Utils.getChildText(element, ShadowConstants.NOTE);
        item.setString(ShadowConstants.NOTE, note);

        // Set the checked attribute.
        boolean checked = ShadowConstants.YES.equals(element.getAttribute(ShadowConstants.CHECKED));
        item.setBoolean(ShadowConstants.CHECKED, checked);
        
        // Set the priority.
        String priorityText = element.getAttribute(ShadowConstants.PRIORITY);
        int priority = 0;
        if (! Utils.isEmpty(priorityText))
        {
            priority = Integer.parseInt(priorityText);
        }
        item.setInteger(ShadowConstants.PRIORITY, priority);

        // Set the id attribute.
        String id = element.getAttribute(ShadowConstants.UNIQUE_ID);   
        item.setString(ShadowConstants.UNIQUE_ID, id);

        String localID = element.getAttribute(ShadowConstants.LOCAL_ID);
        if (! Utils.isEmpty(localID))
        {
            item.setString(ShadowConstants.LOCAL_ID, localID);
        }        
        
        return item;
    }

    /**
     * Parses out the palm dates and times from the shadow XML file.
     *
     * @param pElement
     * @param pItem
     * @param pAttributeName
     */
    protected void parsePalmTime(Element pElement, Item pItem,
                               String pAttributeName)
    {
        String dateText = Utils.getChildText(pElement, pAttributeName);
        Date dateTime = Utils.convertPalmDate(dateText);

        if (dateTime != null)
        {
            sLogger.debug("parsePalmTime: " + dateText + " = " + dateTime);
            pItem.setDate(pAttributeName, dateTime);
        }
    }

    /**
     * Parses out the links in each shadow item.  This contains the tag information
     * as well.
     *
     * @param pElement
     * @param pItem
     *
     * @throws ImportException if the document cannot be imported.
     */
    protected void parseLinks(Element pElement, Item pItem)
        throws ImportException
    {
        sLogger.debug("parsePalmTime: pElement = " + pElement + ", pItem = " + pItem);
        
        /* linkEAB eabType="2" eabID="3162891368"/> */
        NodeList linkElements = pElement.getElementsByTagName(LINK_EAB);

        for (int i = 0; i < linkElements.getLength(); i++)
        {
            Element link = (Element) linkElements.item(i);
            String id = link.getAttribute(EAB_ID);
            String attribute = link.getAttribute(EAB_TYPE);

            if (TAG_CONSTANT.equals(attribute))
            {
                TagManager tagManager = getTagManager();

                if (tagManager == null)
                {
                    throw new ImportException("null tagManager");
                }

                Tag tag = tagManager.getTagByID(id);
                pItem.setString(tag.getName(), tag.getName());
            }
        }
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
}
