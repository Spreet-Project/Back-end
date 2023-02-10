package com.team1.spreet.domain.event.model;

import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.common.model.TimeStamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
public class Event extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVENT_ID")
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private String date;

	@Column(nullable = false)
	private String time;

	@Column(nullable = false)
	private String eventImageUrl;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AreaCode areaCode;

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EventComment> eventCommentList = new ArrayList<>();

	public Event(String title, String content, String location, String date, String time,
		String eventImageUrl, AreaCode areaCode, User user) {
		this.title = title;
		this.content = content;
		this.location = location;
		this.date = date;
		this.time = time;
		this.eventImageUrl = eventImageUrl;
		this.areaCode = areaCode;
		this.user = user;
	}

	public void update(String title, String content, String location, String date,
		String time, String eventImageUrl, AreaCode areaCode) {
		this.title = title;
		this.content = content;
		this.location = location;
		this.date = date;
		this.time = time;
		this.eventImageUrl = eventImageUrl;
		this.areaCode = areaCode;
	}

	public void isDeleted() {
		this.deleted = true;
	}
}
