package com.team1.spreet.service;

import com.team1.spreet.dto.ShortsCommentDto;
import com.team1.spreet.dto.ShortsDto;
import com.team1.spreet.entity.Category;
import com.team1.spreet.entity.Shorts;
import com.team1.spreet.entity.ShortsComment;
import com.team1.spreet.entity.ShortsLike;
import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.ShortsLikeRepository;
import com.team1.spreet.repository.ShortsRepository;
import com.team1.spreet.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortsService {

	private final ShortsRepository shortsRepository;
	private final AwsS3Service awsS3Service;
	private final ShortsLikeRepository shortsLikeRepository;
	private final UserRepository userRepository;

	// shorts 등록
	public SuccessStatusCode saveShorts(ShortsDto.RequestDto requestDto, Long userId) {
		User user = getUser(userId);

		String videoUrl = awsS3Service.uploadFile(requestDto.getFile());
		Shorts shorts = new Shorts(requestDto, user, videoUrl);

		shortsRepository.saveAndFlush(shorts);

		return SuccessStatusCode.SAVE_SHORTS;
	}

	// shorts 수정
	public SuccessStatusCode updateShorts(ShortsDto.UpdateRequestDto requestDto, Long shortsId, Long userId) {
		User user = getUser(userId);

		Shorts shorts = checkShorts(shortsId);
		String videoUrl;

		if (user.getUserRole() == UserRole.ROLE_ADMIN || checkOwner(shorts, user.getId())) {
			if (!requestDto.getFile().isEmpty()) {
				//첨부파일 수정시 기존 첨부파일 삭제
				String fileName = shorts.getVideoUrl().split(".com/")[1];
				awsS3Service.deleteFile(fileName);

				//새로운 파일 업로드
				videoUrl = awsS3Service.uploadFile(requestDto.getFile());
			} else {
				//첨부파일 수정 안함
				videoUrl = shorts.getVideoUrl();
			}
			shorts.update(requestDto, user, videoUrl);
		}
		return SuccessStatusCode.UPDATE_SHORTS;
	}


	// shorts 삭제
	public SuccessStatusCode deleteShorts(Long shortsId, Long userId) {
		User user = getUser(userId);

		Shorts shorts = checkShorts(shortsId);

		if (user.getUserRole() == UserRole.ROLE_ADMIN || checkOwner(shorts, user.getId())) {
			shortsRepository.deleteById(shortsId);
		}
		return SuccessStatusCode.DELETE_SHORTS;
	}

	// shorts 상세조회
	@Transactional(readOnly = true)
	public ShortsDto.ResponseDto getShorts(Long shortsId, Long userId) {
		Shorts shorts = checkShorts(shortsId);

		List<ShortsCommentDto.ResponseDto> commentList = new ArrayList<>();
		for (ShortsComment comment : shorts.getShortsCommentList()) {
			commentList.add(new ShortsCommentDto.ResponseDto(comment));
		}

		if (userId == 0L) {   //로그인 하지 않은 user 의 경우
			return new ShortsDto.ResponseDto(shorts, false, commentList);
		} else {
			return new ShortsDto.ResponseDto(shorts, checkLike(shortsId, userId), commentList);
		}
	}

	// 카테고리별 shorts 조회(페이징)
	@Transactional(readOnly = true)
	public List<ShortsDto.ResponseDto> getShortsByCategory(Category category, int page, int size, Long userId) {
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Shorts> pageShorts = shortsRepository.findShortsByCategory(category, pageable);

		List<ShortsDto.ResponseDto> shortsList = new ArrayList<>();

		if (userId == 0L) {   //로그인 하지 않은 user 의 경우
			for (Shorts shorts : pageShorts) {
				shortsList.add(new ShortsDto.ResponseDto(shorts, false, null));
			}
		} else {
			for (Shorts shorts : pageShorts) {
				shortsList.add(new ShortsDto.ResponseDto(shorts,
					checkLike(shorts.getId(), userId), null));
			}
		}
		return shortsList;
	}

	// shorts 가 존재하는지 확인
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findById(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_FOUND_SHORTS)
		);
	}

	// shorts 작성자와 user 가 같은지 확인
	private boolean checkOwner(Shorts shorts, Long userId) {
		if (!shorts.getUser().getId().equals(userId)) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		return true;
	}

	// user 가 해당 shorts 에 좋아요를 눌렀는지 확인
	private boolean checkLike(Long shortsId, Long userId) {
		ShortsLike shortsLike = shortsLikeRepository.findByShortsIdAndUserId(shortsId, userId).orElse(null);
		return shortsLike != null;
	}

	// user 객체 가져오기
	private User getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION));
	}

}
