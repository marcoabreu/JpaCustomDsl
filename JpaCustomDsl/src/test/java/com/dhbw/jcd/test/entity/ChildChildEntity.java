package com.dhbw.jcd.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "ChildChildEntity")
public class ChildChildEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TRACK_ID")
	private long id;
	

	@Column (name= "trackName")
	private String trackName;
	
	@Column (name= "lengthOfTrack")
	private int lengthOfTrack;
	
	@ManyToOne()
	@JoinColumn(name = "ALBUM_ID", referencedColumnName = "ALBUM_ID")
	private ChildEntity album;
	
	public ChildChildEntity(String trackName, int lengthOfTrack) {
		this.trackName = trackName;
		this.lengthOfTrack = lengthOfTrack;
	}

	public ChildEntity getAlbum() {
		return album;
	}

	public void setAlbum(ChildEntity album) {
		this.album = album;
	}
	
	
	

}
