package com.team1.spreet.domain.event.model;

import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.common.model.TimeStamped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EventComment extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVENT_COMMENT_ID")
	private Long id;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_ID")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	public EventComment(String content, Event event, User user) {
		this.content = content;
		this.event = event;
		this.user = user;
	}

	public void updateEventComment(String content) {
		this.content = content;
	}

	public void isDeleted() {
		this.deleted = true;
	}
}
