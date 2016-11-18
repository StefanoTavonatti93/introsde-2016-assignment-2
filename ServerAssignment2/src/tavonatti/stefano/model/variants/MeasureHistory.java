package tavonatti.stefano.model.variants;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import tavonatti.stefano.model.Measure;

@XmlRootElement(name="measureHistory")
/**
 * this calss is used by the call that asks for the measure history in order to display the elements in the right way
 * @author stefano
 *
 */
public class MeasureHistory {

	private  List<Measure> measure;

	public List<Measure> getMeasure() {
		return measure;
	}

	public void setMeasure(List<Measure> measure) {
		this.measure = measure;
	}
}
