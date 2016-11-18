package tavonatti.stefano.rest;

import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jersey.repackaged.com.google.common.cache.Weigher;
import tavonatti.stefano.model.HealthProfile;
import tavonatti.stefano.model.Measure;
import tavonatti.stefano.model.Person;
import tavonatti.stefano.utilities.MeasureType;

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
    	/*MeasureDefinition md=new MeasureDefinition();
    	md.setType("WEIGHT");*/
    	m.setMeasureType("WEIGHT");
    	m.setCreated(new Date(System.currentTimeMillis()));
    	
    	ArrayList<Measure> a=new ArrayList<>();
    	a.add(m);
    	
    	m=new Measure();
    	m.setMeasureType(MeasureType.weight.toString());
    	m.setCreated(new Date(System.currentTimeMillis()+86400000));
    	m.setValue(5);
    	
    	a.add(m);
    	
    	hp.setMeasureList(a);
    	p.setHealthProfile(hp);
    	
    	Person.savePerson(p);
    	
    	
        return "OK";
    }
}
