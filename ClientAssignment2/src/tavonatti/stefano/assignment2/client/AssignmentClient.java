package tavonatti.stefano.assignment2.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class AssignmentClient {
	
	public static void main(String args[]){
		ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget service = client.target(getBaseURI());
		System.out.println(service.path("person").request().accept(MediaType.APPLICATION_XML).get().readEntity(String.class));
	}
	
	private static URI getBaseURI() {
        return UriBuilder.fromUri(
                "http://localhost:8080/ServerAssignment2").build();
    }

}
