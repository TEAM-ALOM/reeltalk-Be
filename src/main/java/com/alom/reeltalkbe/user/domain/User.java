package com.alom.reeltalkbe.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user")  // 실제 DB 테이블 이름
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 ID
    private Long id;  // ERD 기준: user_id

    @Column(nullable = false, unique = true)
    private String email;  // ERD 기준: email

    @Column(nullable = false)
    private String password;  // ERD 기준: password

    @Column(nullable = false, unique = true)
    private String username;  // ERD 기준: username

    private String badge;  // ERD 기준: 뱃지

    private String description;  // ERD 기준: description (자기소개)

    private String field;  // ERD 기준: Field (사진, 추후 파일 업로드 처리 필요)

    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // ERD 기준: created_at 회원가입 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // ERD 기준: updated_at 회원 정보 수정

    private String role;

    @Builder
    public User(Long id, String email, String password, String username, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
