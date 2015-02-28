package com.nick.sanz;



public class Gig {
	private Long id;
	private String description;
	private String place;
	private String date;
	
	public Gig() {
	}


	public Gig(String description, String place, String date) {
		super();
		this.description = description;
		this.place = place;
		this.date = date;
	}

	@Override
	public String toString() {
		return "Gig [id=" + id + ", description=" + description + ", place=" + place + ", date=" + date + "]";
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
