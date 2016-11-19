package tavonatti.stefano.model.variants;

import javax.xml.bind.annotation.XmlRootElement;

import tavonatti.stefano.utilities.MeasureTypes;

@XmlRootElement(name="measureType")
/**
 * This calass is used to build the Health profile dinamically
 * 
 * @author stefano
 *
 */
public class MeasureType {
	
	private String measure;
	private double value;
	
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * Create a new MeasureType object with the given data
	 * @param type
	 * @param value
	 * @return
	 */
	public static MeasureType createMeasure(MeasureTypes type,double value){
		MeasureType m=new MeasureType();
		m.setMeasure(type.toString());
		m.setValue(value);
		return m;
	}

}
