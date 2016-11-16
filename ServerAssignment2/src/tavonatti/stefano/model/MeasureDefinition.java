package tavonatti.stefano.model;

import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="measuredefinition")
public class MeasureDefinition {
	
	@Id
	private String type;

	@OneToMany(cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
	@XmlTransient
	private List<Measure> measure;

	public List<Measure> getMeasure() {
		return measure;
	}

	public void setMeasure(List<Measure> measure) {
		this.measure = measure;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
