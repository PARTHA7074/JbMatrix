package com.parthacreation.jbmatrix.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VideoTabResponse {

	@SerializedName("data")
	private List<VideoItem> data;

	@SerializedName("status")
	private String status;

	public void setData(List<VideoItem> data){
		this.data = data;
	}

	public List<VideoItem> getData(){
		return data;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}