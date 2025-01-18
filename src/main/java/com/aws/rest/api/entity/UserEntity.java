package com.aws.rest.api.entity;

import jakarta.persistence.*; // Javax persistence package for entity annotations

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // Database table name
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for primary key
    @Column(name = "USER_ID") // Map to column in the database
    private Long userId;

    @Column(name = "UID", nullable = false, unique = true) // Unique UID field
    private String uid;

    @Column(name = "CNTPY_NUM", nullable = false) // Country number field
    private String cntpyNum;

    @Column(name = "TITLE_ID") // Title field (optional, can be null)
    private String titleId;

    @Column(name = "FIRST_NAME", nullable = false) // Mandatory first name
    private String firstName;

    @Column(name = "MIDDLE_NAME") // Optional middle name
    private String middleName;

    @Column(name = "LAST_NAME", nullable = false) // Mandatory last name
    private String lastName;
}

