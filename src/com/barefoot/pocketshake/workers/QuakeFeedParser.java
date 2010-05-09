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
			String id = null;
			String title = null;
			String cordinates = null;
			String dateTime = null;
			
			for (int size = 0; size < nodeLst.getLength(); size++) {
				currentNode = nodeLst.item(size);
			    
			    if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
			    	id = getNodeValueWithTagNameAs(currentNode, "id");
			    	title = getNodeValueWithTagNameAs(currentNode, "title");
			    	cordinates = getNodeValueWithTagNameAs(currentNode, "georss:point");
			    	dateTime = getNodeValueWithTagNameAs(currentNode, "updated");
			    	try {
			    		parsedObjectList.add(new EarthQuake(id, title, cordinates, dateTime));
			    	} catch(InvalidFeedException ife) {
			    		//just procastinate the feed sync until next time out happens
			    	}
			    }
			}
		}
		
		return parsedObjectList;
	}
	
	private String getNodeValueWithTagNameAs(Node currentNode, String tagName) {
		Node valueNode = null;
		Element entryNode = (Element) currentNode;
		NodeList idElementList = null;
		Element idElement = null;
		idElementList = entryNode.getElementsByTagName(tagName);
		idElement = (Element) idElementList.item(0);
		valueNode = (Node)idElement.getChildNodes().item(0);
		return valueNode.getNodeValue();
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
