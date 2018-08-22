package me.sigtrap.embeddedee.core.jpa.persistence;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JpaPersistenceHandler extends DefaultHandler {

    private String unitName;

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        System.out.println("Start Element :" + qName);

        if(localName.equals("persistence-unit")) {
            unitName = attributes.getValue("name");
        }
    }

    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        System.out.println("End Element :" + qName);

    }

    public String getUnitName() {
        return unitName;
    }
}
