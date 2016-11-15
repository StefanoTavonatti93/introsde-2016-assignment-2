package tavonatti.stefano.test;

import java.util.Date;

import org.junit.Test;

import tavonatti.stefano.model.Person;

public class DBTest {

	@Test
	public void insertPerson(){
		Person p=new Person();
		p.setName("Herbert");
		p.setLastName("Ballerina");
		p.setBirthdate(new Date(System.currentTimeMillis()));
		
		Person.savePerson(p);
	}
}
