package com.team1.spreet.service;

import com.team1.spreet.dto.ShortsCommentDto;
import com.team1.spreet.dto.ShortsDto;
import com.team1.spreet.entity.Shorts;
import com.team1.spreet.entity.ShortsComment;
import com.team1.spreet.entity.ShortsLike;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.ShortsLikeRepository;
import com.team1.spreet.repository.ShortsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortsService {

	private final ShortsRepository shortsRepository;
	private final AwsS3Service awsS3Service;
	private final ShortsLikeRepository shortsLikeRepository;

	// shorts 등록
	public SuccessStatusCode saveShorts(ShortsDto.RequestDto requestDto) {
		//User user = SecurityUtil.getCurrentUser();

		String videoUrl = awsS3Service.uploadFile(requestDto.getFile());
		Shorts shorts = new Shorts(requestDto, user, videoUrl);

		shortsRepository.saveAndFlush(shorts);

		return SuccessStatusCode.SAVE_SHORTS;
	}

	// shorts 수정
	public SuccessStatusCode updateShorts(ShortsDto.RequestDto requestDto, Long shortsId) {
		//User user = SecurityUtil.getCurrentUser();
		Shorts shorts = checkShorts(shortsId);

		String videoUrl = null;
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
	public SuccessStatusCode deleteShorts(Long shortsId) {
		//User user = SecurityUtil.getCurrentUser();
		Shorts shorts = checkShorts(shortsId);

		if (user.getUserRole() == UserRole.ROLE_ADMIN || checkOwner(shorts, user.getId())) {
			shortsRepository.deleteById(shortsId);
		}
		return SuccessStatusCode.DELETE_SHORTS;
	}

	// shorts 상세조회
	@Transactional(readOnly = true)
	public ShortsDto.ResponseDto getShorts(Long shortsId) {
		//User user = SecurityUtil.getCurrentUser();

		Shorts shorts = checkShorts(shortsId);
		checkOwner(shorts, user.getId());

		List<ShortsCommentDto.ResponseDto> commentList = new ArrayList<>();
		for (ShortsComment comment : shorts.getShortsCommentList()) {
			commentList.add(new ShortsCommentDto.ResponseDto(comment));
		}
		boolean likeCheck = checkLike(shortsId, user.getId());

		return new ShortsDto.ResponseDto(shorts, likeCheck, commentList);
	}

	// 카테고리별 shorts 조회(페이징)
	@Transactional(readOnly = true)
	public List<ShortsDto.ResponseDto> getShortsByCategory(String category, int page, int size,
		Pageable pageable) {
		//User user = SecurityUtil.getCurrentUser();

		pageable = PageRequest.of(page - 1, size);
		Page<Shorts> pageShorts = shortsRepository.findShortsByCategoryOrderByCreatedAtDesc(
			category, pageable);

		List<ShortsDto.ResponseDto> shortsList = new ArrayList<>();
		for (Shorts shorts : pageShorts) {
			shortsList.add(
				new ShortsDto.ResponseDto(shorts, checkLike(shorts.getId(), user.getId())));
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
		Optional<ShortsLike> shortsLike = shortsLikeRepository.findByShortsIdAndUserId(shortsId,
			userId);
		return shortsLike.isEmpty();
	}

}
