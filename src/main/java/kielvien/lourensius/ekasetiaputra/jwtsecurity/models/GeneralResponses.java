package kielvien.lourensius.ekasetiaputra.jwtsecurity.models;

public class GeneralResponses<T> {
	private String statusCode;
	private String desc;
	private T data;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GeneralResponses [statusCode=" + statusCode + ", desc=" + desc + ", data=" + data + "]";
	}
}
