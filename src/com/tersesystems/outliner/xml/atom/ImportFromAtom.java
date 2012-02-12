package com.tersesystems.outliner.xml.atom;

import java.net.URL;

import com.tersesystems.outliner.core.ImportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.core.ItemImporter;

/**
 * Imports an Atom document and converts it to an item tree.
 *
 * @version $Revision$
 * @author wsargent
 * @since Feb 27, 2005
 */
public class ImportFromAtom  implements ItemImporter {

    /* (non-Javadoc)
     * @see com.tersesystems.outliner.core.ItemImporter#importItem(java.net.URL)
     */
    public Item importItem(URL pFilename) throws ImportException {
        // TODO Implement ImportFromAtom.importItem
        return null;
    }

}
