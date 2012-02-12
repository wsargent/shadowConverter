package com.tersesystems.outliner.core;

/**
 * An exception thrown if a property is not found in an item.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Apr 6, 2004
 */
public class PropertyNotFoundException extends RuntimeException
{
    /**
     *
     */
    private static final long serialVersionUID = -1952580644406299889L;

    /**
     * Empty constructor.
     */
    public PropertyNotFoundException()
    {
        super();
    }

    /**
     * Creates an exception with a message.
     *
     * @param message
     */
    public PropertyNotFoundException(String message)
    {
        super(message);
    }

    /**
     * Creates an exception with a Throwable.
     *
     * @param cause
     */
    public PropertyNotFoundException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates an exception with a message and a throwable.
     *
     * @param message
     * @param cause
     */
    public PropertyNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
