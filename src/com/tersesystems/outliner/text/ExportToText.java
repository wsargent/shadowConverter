package com.tersesystems.outliner.text;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import com.tersesystems.outliner.core.ExportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.core.ItemExporter;
import com.tersesystems.outliner.util.IndentWriter;
import com.tersesystems.outliner.xml.shadow.TagManager;


/**
 * This class writes out items as indented text according to the format
 * described in ImportFromText.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Mar 11, 2004
 */
public class ExportToText implements ItemExporter
{
    private static final String TITLE = "title";
    private TagManager mTagManager;

    /**
     *
     */
    public ExportToText()
    {
        super();
    }

    /**
     * Writes an indented list of titles.
     *
     * @param pItem the root item to export to a text format. 
     *
     * @return a String containing tab indented lines.
     *
     * @throws ExportException if the document cannot be exported.
     */
    public String exportItem(Item pItem) throws ExportException
    {
        StringWriter stringWriter = new StringWriter();
        IndentWriter writer = new IndentWriter(stringWriter);
        writer.setUseIndent(true);

        try
        {
            // Don't iterate over the root directly.
            List list = pItem.getChildren();

            for (Iterator iter = list.iterator(); iter.hasNext();)
            {
                Item item = (Item) iter.next();
                exportItem(writer, item);
            }

            return stringWriter.toString();
        } catch (IOException e)
        {
            throw new ExportException(e);
        } catch (ParseException e)
        {
            throw new ExportException(e);
        }
    }

    public void exportItem(IndentWriter pWriter, Item pItem)
        throws IOException, ParseException
    {
        String title = pItem.getString(TITLE);
        StringBuffer b = new StringBuffer();

        b.append(title);
        pWriter.writeln(b.toString());

        pWriter.indent();

        List children = pItem.getChildren();

        for (int i = 0; i < children.size(); i++)
        {
            Item item = (Item) children.get(i);
            exportItem(pWriter, item);
        }

        pWriter.undent();
    }

    /* (non-Javadoc)
     * @see com.tersesystems.shadow.core.TagSupport#getTagManager()
     */
    public TagManager getTagManager()
    {
        return mTagManager;
    }

    /* (non-Javadoc)
     * @see com.tersesystems.shadow.core.TagSupport#setTagManager(com.tersesystems.shadow.core.TagManager)
     */
    public void setTagManager(TagManager pTagManager)
    {
        mTagManager = pTagManager;
    }
}
