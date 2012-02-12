package com.tersesystems.outliner.util;

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;


/**
 * A writer with indent and outdent support.
 *
 * @author Will Sargent
 * @version $Id: IndentWriter.java,v 1.1 2002/07/14 09:36:35 will Exp $
 */
public class IndentWriter extends Writer
{
    private static final char[] SPACES = { '\t' };
    Writer mWriter;
    private boolean mUseIndent;
    private int mIndentSize = 0;

    /**
     * Creates a new IndentWriter object.
     *
     * @param pWriter
     */
    public IndentWriter(Writer pWriter)
    {
        mWriter = pWriter;
    }

    //-------------------------------------

    /**
     * Indents the text printed.
     */
    public void indent()
    {
        if (mUseIndent)
        {
            mIndentSize++;
        }
    }

    //-------------------------------------

    /**
     * Undents the text printed.
     */
    public void undent()
    {
        if (mUseIndent)
        {
            if (mIndentSize > 0)
            {
                mIndentSize--;
            }
        }
    }

    //-------------------------------------
    // Writer implementation.
    public void close() throws IOException
    {
        mWriter.close();
    }

    /**
     *
     *
     * @throws IOException
     */
    public void flush() throws IOException
    {
        mWriter.flush();
    }

    /**
     *
     *
     * @param pBuffer
     * @param pPosition
     * @param pSize
     *
     * @throws IOException
     */
    public void write(char[] pBuffer, int pPosition, int pSize)
        throws IOException
    {
        mWriter.write(pBuffer, pPosition, pSize);
    }

    /**
     *
     *
     * @param pString
     *
     * @throws IOException
     */
    public void write(String pString) throws IOException
    {
        for (int i = 0; i < mIndentSize; i++)
        {
            mWriter.write(SPACES);
        }

        mWriter.write(pString);
    }

    /**
     * Returns the writer as a string.
     *      
     * @return the writer as a string.
     */
    public String toString()
    {
        return mWriter.toString();
    }

    //-------------------------------------

    /**
     * Writes string, adding a newline character to the end of the string.  If
     * the string already contains newline characters, then they have to be
     * padded to be in line with the rest of the spaces.
     * 
     * <p>
     * Dunno what to do about tabs.
     * </p>
     *
     * @param string
     *
     * @throws IOException
     */
    public void writeln(String string) throws IOException
    {
        if (string == null)
        {
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(string, "\n");

        while (tokenizer.hasMoreTokens())
        {
            String line = tokenizer.nextToken();
            write(line + "\n");
        }
    }

    /**
     * Returns true if the writer is indenting, false otherwise.
     *
     * @return true if the writer is indenting, false otherwise.
     */
    public boolean isUseIndent()
    {
        return mUseIndent;
    }

    /**
     * Set to true if the indenting features of the writer should be enabled.
     *
     * @param pB true if indenting should be enabled, false otherwise.
     */
    public void setUseIndent(boolean pB)
    {
        mUseIndent = pB;
    }
}
