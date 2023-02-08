package com.team1.spreet.domain.event.model;

import lombok.Getter;

@Getter
public enum AreaCode {
	A01("서울"),
	A02("경기"),
	A03("인천"),
	A04("강원"),
	A05("충북"),
	A06("충남"),
	A07("대전"),
	A08("경북"),
	A09("경남"),
	A10("대구"),
	A11("울산"),
	A12("부산"),
	A13("전북"),
	A14("전남"),
	A15("광주"),
	A16("제주");

	private final String area;

	AreaCode(String area) {
		this.area = area;
	}

	public String value() {
		return area;
	}
}
