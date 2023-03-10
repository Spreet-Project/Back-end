package com.team1.spreet.domain.shorts.service;

import com.team1.spreet.domain.admin.service.BadWordService;
import com.team1.spreet.domain.alarm.service.AlarmService;
import com.team1.spreet.domain.shorts.dto.ShortsCommentDto;
import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.shorts.model.ShortsComment;
import com.team1.spreet.domain.shorts.repository.ShortsCommentRepository;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.util.SecurityUtil;
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
	private final AlarmService alarmService;
	private final BadWordService badWordService;

	// shortsComment ๋ฑ๋ก
	public void saveShortsComment(Long shortsId, ShortsCommentDto.RequestDto requestDto) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		Shorts shorts = checkShorts(shortsId);

		String content = badWordService.checkBadWord(requestDto.getContent());
		shortsCommentRepository.saveAndFlush(requestDto.toEntity(content, shorts, user));

		if (!shorts.getUser().getId().equals(user.getId())) {
			alarmService.send(user.getId(),
				"๐ฌ" + shorts.getUser().getNickname() + "๋! " + "์์ฑํ์  shorts์ ๋๊ธ ์๋ฆผ์ด ๋์ฐฉํ์ดYo!\n",
				"https://www.spreet.co.kr/api/shorts/" + shorts.getId(), shorts.getUser().getId());
		}
	}

	// shortsComment ์์ 
	public void updateShortsComment(Long shortsCommentId, ShortsCommentDto.RequestDto requestDto) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		ShortsComment shortsComment = checkShortsComment(shortsCommentId);
		if (!user.getId().equals(shortsComment.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}

		String content = badWordService.checkBadWord(requestDto.getContent());
		shortsComment.updateShortsComment(content);
	}

	// shortsComment ์ญ์ 
	public void deleteShortsComment(Long shortsCommentId) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		ShortsComment shortsComment = checkShortsComment(shortsCommentId);

		if (!user.getUserRole().equals(UserRole.ROLE_ADMIN) && !user.getId().equals(shortsComment.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		shortsComment.idDeleted();
	}

	// shortsComment ์กฐํ
	@Transactional(readOnly = true)
	public List<ShortsCommentDto.ResponseDto> getCommentList(Long shortsId) {
		checkShorts(shortsId);

		return shortsCommentRepository.findAllByShortsId(shortsId);
	}

	// shorts ๊ฐ ์กด์ฌํ๋์ง ํ์ธ
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findByIdAndDeletedFalse(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_SHORTS)
		);
	}

	// shortsComment ๊ฐ ์กด์ฌํ๋์ง ํ์ธ
	private ShortsComment checkShortsComment(Long shortsCommentId) {
		return shortsCommentRepository.findByIdAndDeletedFalseWithUser(shortsCommentId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_COMMENT)
		);
	}
}
