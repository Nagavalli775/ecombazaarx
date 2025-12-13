package com.ecobazaarx.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProfile {

    @Id
    @Column(name = "admin_id")
    private Long adminId;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private User user;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "access_level")
    private String accessLevel;
}