package tavonatti.stefano.assignment2.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument.Field.Xpath;
import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import tavonatti.stefano.model.HealthProfile;
import tavonatti.stefano.model.People;
import tavonatti.stefano.model.Person;
import tavonatti.stefano.utilities.MarshallingUtilities;

public class AssignmentClient {
	
	private ClientConfig clientConfig ;
	private Client client;
	private WebTarget service;
	private DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	
	public AssignmentClient(String args[]){
		clientConfig = new ClientConfig();
        client = ClientBuilder.newClient(clientConfig);
        service = client.target(getBaseURI());
		//System.out.println(service.path("person").request().accept(MediaType.APPLICATION_XML).get().readEntity(String.class));
        
        /*R1*/
        Response peopleResponse=makeRequest("person", MediaType.APPLICATION_XML);//make call
        People people=peopleResponse.readEntity(People.class);//parse response
        printResponseStatusXML("1",people.getPerson().size()>2?"OK":"ERROR", peopleResponse,people);//print status
        
        /*R2*/
        int id=people.getPerson().size()>0?people.getPerson().get(0).getIdPerson():0;
        Response person1R=makeRequest("person/"+id, MediaType.APPLICATION_XML);
        Person person1=null;
        String res="ERROR";
        if(person1R.getStatus()==200 || person1R.getStatus()==202)
        {
        	person1=person1R.readEntity(Person.class);
        	res="OK";
        }
        printResponseStatusXML("2", "OK" , person1R, person1);
        
        /*R3*/
        String name="roberto";
        person1.setFirstname(name);
        Response editName=service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).put(Entity.entity(person1, MediaType.APPLICATION_XML));
        printResponseStatusXML("3", person1.getFirstname().equals(name)?"OK":"ERROR", editName, editName.readEntity(Person.class));
        
        /*R4*/
        Person norris=new Person();
        norris.setFirstname("Chuck");
        norris.setLastname("Norris");
        
        try {
			norris.setBirthdate(format.parse("1945-01-01"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        HealthProfile hp=new HealthProfile();
        hp.setHeight(172);
        hp.setWeight(78.9);
        
        norris.setHealthProfile(hp);
        Person norrisResult=null;
        
        Response norrisResponse=service.path("/person").request().accept(MediaType.APPLICATION_XML).post(Entity.entity(norris, MediaType.APPLICATION_XML_TYPE));
        int idNorris=0;
        
        if(norrisResponse.getStatus()==200 || norrisResponse.getStatus()==201 || norrisResponse.getStatus()==202){
        	norrisResult=norrisResponse.readEntity(Person.class);
        	idNorris=norrisResult.getIdPerson();
        }
        
        printResponseStatusXML("4", norrisResult!=null?"OK":"ERROR", norrisResponse, norrisResult);
	}
	
	public static void main(String args[]){
		new AssignmentClient(args);
	}
	
	private static URI getBaseURI() {
        return UriBuilder.fromUri(
                "http://localhost:8080/ServerAssignment2").build();
    }
	
	private void printResponseStatusXML(String reqNumber,String result,Response response,Object body){
		System.out.println("Request: #"+reqNumber);
		System.out.println("=> Result: "+result);
		System.out.println("=> HTTP STATUS: "+response.getStatus());
		
		if(body!=null)
			try {
				System.out.println(MarshallingUtilities.marshallXMLToString(body.getClass(), body));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private Response makeRequest(String url,String type){
		
		Response r=service.path(url).request().accept(type).get();
		return r;
		
	}
		
	

}
