package com.team1.spreet.service;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.ShortsLikeDto;
import com.team1.spreet.entity.Shorts;
import com.team1.spreet.entity.ShortsLike;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.ShortsLikeRepository;
import com.team1.spreet.repository.ShortsRepository;
import com.team1.spreet.repository.UserRepository;
import com.team1.spreet.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortsLikeService {
	private final ShortsLikeRepository shortsLikeRepository;
	private final ShortsRepository shortsRepository;
	private final UserRepository userRepository;


	// shortsLike 등록
	public CustomResponseBody<ShortsLikeDto.ResponseDto> setShortsLike(Long shortsId, UserDetailsImpl userDetails) {
		User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
		);

		Shorts shorts = checkShorts(shortsId);

		ShortsLike findShortsLike = shortsLikeRepository.findByShortsIdAndUserIdAndIsDeletedFalse(shortsId, user.getId()).orElse(null);
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
