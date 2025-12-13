package com.ecobazaarx.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "birthdate")
    private java.sql.Date birthdate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "default_city")
    private String defaultCity;
}
