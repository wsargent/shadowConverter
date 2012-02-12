package com.tersesystems.outliner.core;

/**
 *
 *
 * @author wsargent
 * @version $Revision$
 * @since Mar 11, 2004
 */
public class ImportException extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 6678531403359324393L;

    /**
     *
     */
    public ImportException()
    {
        super();
    }

    /**
     * @param message
     */
    public ImportException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public ImportException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ImportException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
