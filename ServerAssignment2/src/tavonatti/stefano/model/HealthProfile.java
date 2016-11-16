package tavonatti.stefano.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="healthprofile")
@XmlRootElement(name="healthProfile")
public class HealthProfile implements Serializable {
	
	@Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(strategy=GenerationType.AUTO) 
    @Column(name="idHealtprofile") // maps the following attribute to a column
    private int idHealthProfile;
	
	@OneToOne
	@XmlTransient
	private Person person;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Measure> measureList;

	public int getIdHealthProfile() {
		return idHealthProfile;
	}

	public void setIdHealthProfile(int idHealthProfile) {
		this.idHealthProfile = idHealthProfile;
	}

	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Measure> getMeasureList() {
		return measureList;
	}

	public void setMeasureList(List<Measure> measureList) {
		this.measureList = measureList;
	}

}
