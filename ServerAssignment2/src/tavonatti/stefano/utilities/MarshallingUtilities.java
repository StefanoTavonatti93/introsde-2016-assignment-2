package tavonatti.stefano.utilities;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


public class MarshallingUtilities {
	
	public static String marshallXMLToString(Class c,Object o) throws JAXBException{
		
		JAXBContext jc=JAXBContext.newInstance(c);
		Marshaller marshaller=jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw=new StringWriter();
		
		marshaller.marshal(o, sw);
		return sw.toString();
	}

}
