package com.tersesystems.outliner.core;

/**
 *
 *
 * @author wsargent
 * @version $Revision$
 * @since Mar 10, 2004
 */
public class ExportException extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = -2616282367498227177L;

    /**
     *
     */
    public ExportException()
    {
        super();
    }

    /**
     * @param message
     */
    public ExportException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public ExportException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ExportException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
