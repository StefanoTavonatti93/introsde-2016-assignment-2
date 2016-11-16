package tavonatti.stefano.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="measure")
public class Measure {
	
	@Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(strategy=GenerationType.AUTO) 
    @Column(name="mid") // maps the following attribute to a column
    private int mid;
	
	@Column(name="value")
	private double value;
	
	//@Column(name="measuretype")
	//private String measureType;
	
	@ManyToOne(cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
	private MeasureDefinition type;

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

	public MeasureDefinition getType() {
		return type;
	}

	public void setType(MeasureDefinition type) {
		this.type = type;
	}

}
