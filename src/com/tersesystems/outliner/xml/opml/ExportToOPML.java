package com.tersesystems.outliner.xml.opml;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tersesystems.outliner.core.ExportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.util.Utils;
import com.tersesystems.outliner.xml.AbstractXMLExporter;
import com.tersesystems.outliner.xml.oml.OMLConstants;
import com.tersesystems.outliner.xml.shadow.ShadowConstants;

/**
 * Exports from an item to OPML format.
 *
 * @version $Revision$
 * @author wsargent
 * @since Feb 26, 2005
 */
public class ExportToOPML extends AbstractXMLExporter {
    protected static final String NOTE = ShadowConstants.NOTE;
    protected static final String TITLE = ShadowConstants.TITLE;
    protected static final String HH_CREATE_TIME = ShadowConstants.HH_CREATE_TIME;
    private static final Logger sLogger = Logger.getLogger(ExportToOPML.class);


    /* (non-Javadoc)
     * @see com.tersesystems.outliner.xml.AbstractXMLExporter#exportRoot(com.tersesystems.outliner.core.Item, org.w3c.dom.Document)
     */
    public Element exportRoot(Item pRoot, Document d) throws ExportException {
                
        if (d == null) {
            throw new ExportException("null document");
        }

        if (pRoot == null) {
            throw new ExportException("null pRoot");
        }

        sLogger.debug("exportRoot: pRoot = " + pRoot);

        //DocumentType docType = d.getDoctype();
        Element opml = d.createElement(OPMLConstants.OPML);
        d.appendChild(opml);

        opml.setAttribute(OPMLConstants.VERSION, OPMLConstants.OPML_VERSION_NUMBER);

        try {
            Element head = Utils.createElement(opml, OPMLConstants.HEAD);            
            Element dateModified = Utils.createElement(head, OPMLConstants.DATE_MODIFIED);
            String now = Utils.formatDateTime(new Date());
            Utils.setTextValue(dateModified, now);
            
            Element body = Utils.createElement(opml, OPMLConstants.BODY);

            List children = pRoot.getChildren();
            
            for (Iterator iter = children.iterator(); iter.hasNext();) {
                Item child = (Item) iter.next();
                exportChild(body, child);
            }
        } catch (DOMException e) {
            throw new ExportException(e);
        } catch (ParseException e) {
            throw new ExportException(e);
        }

        return opml;
    }

    /**
     * Exports the children of the outline.
     *
     * @param pParent
     * @param pItem
     *
     * @throws ExportException if the document can't be exported.
     * @throws DOMException
     * @throws ParseException
     */
    protected void exportChild(Element pParent, Item pItem)
                        throws ExportException, DOMException, ParseException {
        sLogger.debug("exportChild: pParent = " + pParent + ", item = " + pItem);

        Element outline = Utils.createElement(pParent, OMLConstants.OUTLINE);

        // OPML can't handle notes.
//        if (pItem.hasProperty(NOTE)) {
//            String note = pItem.getString(NOTE);
//
//            if (! Utils.isEmpty(note)) {             
//                outline.setAttribute(OPMLConstants.TEXT, note);                
//            }
//        }

        // Set the outline
        String title = pItem.getString(TITLE);
        outline.setAttribute(OMLConstants.TEXT, title);

        List children = pItem.getChildren();

        for (Iterator iter = children.iterator(); iter.hasNext();) {
            Item item = (Item) iter.next();
            exportChild(outline, item);
        }
    }

}
