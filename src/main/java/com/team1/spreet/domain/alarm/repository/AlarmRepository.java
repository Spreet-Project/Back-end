package com.team1.spreet.domain.alarm.repository;

import com.team1.spreet.domain.alarm.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findAllByReceiverIdAndReadFalse(Long receiverId);


}
