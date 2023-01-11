package com.team1.spreet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EmailConfirm implements Serializable {

    @Id
    private String email;

    private String confirmCode;
}