package com.tersesystems.outliner.xml.oml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.tersesystems.outliner.core.EccoConstants;
import com.tersesystems.outliner.core.ImportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.core.ItemImporter;
import com.tersesystems.outliner.util.Utils;
import com.tersesystems.outliner.xml.InternalDTDResolver;


/**
 * Imports an outline from OML.  This assumes that properties and the like are
 * going to be exported from Ecco. 
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Apr 4, 2004
 */
public class ImportFromOML implements ItemImporter
{
    private static Logger sLogger = Logger.getLogger(ImportFromOML.class);
    
    //private TagManager mTagManager;

    /* (non-Javadoc)
     * @see com.tersesystems.shadow.core.ItemImporter#importItem(java.net.URL)
     */
    public Item importItem(URL pURL) throws ImportException
    {
        if (pURL == null)
        {
            throw new ImportException("null pURL");
        }

        try
        {
            // Use the retarded spec of Xerces to stop it connecting to the network...
            DOMParser parser = new DOMParser();

            EntityResolver resolver = new InternalDTDResolver(OMLConstants.OML_DTD, OMLConstants.OML_DTD_RESOURCE_PATH);
            parser.setEntityResolver(resolver);
            File file = new File(pURL.getFile());
            InputSource is = new InputSource(new BufferedReader(new FileReader(file)));
            parser.parse(is);
            Document doc = parser.getDocument();

            Element element = doc.getDocumentElement();
            Item rootItem = new Item();

            parseContainer(element, rootItem);

            return rootItem;
        } catch (SAXException e)
        {     
            //sLogger.error(e.getMessage(), e);
            String msg = "Cannot parse document: " + pURL;
            throw new ImportException(msg, e);
        } catch (IOException e)
        {
            //sLogger.error(e.getMessage(), e);
            String msg = "Cannot read document: " + pURL;
            throw new ImportException(msg, e);
        }
    }

    /**
     * Parses out the document root.
     *
     * @param pElement
     * @param rootItem
     *
     * @throws ImportException if the document cannot be imported.
     */
    protected void parseContainer(Element pElement, Item rootItem)
        throws ImportException
    {
        if (sLogger.isDebugEnabled()) {
            String msg = "parseContainer: element = {0}, item = {1}";
            Object[] params = { pElement, rootItem };
            msg = MessageFormat.format(msg, params);
            sLogger.debug(msg);
        }  
        
        // We start with "oml"
        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                // "oml" contains "head" and "body"
                if (OMLConstants.BODY.equals(nodeName))
                {
                    parseBody(element, rootItem);
                }
            }
        }
    }

    /**
     * Parses the body of the OML.
     *
     * @param pElement
     * @param rootItem
     *
     * @throws ImportException if the document cannot be imported.
     */
    protected void parseBody(Element pElement, Item rootItem)
        throws ImportException
    {
        if (sLogger.isDebugEnabled()) {
            String msg = "parseBody: element = {0}, item = {1}";
            Object[] params = { pElement, rootItem };
            msg = MessageFormat.format(msg, params);
            sLogger.debug(msg);
        }        
        
        // "body" contains "outline"
        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                if (OMLConstants.OUTLINE.equals(nodeName))
                {
                    parseOutline(element, rootItem);
                }
            }
        }
    }

    /**
     * Parses out the outline element.
     *
     * @param pElement
     * @param pItem
     *
     * @throws ImportException if the document cannot be imported.
     */
    protected void parseOutline(Element pElement, Item pItem)
        throws ImportException
    {
        if (sLogger.isDebugEnabled()) {
            String msg = "parseOutline: element = {0}, item = {1}";
            Object[] params = { pElement, pItem };
            msg = MessageFormat.format(msg, params);
            sLogger.debug(msg);
        }
        
        // "outline" contains "outline" and "item".
        // If we have an outline, we automatically have an item.
        Item item = new Item();
        pItem.getChildren().add(item);

        String text = pElement.getAttribute(OMLConstants.TEXT);
        item.set(OMLConstants.TEXT, text);

        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                if (OMLConstants.OUTLINE.equals(nodeName))
                {
                    parseOutline(element, item);
                } else if (OMLConstants.ITEM.equals(nodeName))
                {
                    parseItem(element, item);
                }
            }
        }
    }

    /**
     * Parses out the item data.
     *
     * @param element
     * @param item
     *
     * @throws ImportException if the document cannot be imported.
     */
    protected void parseItem(Element element, Item item)
        throws ImportException
    {
        if (sLogger.isDebugEnabled()) {
            String msg = "parseItem: element = {0}, item = {1}";
            Object[] params = { element, item };
            msg = MessageFormat.format(msg, params);
            sLogger.debug(msg);
        }
        
        // item contains only the name and the value.
        String name = element.getAttribute(OMLConstants.NAME);
        String value = Utils.getTextValue(element);

        // Don't do anything if the name is null.
        if (Utils.isEmpty(name))
        {
            return;
        }

        if (isDate(name))
        {
            Date dateValue;

            try
            {
                dateValue = Utils.parseDateTime(value);
            } catch (ParseException e)
            {
                throw new ImportException(e);
            }

            item.setDate(name, dateValue);
        } else
        {
            item.set(name, value);
        }
    }

    /**
     * Returns true if the property is a date, false otherwise.
     *
     * @param name
     *
     * @return  true if the property is a date, false otherwise.
     */
    protected boolean isDate(String name)
    {
        for (int i = 0; i < EccoConstants.DATE_PROPERTIES.length; i++)
        {
            String propertyName = EccoConstants.DATE_PROPERTIES[i];

            if (propertyName.equals(name))
            {
                return true;
            }
        }

        return false;
    }
}
