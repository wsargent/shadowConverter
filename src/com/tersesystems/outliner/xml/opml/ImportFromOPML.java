

package com.tersesystems.outliner.xml.opml;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tersesystems.outliner.core.ImportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.core.ItemImporter;

/**
 * Imports from OPML into an item tree.
 *
 * @version $Revision$
 * @author wsargent
 * @since Feb 26, 2005
 */
public class ImportFromOPML implements ItemImporter {
    
    /* (non-Javadoc)
     * @see com.tersesystems.outliner.core.ItemImporter#importItem(java.net.URL)
     */
    public Item importItem(URL pURL) throws ImportException {
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            File file = new File(pURL.getFile());
            Document doc = builder.parse(file);

            Element element = doc.getDocumentElement();
            Item rootItem = new Item();

            parseContainer(element, rootItem);

            return rootItem;
        } catch (ParserConfigurationException e)
        {
            throw new ImportException(e.toString(), e);
        } catch (SAXException e)
        {            
            throw new ImportException(e.toString(), e);
        } catch (IOException e)
        {
            throw new ImportException(e.toString(), e);
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
        // We start with "opml"
        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                // OPML contains head and body, but we're only interested
                // in the body.  fnar fnar.
                if (OPMLConstants.BODY.equals(nodeName))
                {
                    parseBody(element, rootItem);
                }
            }
        }
    }

    /**
     * Parses the body of the OPML.
     *
     * @param pElement
     * @param rootItem
     *
     * @throws ImportException if the document cannot be imported.
     */
    protected void parseBody(Element pElement, Item rootItem)
        throws ImportException
    {
        // "body" contains "outline"
        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                if (OPMLConstants.OUTLINE.equals(nodeName))
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
        // "outline" contains "outline".
        // If we have an outline, we automatically have an item.
        Item item = new Item();
        pItem.getChildren().add(item);

        String text = pElement.getAttribute(OPMLConstants.TEXT);
        item.set(OPMLConstants.TEXT, text);

        NodeList children = pElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String nodeName = node.getNodeName();

                if (OPMLConstants.OUTLINE.equals(nodeName))
                {
                    parseOutline(element, item);
                } 
            }
        }
    }

}
