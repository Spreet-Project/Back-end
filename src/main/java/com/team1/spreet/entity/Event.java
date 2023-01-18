package com.team1.spreet.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Event extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVENT_ID")
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private String date;

	@Column(nullable = false)
	private String eventImageUrl;

	@Column(nullable = false)
	private boolean isDeleted = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EventComment> eventCommentList = new ArrayList<>();

	public Event(String title, String content, String location, String date, String eventImageUrl, User user) {
		this.title = title;
		this.content = content;
		this.location = location;
		this.date = date;
		this.eventImageUrl = eventImageUrl;
		this.user = user;
	}

	public void update(String title, String content, String location, String date, String eventImageUrl) {
		this.title = title;
		this.content = content;
		this.location = location;
		this.date = date;
		this.eventImageUrl = eventImageUrl;
	}
}