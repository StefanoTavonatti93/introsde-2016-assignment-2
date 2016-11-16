package tavonatti.stefano.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import tavonatti.stefano.dao.LifeCoachDao;

@Entity
@Table(name="healthprofile")
@XmlRootElement(name="healthProfile")
public class HealthProfile implements Serializable {
	
	@Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(strategy=GenerationType.AUTO) 
    @Column(name="idHealtprofile") // maps the following attribute to a column
    private int idHealthProfile;
	
	@OneToOne(mappedBy="healthProfile", cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	@XmlTransient
	private Person person;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@XmlTransient
	private List<Measure> measureList;
	
	@Transient
	private double height;
	
	@Transient
	private double weight;

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

	@XmlTransient
	public List<Measure> getMeasureList() {
		return measureList;
	}

	public void setMeasureList(List<Measure> measureList) {
		this.measureList = measureList;
	}

	public double getHeight() {
		
		if(measureList.size()==0){
			return 0;
		}
		
		List<Measure> measures=new ArrayList<>(measureList);
		measures.sort(new ComaparatorMeasureDate());
		
		//TODO measureType
		
		height=measures.get(0).getValue();
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public static HealthProfile save(HealthProfile h) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(h);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return h;
    } 

}
