package com.dvt.HubotService.business.main.dto;

public class ReqParam {
//	//params.put("title", "你的加班申请");
//	params.put("nonce", nonceStr);
//	params.put("timestamp",timeStamp);
//	params.put("signature", signature);
//	params.put("username", "like@bjdvt.com");
//	//params.put("redirectUrl", "http://192.168.1.100:8069/web?min=1&limit=80&view_type=list&model=apply.for.overtime&action=518&menu_id=324#min=1&limit=80&view_type=list&model=apply.for.overtime&action=518&menu_id=324");
//	
	private String nonce;
	private String timestamp;
	private String signature;
	private String username;
	private String title;
	private String redirectUrl;
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public ReqParam(String nonce, String timestamp, String signature,
			String username, String title, String redirectUrl) {
		super();
		this.nonce = nonce;
		this.timestamp = timestamp;
		this.signature = signature;
		this.username = username;
		this.title = title;
		this.redirectUrl = redirectUrl;
	}
	public ReqParam() {
		super();
	}
	
	
}
