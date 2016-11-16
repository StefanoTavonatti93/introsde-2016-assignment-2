package tavonatti.stefano.utilities;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;


public class MarshallingUtilities {
	
	public static String marshallXMLToString(Class c,Object o) throws JAXBException{
		
		JAXBContext jc=JAXBContext.newInstance(c);
		Marshaller marshaller=jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw=new StringWriter();
		
		marshaller.marshal(o, sw);
		return sw.toString();
	}
	
	public static String marshallJSONTOString(Object o) throws JsonGenerationException, JsonMappingException, IOException{
		// Jackson Object Mapper 
		ObjectMapper mapper = new ObjectMapper();
		
		// Adding the Jackson Module to process JAXB annotations
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        
		// configure as necessary
		mapper.registerModule(module);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        
        StringWriter sw=new StringWriter();

        mapper.writeValue(sw, o);
        
        return sw.toString();
	}

}
