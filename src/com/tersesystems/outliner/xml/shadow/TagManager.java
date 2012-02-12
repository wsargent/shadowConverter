package com.tersesystems.outliner.xml.shadow;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tersesystems.outliner.core.ImportException;
import com.tersesystems.outliner.core.Item;


/**
 * Parses out the tags from the shadow tags file and presents the information
 * as an object.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Mar 23, 2004
 */
public class TagManager
{
    private List mTags = new ArrayList();
    private Map mTagsById = new HashMap();
    private Map mTagsByName = new HashMap();

    /*
     * <ShadowTagFile>
              <item>
                <title>Unfiled</title>
              </item>
              <item>
                <title>Contexts</title>
                <item>
                  <title>3162891898</title>
                  <note>Calls</note>
                </item>
                <item>
                  <title>3162891832</title>
                  <note>Computer</note>
                </item>
                <item>
                  <title>3162891368</title>
                  <note>Errands</note>
                </item>
                <item>
                  <title>3162891440</title>
                  <note>Home</note>
                </item>
                <item>
                  <title>3162891449</title>
                  <note>Office</note>
                </item>
              </item>
              <item>
                <title>Projects</title>
              </item>
             </ShadowTagFile>
     */
    /**
     * @param pTagsFile
     * @throws TagException
     *
     */
    public TagManager(File pTagsFile) throws TagException
    {
        super();
        parseTags(pTagsFile);
    }

    /**
     * Gets the first tag with the given ID.
     *
     * @param pID
     *
     * @return the first tag with the given ID.
     */
    public Tag getTagByID(String pID)
    {
        return (Tag) mTagsById.get(pID);
    }

    /**
     * Gets the first tag with the given name.
     *
     * @param pName
     *
     * @return the first tag with the given name.
     */
    public Tag getTagByName(String pName)
    {
        return (Tag) mTagsByName.get(pName);
    }

    /**
     * Returns the array of tags.
     *
     * @return the array of tags.
     */
    public Tag[] getTags()
    {
        return (Tag[]) mTags.toArray(new Tag[0]);
    }
    
    /**
     * Parses out the tags in the tags file.
     *
     * @param pFile the tags file.
     *
     * @throws TagException if the tags could not be parsed.
     */
    protected synchronized void parseTags(File pFile) throws TagException
    {
        ImportFromShadow importer = new ImportFromShadow();

        try
        {
            URL url = pFile.toURL();
            Item rootTags = importer.importItem(url);

            List contexts = rootTags.getChildren();

            for (Iterator iter = contexts.iterator(); iter.hasNext();)
            {
                Item item = (Item) iter.next();
                List tags = item.getChildren();

                for (Iterator iterator = tags.iterator(); iterator.hasNext();)
                {
                    Item tagItem = (Item) iterator.next();
                    String id = (String) tagItem.get(ShadowConstants.TITLE);
                    String name = (String) tagItem.get(ShadowConstants.NOTE);
                                        
                    Tag tag = new Tag(id, name);
                    mTagsById.put(id, tag);
                    mTagsByName.put(name, tag);

                    mTags.add(tag);
                }
            }
        } catch (ImportException e)
        {
            String msg = "Cannot get tags: " + e;
            throw new TagException(msg, e);
        } catch (MalformedURLException e)
        {
            String msg = "Cannot parse URL: " + e;
            throw new TagException(msg, e);
        }
    }

}
