package com.team1.spreet.service;

import com.team1.spreet.dto.ShortsCommentDto;
import com.team1.spreet.entity.Shorts;
import com.team1.spreet.entity.ShortsComment;
import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.repository.ShortsCommentRepository;
import com.team1.spreet.repository.ShortsRepository;
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

	// shortsComment 등록
	public void saveShortsComment(Long shortsId, ShortsCommentDto.RequestDto requestDto, User user) {
		Shorts shorts = checkShorts(shortsId);
		shortsCommentRepository.saveAndFlush(requestDto.toEntity(shorts, user));
	}

	// shortsComment 수정
	public void updateShortsComment(Long shortsCommentId, ShortsCommentDto.RequestDto requestDto, User user) {
		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (!user.equals(shortsComment.getUser())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		shortsComment.updateShortsComment(requestDto.getContent());
	}

	// shortsComment 삭제
	public void deleteShortsComment(Long shortsCommentId, User user) {
		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (!user.getUserRole().equals(UserRole.ROLE_ADMIN) && !user.equals(shortsComment.getUser())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		shortsComment.idDeleted();
	}

	// shortsComment 조회
	@Transactional(readOnly = true)
	public List<ShortsCommentDto.ResponseDto> getCommentList(Long shortsId) {
		checkShorts(shortsId);

		List<ShortsComment> comments = shortsCommentRepository.
			findByShortsIdAndIsDeletedFalseWithUserOrderByCreatedAtDesc(shortsId);

		List<ShortsCommentDto.ResponseDto> commentList = new ArrayList<>();
		for (ShortsComment comment : comments) {
			commentList.add(new ShortsCommentDto.ResponseDto(comment));
		}
		return commentList;
	}

	// shorts 가 존재하는지 확인
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findByIdAndIsDeletedFalse(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_SHORTS)
		);
	}

	// shortsComment 가 존재하는지 확인
	private ShortsComment checkShortsComment(Long shortsCommentId) {
		return shortsCommentRepository.findByIdAndIsDeletedFalseWithUser(shortsCommentId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_COMMENT)
		);
	}
}
