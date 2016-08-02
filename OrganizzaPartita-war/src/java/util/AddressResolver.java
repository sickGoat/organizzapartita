/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author antonio
 */
public class AddressResolver {
    
    private static final String GEOCODE_ADDRESS = "http://maps.google.com/maps/api/geocode/xml";
    private final String address;
    private static Document result = null;
    
    public AddressResolver(String address) {
        
        this.address = address;
    }
       
    public void resolve() throws UnsupportedEncodingException, MalformedURLException, IOException, ParserConfigurationException, SAXException{
    
        URL url = new URL(GEOCODE_ADDRESS + "?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try{
        
            connection.connect();
            InputSource input_result = new InputSource(connection.getInputStream());
            result = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input_result);
        }
        finally{
        
            connection.disconnect();
        }
    }
       
    public void getFormattedAddress() throws XPathExpressionException{
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        NodeList resultNodeList = null ;
        resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result", result, XPathConstants.NODESET);
        for (int i = 1 ; i < resultNodeList.getLength() ; i++ ){
            NodeList addr = (NodeList) xpath.evaluate("/GeocodeResponse/result["+i+"]/formatted_address", result, XPathConstants.NODESET);
            NodeList lat = (NodeList) xpath.evaluate("/GeocodeResponse/result["+i+"]/geometry/location/lat", result, XPathConstants.NODESET);
            NodeList lng = (NodeList) xpath.evaluate("/GeocodeResponse/result["+i+"]/geometry/location/lng", result, XPathConstants.NODESET);
            System.out.println("address = " + addr.item(0).getTextContent());
            System.out.println("lng = " + lng.item(0).getTextContent());
            System.out.println("lat = " + lat.item(0).getTextContent());
        }
    }
    
    public List<String> getFormattedAddresses() throws XPathExpressionException{
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        List<String> indirizzi = new ArrayList<>();
        NodeList resultNodeList = null ;
        resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result/formatted_address", result, XPathConstants.NODESET);
        for (int i = 0 ; i < resultNodeList.getLength() ; i++ ){
            
            indirizzi.add(resultNodeList.item(i).getTextContent());
        }
        
        return indirizzi;
    }
    
    public Map<String,float[]> mapAddresses() throws XPathExpressionException{
          
        Map<String,float[]> indirizzi = new HashMap<>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        NodeList resultNodeList = null ;
        resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result", result, XPathConstants.NODESET);
        for (int i = 1 ; i < resultNodeList.getLength() ; i++ ){
            NodeList addr = (NodeList) xpath.evaluate("/GeocodeResponse/result["+i+"]/formatted_address", result, XPathConstants.NODESET);
            NodeList lat = (NodeList) xpath.evaluate("/GeocodeResponse/result["+i+"]/geometry/location/lat", result, XPathConstants.NODESET);
            NodeList lng = (NodeList) xpath.evaluate("/GeocodeResponse/result["+i+"]/geometry/location/lng", result, XPathConstants.NODESET);
            indirizzi.put(addr.item(0).getTextContent(),
                    new float[]{Float.parseFloat(lat.item(0).getTextContent()) ,
                        Float.parseFloat(lng.item(0).getTextContent()) });
        }
            
        return indirizzi;
    }
    
    public float[] getCoordinate(String address) throws XPathExpressionException{
    
        float [] coordinate = new float[2];
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList resultNodeList = null ;
        NodeList lat = (NodeList) xpath.evaluate("/GeocodeResponse/result/geometry/location/lat", result, XPathConstants.NODESET);
        NodeList lng = (NodeList) xpath.evaluate("/GeocodeResponse/result/geometry/location/lng", result, XPathConstants.NODESET);
        coordinate[0] = Float.parseFloat(lat.item(0).getTextContent());
        coordinate[1] = Float.parseFloat(lng.item(0).getTextContent());
                
        return coordinate;
    }
}
