package com.tersesystems.outliner.core;

/**
 * An interface for objects that can take in an item and export it 
 * in a particular format.
 * 
 * @author wsargent
 * @version $Revision$ 
 * @since Mar 11, 2004
 */
public interface ItemExporter
{
    /**
     * Exports a tree of items, returning the result as a string.
     * 
     * @param pItem the root item to export.
     * @return the string representing the exported text.
     * @throws ExportException if the export cannot be completed.
     */
    public String exportItem(Item pItem) throws ExportException;
}
