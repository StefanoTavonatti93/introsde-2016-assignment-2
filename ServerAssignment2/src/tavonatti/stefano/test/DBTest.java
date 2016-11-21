package tavonatti.stefano.test;

import java.util.Date;

import org.junit.Test;

import tavonatti.stefano.model.Person;

public class DBTest {

	@Test
	public void insertPerson(){
		Person p=new Person();
		p.setFirstname("Herbert");
		p.setLastname("Ballerina");
		p.setBirthdate(new Date(System.currentTimeMillis()));
		
		Person.savePerson(p);
	}
	
	@Test
	public void getPeople(){
		System.out.println("LIST SIZE: "+Person.getAll().size());
	}
}
