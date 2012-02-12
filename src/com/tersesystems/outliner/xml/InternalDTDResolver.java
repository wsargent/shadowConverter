package com.tersesystems.outliner.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


/**
 * Believe it or not, this is the only way to keep Xerces from grabbing stuff
 * from the network.
 *
 * @version $Revision$
 * @author wsargent
 * @since Mar 24, 2005
 */
public class InternalDTDResolver implements EntityResolver {
    private static Logger sLogger = Logger.getLogger(InternalDTDResolver.class);
    
    protected String mSystemId;
    protected String mDTDPath;
    
    public InternalDTDResolver(String pSystemId, String pDTDPath) {
        mSystemId = pSystemId;
        mDTDPath = pDTDPath;
    }
    
    /**
     *
     *
     * @param publicId param
     * @param systemId param
     *
     * @return return
     */
    public InputSource resolveEntity(String publicId, String systemId) 
    {      
        if (sLogger.isDebugEnabled()) 
        {
            String msg = "publicId = {0}, systemId = {1}";
            Object[] params = { publicId, systemId }; 
            msg = MessageFormat.format(msg, params);
            sLogger.debug(msg);            
        }
        
        if (systemId.equals(mSystemId)) 
        {
            InputStream in = getClass().getResourceAsStream(mDTDPath);
            
            if (sLogger.isDebugEnabled()) 
            {
                try 
                {
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = r.readLine()) != null) {
                        sLogger.debug(line);
                    }
                } catch (IOException ie) {
                    sLogger.error(ie.getMessage(), ie);
                }
            }
                
            return new InputSource(in);
        }

        return null;
    }
}
