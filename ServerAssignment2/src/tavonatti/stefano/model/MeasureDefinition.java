package tavonatti.stefano.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="measuredefinition")
public class MeasureDefinition {
	
	@Id
	private String type;

	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<Measure> measure;
}
