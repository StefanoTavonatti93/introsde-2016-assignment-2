package tavonatti.stefano.model;

import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="measuredefinition")
public class MeasureDefinition {
	
	@Id
	private String type;

	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@XmlTransient
	private List<Measure> measure;
}
