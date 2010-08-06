package com.barefoot.pocketshake.workers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

import com.barefoot.pocketshake.data.EarthQuake;
import com.barefoot.pocketshake.exceptions.InvalidFeedException;

public class QuakeFeedParser {
	
	private InputStream feedStream;
	
	public QuakeFeedParser(InputStream feedStream) {
		this.feedStream = feedStream;
	}
	
	public ArrayList<EarthQuake> asParsedObject() {
		ArrayList<EarthQuake> parsedObjectList = new ArrayList<EarthQuake>();
		if (feedStream != null) {
			Document doc = getDocument();
			NodeList nodeLst = getEntryNodeList(doc);
			doc.getDocumentElement().normalize();
			
			Node currentNode = null;
			int id = 0; //Never used while creating
			String identifier = null;
			String title = null;
			String cordinates = null;
			String dateTime = null;
			String href = null;
			
			for (int size = 0; size < nodeLst.getLength(); size++) {
				currentNode = nodeLst.item(size);
			    
			    if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
			    	identifier = getNodeValueWithTagNameAs(currentNode, "id", null);
			    	title = getNodeValueWithTagNameAs(currentNode, "title", null);
			    	cordinates = getNodeValueWithTagNameAs(currentNode, "georss:point", null);
			    	dateTime = getNodeValueWithTagNameAs(currentNode, "updated", null);
			    	href = getNodeValueWithTagNameAs(currentNode, "link", "href");
			    	try {
			    		parsedObjectList.add(new EarthQuake(id, identifier, title, cordinates, dateTime, href));
			    	} catch(InvalidFeedException ife) {
			    		Log.e("FeedParser","Feed bombed, don't know whats wrong. Error is "+ife);
			    		//just procrastinate the feed sync until next time out happens
			    	}
			    }
			}
		}
		
		return parsedObjectList;
	}
	
	private String getNodeValueWithTagNameAs(Node currentNode, String tagName, String attrName) {
		Node valueNode = null;
		Element entryNode = (Element) currentNode;
		NodeList idElementList = null;
		Element idElement = null;
		idElementList = entryNode.getElementsByTagName(tagName);
		idElement = (Element) idElementList.item(0);
		if(null == attrName)
			valueNode = getChildNode(idElement);
		else
			valueNode = getValueFromAttribute(idElement, attrName);
			
		return valueNode.getNodeValue();
	}
	
	private Node getValueFromAttribute(Element idElement, String attrName) {
		return (Node)idElement.getAttributeNode(attrName);
	}

	private Node getChildNode(Node idElement) {
		return (Node)idElement.getChildNodes().item(0);
	}

	private Document getDocument() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			return db.parse(feedStream);
		} catch (ParserConfigurationException e) {
			Log.i("Trying to create document builder", e.toString());
		} catch (SAXException e) {
			Log.i("Trying to parse feedStream into document", e.toString());
		} catch (IOException e) {
			Log.i("Trying to parse feedStream into document", e.toString());
		}
		return null;
	}

	private NodeList getEntryNodeList(Document document) {
		if(document == null) return new NodeList() {
			
			@Override
			public Node item(int index) {
				return null;
			}
			
			@Override
			public int getLength() {
				return 0;
			}
		};
		
		return document.getElementsByTagName("entry");
	}
	
}
