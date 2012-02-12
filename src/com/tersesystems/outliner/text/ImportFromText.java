package com.tersesystems.outliner.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.tersesystems.outliner.core.ImportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.core.ItemImporter;
import com.tersesystems.outliner.util.Utils;
import com.tersesystems.outliner.xml.shadow.TagManager;
import com.tersesystems.outliner.xml.shadow.TagSupport;


/**
 * Creates a list of items from imported text.  It probably helps to be a
 * programmer if you want to modify this format.
 * 
 * <p>
 * The first element of the line is a sequence of tabs which indicates the
 * indent.
 * </p>
 * 
 * <p>
 * The next element is the localID of the item.
 * </p>
 * 
 * <p>
 * The next element is the date of the item, in MM/dd/yyyy HH:mm format.
 * </p>
 * 
 * <p>
 * The last element is the title of the item.
 * </p>
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Mar 11, 2004
 */
public class ImportFromText implements ItemImporter, TagSupport
{
    private String TITLE;

    private TagManager mTagManager;
    
    private static final boolean DEBUG = false;

    /**
     *
     */
    public ImportFromText()
    {
        super();
    }

    /**
     * Imports items from an series of lines.  Doesn't do indenting currently.
     *
     * @see com.tersesystems.outliner.core.ItemImporter#importItem(java.net.URL)
     */   
    public Item importItem(URL pURL) throws ImportException
    {   
        String filename = pURL.getFile();
        File file = new File(filename);

        if (! file.exists())
        {
            throw new ImportException("No such file " + file);
        }

        Item rootItem = new Item();

        String line = null;
        int lineCounter = 0;

        List parents = new ArrayList();
        parents.add(rootItem);

        Item parent = rootItem;
        int lastIndent = 0;
        Item lastItem = rootItem;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null)
            {
                lineCounter++;

                int indent = 0;

                while (line.charAt(indent) == '\t')
                {
                    indent++;
                }

                // Strip off the leading tabs.
                line = line.substring(indent);

                if (indent > lastIndent)
                {
                    parents.add(lastIndent, parent);
                    parent = lastItem;
                } else if (indent < lastIndent)
                {
                    parent = (Item) parents.get(indent);
                }

                Item item = createItem(line);
                if (DEBUG)
                {
                    Object[] args = 
                                    {
                                        new Integer(indent), parent, item
                                    };
                    String msg = MessageFormat.format("indent = {0}, parent = {1}, item = {2}",
                                                      args);
                    System.out.println(msg);
                }

                parent.getChildren().add(item);

                lastIndent = indent;
                lastItem = item;
            }

            return rootItem;
        } catch (FileNotFoundException e)
        {
            throw new ImportException(e);
        } catch (IOException e)
        {
            throw new ImportException(e);
        }
    }

    /**
     * Creates an item from a String.
     *
     * @param line the properties of the item.
     *
     * @return the newly created item.
     *
     * @throws ImportException if the line is empty or null.
     */
    private Item createItem(String line) throws ImportException
    {
        if (Utils.isEmpty(line))
        {
            throw new ImportException("line is empty or null");
        }

        Item item = new Item();   

        // The third element is the title.
        String title = Utils.trimQuotes(line.trim());

        // FIXME don't hardcode the property name.
        item.setString(TITLE, title);

        return item;
    }
    
    /**
     * @return Returns the tagManager.
     */
    public TagManager getTagManager()
    {
        return mTagManager;
    }

    /**
     * @param pTagManager The tagManager to set.
     */
    public void setTagManager(TagManager pTagManager)
    {
        mTagManager = pTagManager;
    }

}
