package com.team1.spreet.repository;

import com.team1.spreet.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByFeedId(Long feedId);

}
