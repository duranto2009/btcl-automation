package common;

public class DefaulReponseJason {
	private int responseCode;
	private Object resposeObject;
	public DefaulReponseJason(int responseCode,Object responseObject){
		this.responseCode = responseCode;
		this.resposeObject = responseObject;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public Object getResposeObject() {
		return resposeObject;
	}
	public void setResposeObject(Object resposeObject) {
		this.resposeObject = resposeObject;
	}
}
