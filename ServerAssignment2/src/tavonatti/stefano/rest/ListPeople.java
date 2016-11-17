package tavonatti.stefano.rest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import tavonatti.stefano.model.HealthProfile;
import tavonatti.stefano.model.Measure;
import tavonatti.stefano.model.People;
import tavonatti.stefano.model.Person;
import tavonatti.stefano.utilities.MarshallingUtilities;
import tavonatti.stefano.utilities.MeasureType;
import tavonatti.stefano.utilities.ResultRet;

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
    
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Person createPerson(Person p) {
    	p.getHealthProfile().setMeasureList(new ArrayList<Measure>());
    	//TODO measure
    	if(p.getHealthProfile()!=null){
    		double h=p.getHealthProfile().rawHeight();
    		double w=p.getHealthProfile().rawWeight();
    		
    		if(h!=0)
    			p=saveMeasure(p, MeasureType.height, h);//if the healtrpofile exists Save the height given by the user in the post request
    		if(w!=0)
    			p=saveMeasure(p, MeasureType.weight, w);//save the weight of the person
    	}
    	Person p2=Person.savePerson(p);
    	return p2;
    }
    
    /**
     * save a new measure inside the healthprofile of the given person
     * @param p
     */
    private Person saveMeasure(Person p,MeasureType type, double value){
    	if(p.getHealthProfile()==null)
    		p.setHealthProfile(new HealthProfile());
    	
    	Measure m=new Measure();
    	m.setType(type.toString());
    	m.setValue(value);
    	Date d=new Date();
    	d.setTime(System.currentTimeMillis());
    	m.setCreated(d);
    	
    	if(p.getHealthProfile().getMeasureList()==null)
    		p.getHealthProfile().setMeasureList(new ArrayList<Measure>());
    	
    	p.getHealthProfile().getMeasureList().add(m);
    	
    	return p;
    }
    
    @DELETE
    @Path("/{personId}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public ResultRet deletePerson(@PathParam("personId") int id) {
    	
    	//get person by the given id
		Person p= Person.getPersonById(id);
		Person.removePerson(p);	
		
		return ResultRet.valueOK();
	
	}
    
    
}
