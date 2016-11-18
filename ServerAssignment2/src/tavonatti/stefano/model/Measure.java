package tavonatti.stefano.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="measure")
@XmlRootElement(name="measure")
public class Measure {
	
	@Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(strategy=GenerationType.AUTO) 
    @Column(name="mid") // maps the following attribute to a column
    private int mid;
	
	@Column(name="value")
	private double value;
	
	//@Column(name="measuretype")
	//private String measureType;
	
	//@ManyToOne(cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
	@Column(name="TYPE")
	private String measureType;
	
	@Temporal(TemporalType.TIMESTAMP) // defines the precision of the date attribute
    @Column(name="created")
    private Date created;

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}


	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}


}
