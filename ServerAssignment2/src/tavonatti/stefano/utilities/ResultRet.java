package tavonatti.stefano.utilities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
public class ResultRet {
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static ResultRet valueOK(){
		ResultRet r=new ResultRet();
		r.setValue("OK");
		return r;
	}

}
