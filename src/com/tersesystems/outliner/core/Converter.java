package com.tersesystems.outliner.core;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.tersesystems.outliner.util.UserMessages;
import com.tersesystems.outliner.util.Utils;
import com.tersesystems.outliner.xml.oml.ExportToOML;
import com.tersesystems.outliner.xml.oml.ImportFromOML;
import com.tersesystems.outliner.xml.shadow.ExportToShadow;
import com.tersesystems.outliner.xml.shadow.ImportFromShadow;
import com.tersesystems.outliner.xml.shadow.ShadowPreferences;
import com.tersesystems.outliner.xml.shadow.TagException;
import com.tersesystems.outliner.xml.shadow.TagManager;
import com.tersesystems.outliner.xml.shadow.TagSupport;


/**
 * Reads in items in one format, and writes them out in another.
 * <p>
 * TODO Provide a command line interface to set preferences.
 * TODO Provide javadoc to show command lines.
 * </p>
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Mar 8, 2004
 */
public class Converter
{
    private static final Logger sLogger = Logger.getLogger(Converter.class);
    private static final UserMessages sMessages = UserMessages.getUserMessages(Converter.class);
    
    private static final String MSG_CANNOT_CONVERT = "cannotConvert";
    private static final String MSG_HELP_TEXT = "helpText";
    
    private static final String OML = "oml";
    private static final String SHADOW = "shadow";
    
    
    protected TagManager mTagManager;
    protected String mExporterName;
    protected String mImporterName;
  
    protected String mShadowName;
    protected String mTextFile;
    protected String mUser;
    protected boolean mDestructive;
    

    /**
     * An empty constructor.
     */
    public Converter()
    {
        super();
    }

    /**
     * Gets the options necessary to call the converter and calls the convert
     * method.
     *
     */
    protected void convertFile()        
    {
        try
        {
            String xmlname = getShadowName();
            boolean destructive = isDestructive();
            String user = getUser();
            
            // Make sure the tag manager is set up...
            File tagsFile = ShadowPreferences.getShadowTags(user);
            TagManager tagManager = new TagManager(tagsFile);
            setTagManager(tagManager);
    
            File outfile = null;
            File infile = null;
            ItemImporter importer = getImporter();
            ItemExporter exporter = getExporter();
    
            String textfile = getTextFile();
    
            if (SHADOW.equals(getImporterName()))
            {
                infile = getShadowFile(user, xmlname);
            } else if (OML.equals(getImporterName()))
            {
                infile = new File(textfile);
            }
    
            if (destructive)
            {
                if (SHADOW.equals(getExporterName()))
                {
                    outfile = getShadowFile(user, xmlname);
                } else if (OML.equals(getExporterName()))
                {
                    outfile = new File(textfile);
                }
            }
    
            convert(infile, outfile, importer, exporter);
        } catch (Exception e) {            
            Object[] args = { 
                getImporterName(),
                getExporterName(),
                e.getMessage()
            };
            String msg = sMessages.getUserMessage(MSG_CANNOT_CONVERT, args);
            sLogger.error(msg, e);
        }
    }

    /**
     * Gets the tag manager for this converter.
     *
     * @return the tag manager for this converter.
     */
    public TagManager getTagManager()
    {
        return mTagManager;
    }

    /**
     * Sets the tag manager for this converter.
     *
     * @param tagManager
     */
    public void setTagManager(TagManager tagManager)
    {
        mTagManager = tagManager;
    }

    /**
     * Returns the shadow file given by pXMLName.
     *
     * @param pUsername The directory containing the shadow XML files.
     * @param pXmlname the name of the shadow file on the handheld.
     *
     * @return the shadow file given by pXMLName.
     */
    protected File getShadowFile(String pUsername, String pXmlname)
    {
        String shadowDir;

        if (! Utils.isEmpty(pUsername))
        {
            shadowDir = ShadowPreferences.getShadowDirectory(pUsername);
        } else
        {
            shadowDir = ShadowPreferences.getShadowDirectory();
        }

        String shadpFile = "ShadP-" + pXmlname + ".XML";
        File f = new File(shadowDir, shadpFile);

        return f;
    }

    /**
     * 
     *
     * @param pInFile 
     * @param pOutFile 
     * @param importer 
     * @param exporter 
     *
     * @throws ImportException 
     * @throws ExportException 
     * @throws IOException 
     * @throws IllegalArgumentException 
     */
    public void convert(File pInFile, File pOutFile, ItemImporter importer,
        ItemExporter exporter)
        throws ImportException, ExportException, IOException
    {
        if (importer == null)
        {
            throw new IllegalArgumentException("null importer");
        }

        if (exporter == null)
        {
            throw new IllegalArgumentException("null exporter");
        }

        if (pInFile == null)
        {
            throw new IllegalArgumentException("null infile");
        }

        if (! pInFile.exists())
        {
            System.err.println("infile " + pInFile + " does not exist!");
            System.exit(-1);
        }

        URL fileURL = pInFile.toURL();
        Item item = importer.importItem(fileURL);
        
        sLogger.debug("convert: item = " + item);
        
        String text = exporter.exportItem(item);

        if (pOutFile != null)
        {
            writeFile(text, pOutFile);
        } else
        {
            System.out.print(text);
        }
    }

