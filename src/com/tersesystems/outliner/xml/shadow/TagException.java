package com.tersesystems.outliner.xml.shadow;

/**
 * An exception thrown when a tag cannot be processed.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Mar 26, 2004
 */
public class TagException extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = -626814070432223334L;

    /**
     *
     */
    public TagException()
    {
        super();
    }

    /**
     * Creates exception with a message.
     *
     * @param pMessage
     */
    public TagException(String pMessage)
    {
        super(pMessage);
    }

    /**
     *  Creates exception with a throwable.
     *
     * @param pCause
     */
    public TagException(Throwable pCause)
    {
        super(pCause);
    }

    /**
     * Creates exception with a message and a throwable.
     *
     * @param pMessage
     * @param pCause
     */
    public TagException(String pMessage, Throwable pCause)
    {
        super(pMessage, pCause);
    }
}
