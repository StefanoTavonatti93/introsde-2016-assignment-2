package tavonatti.stefano.rest;


import java.io.IOException;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import tavonatti.stefano.model.People;
import tavonatti.stefano.model.Person;
import tavonatti.stefano.utilities.MarshallingUtilities;

@Path("/")
public class ListPeople {

	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listPeople() {		
		
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
    @Produces(MediaType.APPLICATION_XML)
    public String ListPeople() {
    	
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
    
    @GET
    @Path("/{personId}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    public Person getPersonXML(@PathParam("personId") int id) {
    	
    	//get person by the given id
		Person p= Person.getPersonById(id);
		
		
		return p;
	}
    
    @GET
    @Path("/{personId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Person getPersonJSON(@PathParam("personId") int id) {
    	
    	//get person by the given id
		Person p= Person.getPersonById(id);
		
		return p;
	
	}
    
    @PUT
    @Path("/{personId}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Person updatePerson(@PathParam("personId") int id, Person p) {
    	p.setIdPerson(id);
    	Person.updatePerson(p);
    	return Person.getPersonById(id);
    }
}