    /**
     * Writes the text to the file given by outFilename
     *
     * @param text
     * @param pFile
     *
     * @throws IOException
     */
    protected void writeFile(String text, File pFile)
        throws IOException
    {
        File outFile = pFile;
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

        // At least create the file, even if there's nothing in it.      
        if (text == null)
        {
            outFile.createNewFile();

            return;
        }

        try
        {
            writer.write(text);
            writer.flush();
        } finally
        {
            writer.close();
        }
    }

    /**
     * 
     *
     * @return gets the exporter.
     *
     * @throws TagException 
     * @throws IllegalArgumentException 
     */
    protected ItemExporter getExporter() throws TagException
    {
        String toFormat = getExporterName();
        ItemExporter exporter = null;

        if (SHADOW.equalsIgnoreCase(toFormat))
        {
            exporter = new ExportToShadow();
        } else if (OML.equalsIgnoreCase(toFormat))
        {
            exporter = new ExportToOML();
        } else
        {
            throw new IllegalArgumentException("unknown format: " + toFormat);
        }

        initTagSupport(exporter);

        return exporter;
    }

    /**
     * Sets the tag manager on the importer or exporter if it supports that
     * interface.
     *
     * @param pObject
     *
     * @throws TagException
     */
    protected void initTagSupport(Object pObject) throws TagException
    {
        if (pObject instanceof TagSupport)
        {
            TagManager tagManager = getTagManager();
            ((TagSupport) pObject).setTagManager(tagManager);
        }
    }

    /**
     * Returns the appropriate importer for the string.
     *
     * @return the appropriate importer for the string.
     *
     * @throws TagException 
     * @throws IllegalArgumentException 
     */
    protected ItemImporter getImporter() throws TagException
    {       
        ItemImporter importer = null;
        String fromFormat = getImporterName();

        if (SHADOW.equalsIgnoreCase(fromFormat))
        {
            importer = new ImportFromShadow();
        } else if (OML.equalsIgnoreCase(fromFormat))
        {
            importer = new ImportFromOML();
        } else
        {
            throw new IllegalArgumentException("unknown format: " + fromFormat);
        }

        initTagSupport(importer);

        return importer;
    }

    /**
     * Runs the arguments through Getopts and breaks it down to arguments passed to the
     * converter.
     *
     * @param argv
     *
     * @throws ImportException 
     * @throws ExportException 
     * @throws IOException 
     * @throws TagException 
     */
    public static void main(String[] argv)
        throws ImportException, ExportException, IOException, TagException
    {
        if ((argv == null) || (argv.length == 0))
        {
            help();

            return;
        }

        Converter converter = new Converter();
        
        String propertiesFile = argv[0];
        Properties props = new Properties();
        InputStream is = new BufferedInputStream(new FileInputStream(propertiesFile));
        props.load(is);
        
        sLogger.debug("properties = " + props);
      
        // XXX convert these to constants.      
        converter.setUser(props.getProperty("user"));
        converter.setTextFile(props.getProperty("file"));
        converter.setShadowName(props.getProperty("shadowName"));
        Boolean destObj = Boolean.valueOf(props.getProperty("destructive"));
        converter.setDestructive(destObj.booleanValue());
        
        String importerName = props.getProperty("importer");
        converter.setImporterName(importerName);
        
        String exporterName = props.getProperty("exporter");
        converter.setExporterName(exporterName);

        converter.convertFile();
    }

    /**
     * 
     *
     * @param pB
     */
    protected void setDestructive(boolean pB)
    {
        mDestructive = pB;
    }

    /**
     * 
     *
     * @param pValue
     */
    protected void setShadowName(String pValue)
    {
        mShadowName = pValue;
    }

    /**
     * 
     *
     * @param pValue
     */
    protected void setTextFile(String pValue)
    {
        mTextFile = pValue;
    }

    /**
     * 
     *
     * @param pValue
     */
    protected void setUser(String pValue)
    {
        mUser = pValue;
    }

    /**
     * Prints out the help text.
     */
    private static void help()
    {
        String helpText = sMessages.getUserMessage(MSG_HELP_TEXT);
        System.out.println(helpText);
    }

    /**
     * 
     *
     * @return Returns the destructive.
     */
    public boolean isDestructive()
    {
        return mDestructive;
    }

    /**
     * 
     *
     * @return Returns the shadowFile.
     */
    public String getShadowName()
    {
        return mShadowName;
    }

    /**
     * 
     *
     * @return Returns the textFile.
     */
    public String getTextFile()
    {
        return mTextFile;
    }

    /**
     * 
     *
     * @return Returns the user.
     */
    public String getUser()
    {
        return mUser;
    }

    /**
     * Gets the name of the exporter.
     *
     * @return the name of the exporter.
     */
    public String getExporterName()
    {
        return mExporterName;
    }

    /**
     * Gets the name of the importer.
     *
     * @return the name of the importer.
     */
    public String getImporterName()
    {
        return mImporterName;
    }

    /**
     * Sets the name of the exporter.
     *
     * @param pString the name of the exporter.
     */
    public void setExporterName(String pString)
    {
        mExporterName = pString;
    }

    /**
     * Sets the name of the importer.
     *
     * @param pString the name of the importer.
     */
    public void setImporterName(String pString)
    {
        mImporterName = pString;
    }
}
