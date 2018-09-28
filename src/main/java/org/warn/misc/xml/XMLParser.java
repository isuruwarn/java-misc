package org.warn.misc.xml;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.ByteArrayInputStream;




public class XMLParser {
	
	public void parseXMLFile( String xml ){
		
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document dom = docBuilder.parse(xml);
			Element root = dom.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("ChargePoint");
			
			if( nodeList!=null && nodeList.getLength()>0 ) {
				
				for( int i = 0; i<nodeList.getLength(); i++ ) {
					
					Element chargePoint = (Element) nodeList.item(i);
					String name = getValue( chargePoint, "Id" );
					String provider = getValue( chargePoint, "Provider" );
					String zipcode = getValue( chargePoint, "ZipCode" );
					String location = getValue( chargePoint, "Location" );
					String street = getValue( chargePoint, "Street" );
					String city = getValue( chargePoint, "City" );
					
					System.out.println(name);
					System.out.println(provider);
					System.out.println(zipcode);
					System.out.println(location);
					System.out.println(street);
					System.out.println(city);
					System.out.println();
				}
			}
			
		} catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			
		} catch(SAXException se) {
			se.printStackTrace();
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	
	
	public void parseXMLString( String xml ) {
		
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document dom = docBuilder.parse( new ByteArrayInputStream(xml.getBytes()) );
			Element root = dom.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("ChargePoint");
			
			if( nodeList!=null && nodeList.getLength()>0 ) {
				
				for( int i = 0; i<nodeList.getLength(); i++ ) {
					
					Element chargePoint = (Element) nodeList.item(i);
					String name = getValue( chargePoint, "Id" );
					String provider = getValue( chargePoint, "Provider" );
					String zipcode = getValue( chargePoint, "ZipCode" );
					String location = getValue( chargePoint, "Location" );
					String street = getValue( chargePoint, "Street" );
					String city = getValue( chargePoint, "City" );
					
					System.out.println(name);
					System.out.println(provider);
					System.out.println(zipcode);
					System.out.println(location);
					System.out.println(street);
					System.out.println(city);
					System.out.println();
				}
			}
			
		} catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			
		} catch(SAXException se) {
			se.printStackTrace();
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	private String getValue( Element element, String tagName ) {
		
		String value = null;
		NodeList nodes = element.getElementsByTagName(tagName);
		
		if( nodes!=null && nodes.getLength()>0 ) {
			Element node = (Element) nodes.item(0);
			value = node.getFirstChild().getNodeValue();
		}

		return value;
	}
	
}
