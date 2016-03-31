/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.handlers;

import com.sun.mail.handlers.text_plain;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataSource;
import javax.mail.internet.ContentType;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class text_xml
extends text_plain {
    private final DataFlavor[] flavors = new DataFlavor[]{new ActivationDataFlavor(String.class, "text/xml", "XML String"), new ActivationDataFlavor(String.class, "application/xml", "XML String"), new ActivationDataFlavor(StreamSource.class, "text/xml", "XML"), new ActivationDataFlavor(StreamSource.class, "application/xml", "XML")};

    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[])this.flavors.clone();
    }

    public Object getTransferData(DataFlavor df, DataSource ds) throws IOException {
        for (int i = 0; i < this.flavors.length; ++i) {
            DataFlavor aFlavor = this.flavors[i];
            if (!aFlavor.equals(df)) continue;
            if (aFlavor.getRepresentationClass() == String.class) {
                return super.getContent(ds);
            }
            if (aFlavor.getRepresentationClass() == StreamSource.class) {
                return new StreamSource(ds.getInputStream());
            }
            return null;
        }
        return null;
    }

    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if (!this.isXmlType(mimeType)) {
            throw new IOException("Invalid content type \"" + mimeType + "\" for text/xml DCH");
        }
        if (obj instanceof String) {
            super.writeTo(obj, mimeType, os);
            return;
        }
        if (!(obj instanceof DataSource) && !(obj instanceof Source)) {
            throw new IOException("Invalid Object type = " + obj.getClass() + ". XmlDCH can only convert DataSource or Source to XML.");
        }
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(os);
            if (obj instanceof DataSource) {
                transformer.transform(new StreamSource(((DataSource)obj).getInputStream()), result);
            } else {
                transformer.transform((Source)obj, result);
            }
        }
        catch (Exception ex) {
            throw new IOException("Unable to run the JAXP transformer on a stream " + ex.getMessage());
        }
    }

    private boolean isXmlType(String type) {
        try {
            ContentType ct = new ContentType(type);
            return ct.getSubType().equals("xml") && (ct.getPrimaryType().equals("text") || ct.getPrimaryType().equals("application"));
        }
        catch (Exception ex) {
            return false;
        }
    }
}

