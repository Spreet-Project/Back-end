package com.team1.spreet.domain.shorts.service;

import com.team1.spreet.domain.shorts.dto.ShortsCommentDto;
import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.shorts.model.ShortsComment;
import com.team1.spreet.domain.shorts.repository.ShortsCommentRepository;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
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

	// shortsComment 등록
	public void saveShortsComment(Long shortsId, ShortsCommentDto.RequestDto requestDto, User user) {
		Shorts shorts = checkShorts(shortsId);
		shortsCommentRepository.saveAndFlush(requestDto.toEntity(shorts, user));
	}

	// shortsComment 수정
	public void updateShortsComment(Long shortsCommentId, ShortsCommentDto.RequestDto requestDto, User user) {
		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (!user.getId().equals(shortsComment.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		shortsComment.updateShortsComment(requestDto.getContent());
	}

	// shortsComment 삭제
	public void deleteShortsComment(Long shortsCommentId, User user) {
		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (!user.getUserRole().equals(UserRole.ROLE_ADMIN) && !user.getId().equals(shortsComment.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		shortsComment.idDeleted();
	}

	// shortsComment 조회
	@Transactional(readOnly = true)
	public List<ShortsCommentDto.ResponseDto> getCommentList(Long shortsId) {
		checkShorts(shortsId);

		return shortsCommentRepository.findAllByShortsId(shortsId);
	}

	// shorts 가 존재하는지 확인
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findByIdAndDeletedFalse(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_SHORTS)
		);
	}

	// shortsComment 가 존재하는지 확인
	private ShortsComment checkShortsComment(Long shortsCommentId) {
		return shortsCommentRepository.findByIdAndDeletedFalseWithUser(shortsCommentId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_COMMENT)
		);
	}
}
