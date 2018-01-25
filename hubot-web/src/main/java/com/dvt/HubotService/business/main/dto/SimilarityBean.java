package com.dvt.HubotService.business.main.dto;

public class SimilarityBean implements Comparable<SimilarityBean>{
	private float similarity;
	private String key;
	public float getSimilarity() {
		return similarity;
	}
	public void setSimilarity(float similarity) {
		this.similarity = similarity;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
	
	public SimilarityBean() {
		super();
	}
	public SimilarityBean(float similarity, String key) {
		super();
		this.similarity = similarity;
		this.key = key;
	}
	@Override
	public int compareTo(SimilarityBean o) {
		// TODO Auto-generated method stub
		return Math.round((o.similarity - this.similarity)*100);
	}
	
	
}
