package tavonatti.stefano.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import tavonatti.stefano.dao.LifeCoachDao;
import tavonatti.stefano.model.variants.MeasureType;
import tavonatti.stefano.utilities.MeasureTypes;

@Entity
@Table(name="healthprofile")
@XmlRootElement(name="healthProfile")
public class HealthProfile implements Serializable {
	
	@Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(strategy=GenerationType.AUTO) 
    @Column(name="idHealtprofile") // maps the following attribute to a column
	@XmlTransient
    private int idHealthProfile;
	
	@OneToOne(mappedBy="healthProfile", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@XmlTransient
	private Person person;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@XmlTransient
	private List<Measure> measureList;
	
	/* this class supports both the static and dynamic healthProfile, they are transient because in the database only the
	 * measure history will be saved. The health profile is build dynamically based on the measureHystory*/

	@Transient
	private double height;
	
	@Transient
	private double weight;
	
	@Transient
	private List<MeasureType> measureType;

	@XmlTransient
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

	/*calculate the height of the person based on the last height measure.*/
	public double getHeight() {
		
		if(measureList==null){
			return 0;
		}
		if(measureList.size()==0){
			return 0;
		}
		
		List<Measure> measures=new ArrayList<>(measureList);
		measures.sort(new ComaparatorMeasureDate());
		
		Iterator<Measure> it=measures.iterator();
		
		while(it.hasNext()){
			Measure m=it.next();
			if(m.getMeasureType().equals(MeasureTypes.height.toString())){
				height=m.getValue();
				break;
			}
		}
		
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	/*calculate the weight of the person based on the last height measure.*/
	public double getWeight() {
		
		if(measureList==null){
			return 0;
		}
		
		if(measureList.size()==0){
			return 0;
		}
		
		List<Measure> measures=new ArrayList<>(measureList);
		measures.sort(new ComaparatorMeasureDate());
		
		Iterator<Measure> it=measures.iterator();
		
		while(it.hasNext()){
			Measure m=it.next();
			if(m.getMeasureType().equals(MeasureTypes.weight.toString())){
				weight=m.getValue();
				break;
			}
		}
		
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
	
	/**
	 * returns the height without checking in the measure list, used to create a new person
	 * @return
	 */
	public double rawHeight(){
		return height;
	}
	/**
	 * returns the weight without checking in the measure list, used to create a new person
	 * @return
	 */
	public double rawWeight(){
		return weight;
	}

	public List<MeasureType> getMeasureType() {
		measureType=new ArrayList<MeasureType>();
		if(getMeasureList()!=null){
			ArrayList<Measure> measures=new ArrayList<>(getMeasureList());
			
			/*sort measure by date*/
			measures.sort(new ComaparatorMeasureDate());
			
			MeasureTypes mt[]=MeasureTypes.values();
			
			/* get most recent measure per type*/
			for(MeasureTypes type:mt){
				Iterator<Measure> it=measures.iterator();
				while(it.hasNext()){
					Measure m=it.next();
					if(m.getMeasureType().equals(type.toString())){
						MeasureType measure=new MeasureType();
						measure.setValue(m.getValue());
						measure.setMeasure(m.getMeasureType());
						
						measureType.add(measure);
						break;
					}
				}
			}
			
		}
		return measureType;
	}

	public void setMeasureType(List<MeasureType> measureType) {
		this.measureType = measureType;
	}

}
