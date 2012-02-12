package com.tersesystems.outliner.xml;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tersesystems.outliner.core.ExportException;
import com.tersesystems.outliner.core.Item;
import com.tersesystems.outliner.core.ItemExporter;


/**
 * An abstract XML exporter, used to automate the process of creating
 * a DOM tree and converting it to a string.
 *
 * @author wsargent
 * @version $Revision$
 *
 * @since Apr 5, 2004
 */
public abstract class AbstractXMLExporter implements ItemExporter
{
    /**
     * An empty constructor.
     */
    public AbstractXMLExporter()
    {
        super();
    }

    /**
     * Creates and returns a document containing the XML corresponding to the
     * item tree.
     *
     * @param pRoot the root item.
     *
     * @return the document containing XML.
     *
     * @throws ExportException
     */
    public String exportItem(Item pRoot) throws ExportException
    {
        if (pRoot == null)
        {
            String msg = "pRoot is null";
            throw new ExportException(msg);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        try
        {           
            DocumentBuilder builder = dbf.newDocumentBuilder();
            
            initializeBuilder(builder);
            
            Document d = builder.newDocument();
            Element rootElement = exportRoot(pRoot, d);

            StringWriter writer = new StringWriter();
            serializeDocument(rootElement, writer);

            return writer.toString();
        } catch (ParserConfigurationException e)
        {
            throw new ExportException(e);
        } catch (TransformerConfigurationException e)
        {
            throw new ExportException(e);
        } catch (TransformerException e)
        {
            throw new ExportException(e);
        }
    }

    /**
     * Override this method to set properties on the builder before 
     * it creates the document.
     * 
     * @param builder
     */
    protected void initializeBuilder(DocumentBuilder builder)
    {
        
    }

    public abstract Element exportRoot(Item pRoot, Document d)
        throws ExportException;

    /**
     * Go through the somewhat wierd way that JAXP wants us  to serialize the
     * DOM tree to XML... http://java.sun.com/xml/jaxp/faq.html for more
     * details.
     *
     * @param rootElement
     * @param writer
     *
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    protected void serializeDocument(Element rootElement, StringWriter writer)
        throws TransformerFactoryConfigurationError, 
                   TransformerConfigurationException, TransformerException
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource(rootElement);
     
        StreamResult result = new StreamResult(writer);
        initializeTransformer(transformer);
        transformer.transform(source, result);
        
    }

    /**
     * @param transformer
     */
    protected void initializeTransformer(Transformer transformer)
    {
       
    }
}
