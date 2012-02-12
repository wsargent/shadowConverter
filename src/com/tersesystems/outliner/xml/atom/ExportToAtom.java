package com.tersesystems.outliner.xml.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tersesystems.outliner.core.ExportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.xml.AbstractXMLExporter;

/**
 * Exports an item tree to Atom format.
 *
 * @version $Revision$
 * @author wsargent
 * @since Feb 27, 2005
 */
public class ExportToAtom extends AbstractXMLExporter {

    /* (non-Javadoc)
     * @see com.tersesystems.outliner.xml.AbstractXMLExporter#exportRoot(com.tersesystems.outliner.core.Item, org.w3c.dom.Document)
     */
    public Element exportRoot(Item pRoot, Document pD) throws ExportException {
        // TODO Implement ExportToAtom.exportRoot
        return null;
    }

}
