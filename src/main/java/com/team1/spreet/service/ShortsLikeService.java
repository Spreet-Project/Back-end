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
	public CustomResponseBody<ShortsLikeDto.ResponseDto> setShortsLike(Long shortsId, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
		);

		Shorts shorts = checkShorts(shortsId);

		ShortsLike findShortsLike = shortsLikeRepository.findByShortsIdAndUserId(shortsId, user.getId()).orElse(null);
		if (findShortsLike != null) {
			shorts.cancelLike();
			shortsLikeRepository.delete(findShortsLike);
			return new CustomResponseBody<>(SuccessStatusCode.SHORTS_DISLIKE, new ShortsLikeDto.ResponseDto(false));
		} else {
			ShortsLike shortsLike = new ShortsLike(shorts, user);
			shorts.addLike();
			shortsLikeRepository.save(shortsLike);
			return new CustomResponseBody<>(SuccessStatusCode.SHORTS_LIKE, new ShortsLikeDto.ResponseDto(true));
		}

//		if (shortsLike != null && shortsLike.isLike()) {
//			//기존에 좋아요를 했던 회원이 취소하는 경우
//			shorts.cancleLike();
//			return new CustomResponseBody<>(SuccessStatusCode.SHORTS_DISLIKE, new ShortsLikeDto.ResponseDto(false));
//		} else if (shortsLike != null) {
//			//좋아요를 취소해서 현재 false 인 경우(DB 데이터 존재)
//			shorts.addLike();
//			return new CustomResponseBody<>(SuccessStatusCode.SHORTS_LIKE, new ShortsLikeDto.ResponseDto(true));
//		} else {
//			//처음 좋아요를 하는 경우(DB 데이터 존재하지 않음)
//			ShortsLike setShortsLike = new ShortsLike(shorts, user);
//			shorts.addLike();
//			shortsLikeRepository.save(setShortsLike);
//			return new CustomResponseBody<>(SuccessStatusCode.SHORTS_LIKE, new ShortsLikeDto.ResponseDto(true));
//		}
	}

	// shorts 가 존재하는지 확인
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findById(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_FOUND_SHORTS)
		);
	}
}
