package com.team1.spreet.domain.shorts.service;

import com.team1.spreet.domain.shorts.dto.ShortsLikeDto;
import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.shorts.model.ShortsLike;
import com.team1.spreet.domain.shorts.repository.ShortsLikeRepository;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortsLikeService {
	private final ShortsLikeRepository shortsLikeRepository;
	private final ShortsRepository shortsRepository;

	// shortsLike 등록
	public CustomResponseBody<ShortsLikeDto.ResponseDto> setShortsLike(Long shortsId, User user) {
		Shorts shorts = checkShorts(shortsId);

		ShortsLike findShortsLike = shortsLikeRepository.findByShortsIdAndUserId(shortsId, user.getId()).orElse(null);
		if (findShortsLike != null) {
			shorts.cancelLike();
			shortsLikeRepository.delete(findShortsLike);
			return new CustomResponseBody<>(SuccessStatusCode.DISLIKE, new ShortsLikeDto.ResponseDto(false));
		} else {
			ShortsLike shortsLike = new ShortsLike(shorts, user);
			shorts.addLike();
			shortsLikeRepository.save(shortsLike);
			return new CustomResponseBody<>(SuccessStatusCode.LIKE, new ShortsLikeDto.ResponseDto(true));
		}
	}

	// shorts 가 존재하는지 확인
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findById(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_SHORTS)
		);
	}
}
