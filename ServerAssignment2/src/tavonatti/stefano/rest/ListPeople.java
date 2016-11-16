package tavonatti.stefano.rest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import tavonatti.stefano.model.People;
import tavonatti.stefano.model.Person;
import tavonatti.stefano.utilities.MarshallingUtilities;

@Path("/")
public class ListPeople {

	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listPerson() {		
		
		String result="";
    	
    	People p=new People();
		p.setPerson(Person.getAll());
		
	
		try {
			result=MarshallingUtilities.marshallJSONTOString(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
        return result;
    }
	
	
    @GET
    //@Produces(MediaType.TEXT_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String sayHelloXML() {
    	
    	String result="";
    	
    	People p=new People();
		p.setPerson(Person.getAll());
		
		try {
			result=MarshallingUtilities.marshallXMLToString(People.class, p);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return result;
    }
}
