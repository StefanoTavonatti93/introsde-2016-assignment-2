package tavonatti.stefano.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * this class is used to the create the people object for the first CRUD operation
 * @author stefano
 *
 */
@XmlRootElement(name="people")
public class People implements Serializable{
	
	private List<Person> person;

	public List<Person> getPerson() {
		return person;
	}

	public void setPerson(List<Person> person) {
		this.person = person;
	}

}
