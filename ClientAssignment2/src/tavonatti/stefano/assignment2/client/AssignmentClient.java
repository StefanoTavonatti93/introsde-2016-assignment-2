package tavonatti.stefano.assignment2.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import tavonatti.stefano.model.HealthProfile;
import tavonatti.stefano.model.Measure;
import tavonatti.stefano.model.People;
import tavonatti.stefano.model.Person;
import tavonatti.stefano.model.variants.MeasureHistory;
import tavonatti.stefano.model.variants.MeasureType;
import tavonatti.stefano.model.variants.MeasureTypeList;
import tavonatti.stefano.utilities.MarshallingUtilities;
import tavonatti.stefano.utilities.MeasureTypes;

public class AssignmentClient {
	
	private ClientConfig clientConfig ;
	private Client client;
	private WebTarget service;
	private DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	private static String url_base="https://ste-introsde-assignment-2.herokuapp.com";
	private PrintWriter outJSON=null;
	private PrintWriter outXML=null;;
	
	public AssignmentClient(String args[]){
		
		/*the server url is in the first argument
		 * if the argument does not exists the default adress will be used*/
		if(args.length>0){
        	url_base=args[0];
        }
		
		System.out.println("SERVER URL: "+url_base+"\n\n");
		clientConfig = new ClientConfig();
        client = ClientBuilder.newClient(clientConfig);
        service = client.target(getBaseURI());
		
        
        /*populate db before launching the test, heroku reset the DB every time the web application restart*/
        popolateDB();
        
        /*inizializa outputFile*/
        
        try {
			outXML=new PrintWriter(new FileOutputStream(new File("client-server-xml.log")));
			outJSON=new PrintWriter(new FileOutputStream(new File("client-server-json.log")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        /*xml tests*/
        xmlRequests();
        /*json test*/
        jsonRequests();
        
        /*close the files*/
        if(outXML!=null){
	        outXML.flush();
	        outXML.close();
        }
        
        if(outJSON!=null){
	        outJSON.flush();
	        outJSON.close();
        }
	
	}
	private void jsonRequests() {
		/*R3.1*/
        Response peopleResponse=makeRequest("person", MediaType.APPLICATION_JSON);//make call
        People people=peopleResponse.readEntity(People.class);//parse response
        printResponseStatusJSON("R1 GET /person",people.getPerson().size()>2?"OK":"ERROR", peopleResponse,people);//print status
        
        /*R3.2*/
        int id=people.getPerson().size()>0?people.getPerson().get(0).getIdPerson():0;//id of the first person
        Response person1R=makeRequest("person/"+id, MediaType.APPLICATION_JSON);
        Person person1=null;
        String res="ERROR";
        if(person1R.getStatus()==200 || person1R.getStatus()==202)
        {
        	person1=person1R.readEntity(Person.class);
        	res="OK";
        }
        printResponseStatusJSON("R2 GET "+"person/"+id, "OK" , person1R, person1);
        
        /*R3.3*/
        String name="roberto";
        person1.setFirstname(name);
        Response editName=service.path("person/"+id).request().accept(MediaType.APPLICATION_JSON).put(Entity.entity(person1, MediaType.APPLICATION_JSON));
        printResponseStatusJSON("R3 PUT "+"/person/"+id, person1.getFirstname().equals(name)?"OK":"ERROR", editName, editName.readEntity(Person.class));
        
        /*R3.4*/
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
        
        /*save chuck norris*/
        Response norrisResponse=service.path("/person").request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(norris, MediaType.APPLICATION_JSON_TYPE));
        int idNorris=0;
        
        if(norrisResponse.getStatus()==200 || norrisResponse.getStatus()==201 || norrisResponse.getStatus()==202){
        	norrisResult=norrisResponse.readEntity(Person.class);
        	idNorris=norrisResult.getIdPerson();
        }
        
        printResponseStatusJSON("R4 POST /person", norrisResult!=null?"OK":"ERROR", norrisResponse, norrisResult);
        
        /*R3.5*/
        /*delete chuck norris*/
        Response deleteNorris=service.path("person/"+idNorris).request().accept(MediaType.APPLICATION_JSON).delete();
        printResponseStatusJSON("R5 DELETE /person/"+idNorris, deleteNorris.getStatus()==200?"OK":"ERROR", deleteNorris, null);
        /*check if chuck norris has been deleted*/
        Response checkNorris=makeRequest("person/"+idNorris, MediaType.APPLICATION_JSON);
        printResponseStatusJSON("R1 GET /person/"+idNorris, checkNorris.getStatus()==404?"OK":"ERROR", checkNorris, null);
        
        /*R3.6*/
        
        Response measureTypeResponse=makeRequest("measureTypes", MediaType.APPLICATION_JSON);
        MeasureTypeList mlist=null;
        
        List<String> measureTypes=new ArrayList<>();
        if(measureTypeResponse.getStatus()==200||measureTypeResponse.getStatus()==202){
        	mlist=measureTypeResponse.readEntity(MeasureTypeList.class);
        	measureTypes=mlist.getMeasureType();//save the measure array
        }
        
        printResponseStatusJSON("R9 GET /measureTypes", measureTypes.size()>2?"OK":"ERROR", measureTypeResponse, mlist);
        
        /*R3.7*/
        
        Iterator<String> it=measureTypes.iterator();
        
        boolean measureError=true;//false if at least one measureType has at least one measure
        String storedMeasureType="";
        int storedMid=0;
        
        Response rep=null;
        
        MeasureHistory measureHistory=null;
        
        /*make a request for every type*/
        while(it.hasNext()){
        	String type=it.next();
        	rep=makeRequest("person/"+id+"/"+type, MediaType.APPLICATION_JSON);
        	measureHistory=rep.readEntity(MeasureHistory.class);
        	if(measureHistory!=null){
        		if(measureHistory.getMeasure()!=null)
	        		if(measureHistory.getMeasure().size()>=1){
	        			measureError=false;
	        			storedMeasureType=type;
	        			storedMid=measureHistory.getMeasure().get(0).getMid();
	        		}
        	}
       
        }
        
        printResponseStatusJSON("R6 GET /person/{id}/{measureType}", measureError?"ERROR":"OK", rep, null);
        
        /*R3.8*/
        Response measureResponse=makeRequest("person/"+id+"/"+storedMeasureType+"/"+storedMid, MediaType.APPLICATION_JSON);
        printResponseStatusJSON("R7 person/"+id+"/"+storedMeasureType+"/"+storedMid, measureResponse.getStatus()==200?"OK":"ERROR", measureResponse, measureResponse.readEntity(Measure.class));
        
        /*R3.9*/
        //Reading the measure history of first person
        measureResponse=makeRequest("person/"+id+"/"+storedMeasureType,MediaType.APPLICATION_JSON);
        MeasureHistory storedMeasureHistory=measureResponse.readEntity(MeasureHistory.class);
        
        printResponseStatusJSON("R6 GET person/"+id+"/"+storedMeasureType, "OK", measureResponse, storedMeasureHistory);
        
        
        Measure measure=new Measure();
        measure.setValue(72);
        measure.setMeasureType(storedMeasureType);
        try {
			measure.setCreated(format.parse("2011-12-09"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        //adding new measure
        Response adding=service.path("person/"+id+"/"+storedMeasureType).request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(measure, MediaType.APPLICATION_JSON));
        
        Measure storedMeasure=adding.readEntity(Measure.class);
        
        printResponseStatusJSON("R8 POST person/"+id+"/"+storedMeasureType, "OK", adding,storedMeasure );
        Response checkNewMeasure=makeRequest("person/"+id+"/"+storedMeasureType, MediaType.APPLICATION_JSON);
        
        //check if the number of measure is increased
        MeasureHistory newMeasureHistory=checkNewMeasure.readEntity(MeasureHistory.class);
        printResponseStatusJSON("R6 GET person/"+id+"/"+storedMeasureType, newMeasureHistory.getMeasure().size()>storedMeasureHistory.getMeasure().size()?"OK":"ERROR", checkNewMeasure, newMeasureHistory);
        
        /*R3.10*/
        Measure updatedMeasure=new Measure();
        updatedMeasure.setValue(90);
        Response putMeasure=service.path("person/"+id+"/"+storedMeasureType+"/"+storedMeasure.getMid()).request().put(Entity.entity(updatedMeasure, MediaType.APPLICATION_JSON));
        Measure newMeasure=putMeasure.readEntity(Measure.class);
        
        printResponseStatusJSON("R10 PUT "+"person/"+id+"/"+storedMeasureType+"/"+storedMeasure.getMid(), newMeasure.getValue()==90?"OK":"ERROR", putMeasure, newMeasure);
        
        /*R3.11*/
        
        
        Response getByDate=makeRequest("person/"+id+"/"+storedMeasureType+"?before=2016-11-23&after=2016-10-10", MediaType.APPLICATION_JSON);
        MeasureHistory req11=getByDate.readEntity(MeasureHistory.class);
        if(req11!=null){
		    if(req11.getMeasure()==null){
		    	req11.setMeasure(new ArrayList<Measure>());
		    }
		}
	    else{
	    	req11=new MeasureHistory();
	    	req11.setMeasure(new ArrayList<Measure>());
	    }
        printResponseStatusJSON("R11 "+"person/"+id+"/"+storedMeasureType+"?before=2016-11-23&after=2016-10-10", req11.getMeasure().size()>1?"OK":"ERROR", getByDate, req11);
	
        /*R3.12*/
        Response getMinMax=service.path("person").queryParam("min", "2").queryParam("max", "90").queryParam("measureType", storedMeasure).request().get();//makeRequest("person?measureType="+storedMeasureType+"&min=2&max=90", MediaType.APPLICATION_JSON);
        System.out.println(""+getMinMax.getStatus()+" person?measureType="+storedMeasureType+"&min=2&max=90");
        People req12=getMinMax.readEntity(People.class);
        if(req12.getPerson()==null){
        	req12.setPerson(new ArrayList<Person>());
        }
        printResponseStatusJSON("R12 "+"person?measureType="+storedMeasureType+"&min=2&max=90", req12.getPerson().size()>1?"OK":"ERROR", getMinMax, req12);
	}

	private void xmlRequests() {
		/*R3.1*/
        Response peopleResponse=makeRequest("person", MediaType.APPLICATION_XML);//make call
        People people=peopleResponse.readEntity(People.class);//parse response
        printResponseStatusXML("R1 GET /person",people.getPerson().size()>2?"OK":"ERROR", peopleResponse,people);//print status
        
        /*R3.2*/
        int id=people.getPerson().size()>0?people.getPerson().get(0).getIdPerson():0;//id of the first person
        Response person1R=makeRequest("person/"+id, MediaType.APPLICATION_XML);
        Person person1=null;
        String res="ERROR";
        if(person1R.getStatus()==200 || person1R.getStatus()==202)
        {
        	person1=person1R.readEntity(Person.class);
        	res="OK";
        }
        printResponseStatusXML("R2 GET "+"person/"+id, "OK" , person1R, person1);
        
        /*R3.3*/
        String name="roberto";
        person1.setFirstname(name);
        Response editName=service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).put(Entity.entity(person1, MediaType.APPLICATION_XML));
        printResponseStatusXML("R3 PUT "+"/person/"+id, person1.getFirstname().equals(name)?"OK":"ERROR", editName, editName.readEntity(Person.class));
        
        /*R3.4*/
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
        
        /*save chuck norris*/
        Response norrisResponse=service.path("/person").request().accept(MediaType.APPLICATION_XML).post(Entity.entity(norris, MediaType.APPLICATION_XML_TYPE));
        int idNorris=0;
        
        if(norrisResponse.getStatus()==200 || norrisResponse.getStatus()==201 || norrisResponse.getStatus()==202){
        	norrisResult=norrisResponse.readEntity(Person.class);
        	idNorris=norrisResult.getIdPerson();
        }
        
        printResponseStatusXML("R4 POST /person", norrisResult!=null?"OK":"ERROR", norrisResponse, norrisResult);
        
        /*R3.5*/
        /*delete chuck norris*/
        Response deleteNorris=service.path("person/"+idNorris).request().accept(MediaType.APPLICATION_XML).delete();
        printResponseStatusXML("R5 DELETE /person/"+idNorris, deleteNorris.getStatus()==200?"OK":"ERROR", deleteNorris, null);
        /*check if chuck norris has been deleted*/
        Response checkNorris=makeRequest("person/"+idNorris, MediaType.APPLICATION_XML);
        printResponseStatusXML("R1 GET /person/"+idNorris, checkNorris.getStatus()==404?"OK":"ERROR", checkNorris, null);
        
        /*R3.6*/
        
        Response measureTypeResponse=makeRequest("measureTypes", MediaType.APPLICATION_XML);
        MeasureTypeList mlist=null;
        
        List<String> measureTypes=new ArrayList<>();
        if(measureTypeResponse.getStatus()==200||measureTypeResponse.getStatus()==202){
        	mlist=measureTypeResponse.readEntity(MeasureTypeList.class);
        	measureTypes=mlist.getMeasureType();//save the measure array
        }
        
        printResponseStatusXML("R9 GET /measureTypes", measureTypes.size()>2?"OK":"ERROR", measureTypeResponse, mlist);
        
        /*R3.7*/
        
        Iterator<String> it=measureTypes.iterator();
        
        boolean measureError=true;//false if at least one measureType has at least one measure
        String storedMeasureType="";
        int storedMid=0;
        
        Response rep=null;
        
        MeasureHistory measureHistory=null;
        
        /*make a request for every type*/
        while(it.hasNext()){
        	String type=it.next();
        	rep=makeRequest("person/"+id+"/"+type, MediaType.APPLICATION_XML);
        	measureHistory=rep.readEntity(MeasureHistory.class);
        	if(measureHistory!=null){
        		if(measureHistory.getMeasure()!=null)
	        		if(measureHistory.getMeasure().size()>=1){
	        			measureError=false;
	        			storedMeasureType=type;
	        			storedMid=measureHistory.getMeasure().get(0).getMid();
	        		}
        	}
       
        }
        
        printResponseStatusXML("R6 GET GET /person/{id}/{measureType}", measureError?"ERROR":"OK", rep, null);
        
        /*R3.8*/
        Response measureResponse=makeRequest("person/"+id+"/"+storedMeasureType+"/"+storedMid, MediaType.APPLICATION_XML);
        printResponseStatusXML("R7 person/"+id+"/"+storedMeasureType+"/"+storedMid, measureResponse.getStatus()==200?"OK":"ERROR", measureResponse, measureResponse.readEntity(Measure.class));
        
        /*R3.9*/
        //Reading the measure history of first person
        measureResponse=makeRequest("person/"+id+"/"+storedMeasureType,MediaType.APPLICATION_XML);
        MeasureHistory storedMeasureHistory=measureResponse.readEntity(MeasureHistory.class);
        
        printResponseStatusXML("R6 GET person/"+id+"/"+storedMeasureType, "OK", measureResponse, storedMeasureHistory);
        
        
        Measure measure=new Measure();
        measure.setValue(72);
        measure.setMeasureType(storedMeasureType);
        try {
			measure.setCreated(format.parse("2011-12-09"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        //adding new measure
        Response adding=service.path("person/"+id+"/"+storedMeasureType).request().accept(MediaType.APPLICATION_XML).post(Entity.entity(measure, MediaType.APPLICATION_XML));
        
        Measure storedMeasure=adding.readEntity(Measure.class);
        
        printResponseStatusXML("R8 POST person/"+id+"/"+storedMeasureType, "OK", adding,storedMeasure );
        Response checkNewMeasure=makeRequest("person/"+id+"/"+storedMeasureType, MediaType.APPLICATION_XML);
        
        //check if the number of measure is increased
        MeasureHistory newMeasureHistory=checkNewMeasure.readEntity(MeasureHistory.class);
        printResponseStatusXML("R6 GET person/"+id+"/"+storedMeasureType, newMeasureHistory.getMeasure().size()>storedMeasureHistory.getMeasure().size()?"OK":"ERROR", checkNewMeasure, newMeasureHistory);
        
        /*R3.10*/
        Measure updatedMeasure=new Measure();
        updatedMeasure.setValue(90);
        Response putMeasure=service.path("person/"+id+"/"+storedMeasureType+"/"+storedMeasure.getMid()).request().put(Entity.entity(updatedMeasure, MediaType.APPLICATION_XML));
        Measure newMeasure=putMeasure.readEntity(Measure.class);
        
        printResponseStatusXML("R10 PUT "+"person/"+id+"/"+storedMeasureType+"/"+storedMeasure.getMid(), newMeasure.getValue()==90?"OK":"ERROR", putMeasure, newMeasure);
        
        /*R3.11*/
        
        
        Response getByDate=makeRequest("person/"+id+"/"+storedMeasureType+"?before=2016-11-23&after=2016-10-10", MediaType.APPLICATION_XML);
        MeasureHistory req11=getByDate.readEntity(MeasureHistory.class);
        if(req11!=null){
	        if(req11.getMeasure()==null){
	        	req11.setMeasure(new ArrayList<Measure>());
	        }
        }
        else{
        	req11=new MeasureHistory();
        	req11.setMeasure(new ArrayList<Measure>());
        }
        printResponseStatusXML("R11 "+"person/"+id+"/"+storedMeasureType+"?before=2016-11-23&after=2016-10-10", req11.getMeasure().size()>1?"OK":"ERROR", getByDate, req11);
	
        /*R3.12*/
        Response getMinMax=service.path("person").queryParam("min", "2").queryParam("max", "90").queryParam("measureType", storedMeasure).request().get();//makeRequest("person?measureType="+storedMeasureType+"&min=2&max=90", MediaType.APPLICATION_XML);
        System.out.println(""+getMinMax.getStatus()+" person?measureType="+storedMeasureType+"&min=2&max=90");
        People req12=getMinMax.readEntity(People.class);
        if(req12.getPerson()==null){
        	req12.setPerson(new ArrayList<Person>());
        }
        printResponseStatusXML("R12 "+"person?measureType="+storedMeasureType+"&min=2&max=90", req12.getPerson().size()>1?"OK":"ERROR", getMinMax, req12);
	}
	
	public void popolateDB(){
		
		/*Create a new person*/
		makeRequest("person/", MediaType.APPLICATION_XML);
		
		Person p=new Person();
		p.setFirstname("Paolo");
		p.setLastname("Bitta");
		HealthProfile hp=new HealthProfile();
		hp.setHeight(150);
		hp.setWeight(59);
		Measure m=new Measure();
		m.setMeasureType(MeasureTypes.weight.toString());
		m.setValue(59);
		try {
			p.setBirthdate(format.parse("1987-10-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.setHealthProfile(hp);
		
		/*POST request for saving the person*/
		Response r=service.path("/person").request().accept(MediaType.APPLICATION_XML).post(Entity.entity(p, MediaType.APPLICATION_XML_TYPE));
		Person bitta=r.readEntity(Person.class);
		Response r2=service.path("/person/"+bitta.getIdPerson()+"/weight").request().accept(MediaType.APPLICATION_XML).post(Entity.entity(m, MediaType.APPLICATION_XML_TYPE));
		
		/*Create some other Person object and save them*/
		p=new Person();
		p.setFirstname("Donato");
		p.setLastname("Cavallo");
		hp=new HealthProfile();
		
		try {
			p.setBirthdate(format.parse("1987-10-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.setHealthProfile(hp);
		service.path("/person").request().accept(MediaType.APPLICATION_XML).post(Entity.entity(p, MediaType.APPLICATION_XML_TYPE));
		
		
		p=new Person();
		p.setFirstname("Franco");
		p.setLastname("Bollo");
		hp=new HealthProfile();
		hp.setHeight(150);
		hp.setWeight(59);
		try {
			p.setBirthdate(format.parse("1987-10-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.setHealthProfile(hp);
		
		service.path("/person").request().accept(MediaType.APPLICATION_XML).post(Entity.entity(p, MediaType.APPLICATION_XML_TYPE));
	}
	
	public static void main(String args[]){
		new AssignmentClient(args);
	}
	
	private static URI getBaseURI() {
        return UriBuilder.fromUri(
                url_base).build();
    }
	
	private void printResponseStatusXML(String reqNumber,String result,Response response,Object body){
		/*log on console*/
		System.out.println("Request: #"+reqNumber+" Accept: "+MediaType.APPLICATION_XML+" Content-Type: "+MediaType.APPLICATION_XML);
		System.out.println("=> Result: "+result);
		System.out.println("=> HTTP STATUS: "+response.getStatus());
		
		if(body!=null)
			try {
				System.out.println(MarshallingUtilities.marshallXMLToString(body.getClass(), body));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		/*log into the file*/ 
		
		if(outXML==null)
			return;
		
		outXML.println("Request: #"+reqNumber+" Accept: "+MediaType.APPLICATION_XML+" Content-Type: "+MediaType.APPLICATION_XML);
		outXML.println("=> Result: "+result);
		outXML.println("=> HTTP STATUS: "+response.getStatus());
		
		if(body!=null)
			try {
				outXML.println(MarshallingUtilities.marshallXMLToString(body.getClass(), body));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void printResponseStatusJSON(String reqNumber,String result,Response response,Object body){
		System.out.println("Request: #"+reqNumber+" Accept: "+MediaType.APPLICATION_JSON+" Content-Type: "+MediaType.APPLICATION_JSON);
		System.out.println("=> Result: "+result);
		System.out.println("=> HTTP STATUS: "+response.getStatus());
		
		if(body!=null)
			try {
				System.out.println(MarshallingUtilities.marshallJSONTOString(body));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		/*log into the file*/
		if(outJSON==null)
			return ;
		
		outJSON.println("Request: #"+reqNumber+" Accept: "+MediaType.APPLICATION_JSON+" Content-Type: "+MediaType.APPLICATION_JSON);
		outJSON.println("=> Result: "+result);
		outJSON.println("=> HTTP STATUS: "+response.getStatus());
		
		if(body!=null)
			try {
				outJSON.println(MarshallingUtilities.marshallJSONTOString(body));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private Response makeRequest(String url,String type){
		
		Response r=service.path(url).request().accept(type).get();
		return r;
		
	}
		
	

}
