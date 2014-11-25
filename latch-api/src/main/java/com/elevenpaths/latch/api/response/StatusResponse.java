package com.elevenpaths.latch.api.response;

/**
 * Data available at status response
 * @author jpenren
 */
public class StatusResponse extends LatchResponse {
	private static final long serialVersionUID = 1L;
	private String status;
	private String token;
	private long generated;
	
	StatusResponse() {
	}

	public boolean isOn() {
		return "on".equals(status);
	}
	
	public boolean isOff() {
		return "off".equals(status);
	}
	
	public boolean hasSecondFactor(){
		return token!=null;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getGenerated() {
		return generated;
	}

	public void setGenerated(long generated) {
		this.generated = generated;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatusResponse [status=");
		builder.append(status);
		builder.append(", token=");
		builder.append(token);
		builder.append(", generated=");
		builder.append(generated);
		builder.append("]");
		return builder.toString();
	}

}
