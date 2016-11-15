package tavonatti.stefano.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="healthprofile")
public class HealthProfile {
	
	@Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(strategy=GenerationType.AUTO) 
    @Column(name="idHealtprofile") // maps the following attribute to a column
    private int idHealthProfile;
	
	@OneToOne
	private Person person;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Measure> measureList;

	public int getIdHealthProfile() {
		return idHealthProfile;
	}

	public void setIdHealthProfile(int idHealthProfile) {
		this.idHealthProfile = idHealthProfile;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
