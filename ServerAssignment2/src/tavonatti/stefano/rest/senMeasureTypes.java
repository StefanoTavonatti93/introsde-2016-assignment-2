package tavonatti.stefano.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import tavonatti.stefano.model.variants.MeasureTypeList;

@Path("/measureTypes")
public class senMeasureTypes {
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public MeasureTypeList getMeasureTypes(){
    	return new MeasureTypeList();
    }
	
}
