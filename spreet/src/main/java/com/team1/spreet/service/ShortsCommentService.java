package com.team1.spreet.service;

import com.team1.spreet.dto.ShortsCommentDto;
import com.team1.spreet.entity.Shorts;
import com.team1.spreet.entity.ShortsComment;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.ShortsCommentRepository;
import com.team1.spreet.repository.ShortsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortsCommentService {

	private final ShortsCommentRepository shortsCommentRepository;
	private final ShortsRepository shortsRepository;

	// shortsComment 등록
	public ShortsCommentDto.ResponseDto saveShortsComment(
		Long shortsId, ShortsCommentDto.RequestDto requestDto) {

//		User user = SecurityUtil.getCurrentUser();
		Shorts shorts = checkShorts(shortsId);

		ShortsComment shortsComment = new ShortsComment(requestDto.getContent(), shorts, user);
		shortsCommentRepository.saveAndFlush(shortsComment);

		return new ShortsCommentDto.ResponseDto(shortsComment);
	}

	// shortsComment 수정
	public ShortsCommentDto.ResponseDto updateShortsComment(
		Long shortsCommentId, ShortsCommentDto.RequestDto requestDto) {

		//User user = SecurityUtil.getCurrentUser();
		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (user.getUserRole() == UserRole.ROLE_ADMIN || checkOwner(shortsComment, user.getId())) {
			shortsComment.updateShortsComment(requestDto);
		}
		return new ShortsCommentDto.ResponseDto(shortsComment);
	}

	// shortsComment 수정
	public SuccessStatusCode deleteShortsComment(Long shortsCommentId) {
		//User user = SecurityUtil.getCurrentUser();
		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (user.getUserRole() == UserRole.ROLE_ADMIN || checkOwner(shortsComment, user.getId())) {
			shortsCommentRepository.deleteById(shortsCommentId);
		}
		return SuccessStatusCode.DELETE_SHORTS_COMMENT;
	}


	// shorts 가 존재하는지 확인
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findById(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_FOUND_SHORTS)
		);
	}

	// shortsComment 가 존재하는지 확인
	private ShortsComment checkShortsComment(Long shortsCommentId) {
		return shortsCommentRepository.findById(shortsCommentId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_FOUND_SHORTS_COMMENT)
		);
	}

	// shortsComment 작성자가 user 와 일치하는지 확인
	private boolean checkOwner(ShortsComment shortsComment, Long userId) {
		if (!userId.equals(shortsComment.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		return true;
	}
}
