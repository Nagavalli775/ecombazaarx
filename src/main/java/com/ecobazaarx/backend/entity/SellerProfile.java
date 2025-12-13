package com.ecobazaarx.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seller_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerProfile {

    @Id
    @Column(name = "seller_id")
    private Long sellerId;

    @OneToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "business_address")
    private String businessAddress;
}
