package tavonatti.stefano.rest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import tavonatti.stefano.model.HealthProfile;
import tavonatti.stefano.model.Measure;
import tavonatti.stefano.model.People;
import tavonatti.stefano.model.Person;
import tavonatti.stefano.model.variants.MeasureHistory;
import tavonatti.stefano.model.variants.MeasureType;
import tavonatti.stefano.model.variants.MeasureTypeList;
import tavonatti.stefano.utilities.MarshallingUtilities;
import tavonatti.stefano.utilities.MeasureTypes;
import tavonatti.stefano.utilities.ResultRet;

@Path("/person")
public class ListPeople {
	
	
    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public People ListPeople(@QueryParam("measureType") String measureType, @QueryParam("min") double min,@QueryParam("max") double max) {
    	
    	String result="";
    	
    	People p=new People();
		p.setPerson(Person.getAll());
		
		
		/*if the request do not contain the query parameters return all th people*/
		if(measureType!=null){
			if(measureType!=""){
				List<Person> person=p.getPerson();
				for(int i=0;i<person.size();i++){
					Person pers=person.get(i);
					List<MeasureType> mt=pers.getHealthProfile().getMeasureType();
					
					boolean remove=true;
					
					/*chek if the people measures respect the qeury*/
					for(MeasureType m:mt){
						if(m.getMeasure().equals(measureType)){
							if(m.getValue()>=min&&m.getValue()<=max){
								remove=false;
								break;
							}
						}
					}
					
					/*remove piople wich do not satisfies the query*/
					if(remove){
						person.remove(i);
					}
				}
			}
		}
		
        return p;
    }
    
   /* @GET
    @Path("/{personId}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    public Person getPersonXML(@PathParam("personId") int id) {
    	
    	//get person by the given id
		Person p= Person.getPersonById(id);
		
		
		return p;
	}*/
    
    @GET
    @Path("/{personId}")
    @Produces({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    public Response getPersonJSON(@PathParam("personId") int id) {
    	
    	//get person by the given id
		Person p= Person.getPersonById(id);
		
		if(p==null)
			return throw404();
		
		return throw200(p);
	
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
    			p=saveMeasure(p, MeasureTypes.height, h);//if the healtrpofile exists Save the height given by the user in the post request
    		if(w!=0)
    			p=saveMeasure(p, MeasureTypes.weight, w);//save the weight of the person
    	}
    	Person p2=Person.savePerson(p);
    	return p2;
    }
    
    /**
     * save a new measure inside the healthprofile of the given person
     * @param p
     */
    private Person saveMeasure(Person p,MeasureTypes type, double value){
    	if(p.getHealthProfile()==null)
    		p.setHealthProfile(new HealthProfile());
    	
    	Measure m=new Measure();
    	m.setMeasureType(type.toString());
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
    
    @GET
    @Path("/{personId}/{measureType}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public MeasureHistory getHistory(@PathParam("personId") int id,@PathParam("measureType") String measureType) {
    	List<Measure> meas=Person.getPersonById(id).getHealthProfile().getMeasureList();
    	
    	ArrayList<Measure> requestedMeasure=new ArrayList<>();
    	if(meas!=null){
    		Iterator<Measure> it=meas.iterator();
    		while(it.hasNext()){
    			Measure m=it.next();
    			if(m.getMeasureType().equals(measureType)){
    				requestedMeasure.add(m);
    			}
    		}
    	}
    		
    	
    	
    	
    	MeasureHistory mh=new MeasureHistory();
    	mh.setMeasure(requestedMeasure);
    	
    	
    	return mh;
    }
    
    
    @GET
    @Path("/{personId}/{measureType}/{mid}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response getMeasureType(@PathParam("personId") int id,@PathParam("mid") long mid, @PathParam("measureType") String measureType) {
    	List<Measure> meas=Person.getPersonById(id).getHealthProfile().getMeasureList();
    	
    	if(meas!=null){
    		Iterator<Measure> it=meas.iterator();
    		while(it.hasNext()){
    			Measure m=it.next();
    			if(m.getMid()==mid){
    				return throw200(m);
    			}
    		}
    	}
    	
    	
    	return throw404();
    }
    
    private Response throw404(){
    	return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    private Response throw200(Object o){
    	return Response.status(Response.Status.OK).entity(o).build();
    }
    
    @POST
    @Path("/{id}/{measureType}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response saveMeasure(Measure measure, @PathParam("id") int id,@PathParam("measureType") String type) {
    	Person p=Person.getPersonById(id);
    	
    	/* if the person does not exist return a 404 error*/
    	if(p==null)
    	{
    		return throw404();
    	}
    	
    	/*if the person doea not have an health progfile, create a new one*/
    	if(p.getHealthProfile()==null)
    		p.setHealthProfile(new HealthProfile());
    	
    	/*if the person do not have an array of measure, crete a new one */
    	if(p.getHealthProfile().getMeasureList()==null)
    		p.getHealthProfile().setMeasureList(new ArrayList<Measure>());
    	
    	MeasureTypes mType= MeasureTypes.valueOf(type);
    	/* if the measure type ask by the user do not exists trhow an error*/
    	if(mType==null)
    		return throw404();
    	
    	/* add the new measure */
    	p=saveMeasure(p , mType,measure.getValue());
    	
    	p=Person.updatePerson(p);
    	
    	return throw200(p);
    }
    
    
    @PUT
    @Path("/{id}/{measureType}/{mid}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response updateMeasure(Measure measure,@PathParam("mid") int mid,@PathParam("id")int id,@PathParam("measureType")String measureType){
    	//TODO checking on person
    	Measure m=Measure.getMeasureById(mid);
    	if(m==null)
    		return throw404();
    	measure.setMid(mid);
    	return throw200(Measure.updateMeasure(measure));
    }
    
    
}
