package tavonatti.stefano.rest;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import tavonatti.stefano.model.HealthProfile;
import tavonatti.stefano.model.Measure;
import tavonatti.stefano.model.MeasureDefinition;
import tavonatti.stefano.model.People;
import tavonatti.stefano.model.Person;

@Path("/test")
public class Test {
	
	@GET
    @Produces(MediaType.APPLICATION_XML)
    public String sayHelloXML() {
    	
    	Person p=new Person();
    	p.setName("Paolo");
    	p.setLastName("Bitta");
    	p.setBirthdate(new Date(System.currentTimeMillis()));
    	HealthProfile hp=new HealthProfile();
    	Measure m=new Measure();
    	m.setValue(150);
    	MeasureDefinition md=new MeasureDefinition();
    	md.setType("WEIGHT");
    	m.setType(md);
    	
    	ArrayList<Measure> a=new ArrayList<>();
    	a.add(m);
    	hp.setMeasureList(a);
    	
    	p.setHealthProfile(hp);
    	
    	Person.savePerson(p);
    	
    	
        return "OK";
    }
}
