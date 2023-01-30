package com.team1.spreet.domain.alert.repository;

import com.team1.spreet.domain.alert.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByReceiver(String receiver);

    List<Alert> findAllByReceiverAndIsReadFalse(String receiver);


}