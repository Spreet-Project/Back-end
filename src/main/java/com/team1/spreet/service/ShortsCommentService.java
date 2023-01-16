package com.team1.spreet.service;

import com.team1.spreet.dto.ShortsCommentDto;
import com.team1.spreet.entity.Shorts;
import com.team1.spreet.entity.ShortsComment;
import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.ShortsCommentRepository;
import com.team1.spreet.repository.ShortsRepository;
import com.team1.spreet.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortsCommentService {

	private final ShortsCommentRepository shortsCommentRepository;
	private final ShortsRepository shortsRepository;
	private final UserRepository userRepository;

	// shortsComment 등록
	public SuccessStatusCode saveShortsComment(Long shortsId, ShortsCommentDto.RequestDto requestDto, Long userId) {
		User user = getUser(userId);

		Shorts shorts = checkShorts(shortsId);
		shortsCommentRepository.saveAndFlush(requestDto.toEntity(shorts, user));

		return SuccessStatusCode.SAVE_SHORTS_COMMENT;
	}

	// shortsComment 수정
	public SuccessStatusCode updateShortsComment(Long shortsCommentId, ShortsCommentDto.RequestDto requestDto, Long userId) {
		User user = getUser(userId);

		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (user.getUserRole() == UserRole.ROLE_ADMIN || checkOwner(shortsComment, user.getId())) {
			shortsComment.updateShortsComment(requestDto.getContent());
		}
		return SuccessStatusCode.UPDATE_SHORTS_COMMENT;
	}

	// shortsComment 삭제
	public SuccessStatusCode deleteShortsComment(Long shortsCommentId, Long userId) {
		User user = getUser(userId);

		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (user.getUserRole() == UserRole.ROLE_ADMIN || checkOwner(shortsComment, user.getId())) {
			shortsCommentRepository.updateDeletedIsTrue(shortsCommentId);
		}
		return SuccessStatusCode.DELETE_SHORTS_COMMENT;
	}

	// shortsComment 조회
	@Transactional(readOnly = true)
	public List<ShortsCommentDto.ResponseDto> getCommentList(Long shortsId) {
		if (shortsRepository.findByIdAndDeletedIsFalse(shortsId).isEmpty()) {
			throw new RestApiException(ErrorStatusCode.NOT_FOUND_SHORTS);
		}

		List<ShortsComment> comments = shortsCommentRepository.findByShortsIdWithUserAndDeletedIsFalseOrderByCreatedAtDesc(shortsId);

		List<ShortsCommentDto.ResponseDto> commentList = new ArrayList<>();
		for (ShortsComment comment : comments) {
			commentList.add(new ShortsCommentDto.ResponseDto(comment));
		}
		return commentList;
	}

	// shorts 가 존재하는지 확인
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findByIdAndDeletedIsFalse(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_FOUND_SHORTS)
		);
	}

	// shortsComment 가 존재하는지 확인
	private ShortsComment checkShortsComment(Long shortsCommentId) {
		return shortsCommentRepository.findByIdAndDeletedIsFalse(shortsCommentId).orElseThrow(
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

	// user 객체 가져오기
	private User getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
		);
	}
}
