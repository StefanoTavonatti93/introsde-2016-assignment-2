package tavonatti.stefano.rest;

import java.io.BufferedOutputStream;
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
    @Produces(MediaType.TEXT_HTML)
    public String listPerson() {		
		
		List<Person> people=Person.getAll();
		
		Iterator<Person> it=people.iterator();
		
		String result="";
		
		while(it.hasNext()){
			Person p=it.next();
			result+=p.getName()+" "+p.getLastName()+"<br>";
		}
		
        return "<html> " + "<title>" + "Persons List"+"</title>"
                + "<body>" + result + "</body>"
                + "</html> ";
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
		
        return "<?xml version=\"1.0\"?>" + "<msg>" + "Hello World in REST"
                + "</msg>\n"+result;
    }
}
