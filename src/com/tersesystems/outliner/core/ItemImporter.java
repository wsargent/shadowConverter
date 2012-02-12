package com.tersesystems.outliner.core;

import java.net.URL;

/**
 * The interface for importing items.
 * 
 * @author wsargent
 * @version $Revision$ 
 * @since Mar 11, 2004
 */
public interface ItemImporter
{ 
    public Item importItem(URL pFilename) throws ImportException;
}
