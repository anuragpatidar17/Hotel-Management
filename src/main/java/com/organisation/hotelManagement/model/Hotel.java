package com.organisation.hotelManagement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String contact;
    private String description;
    @Getter
    @Setter
    @Column(name = "image")
    private String image;

    private boolean published;
    private LocalDateTime scheduledPublishDate;

    @PrePersist
    public void setDefaultImage() {
        if (this.image == null || this.image.isEmpty()) {
            this.image = "https://hotel-management-images-new.s3.ap-south-1.amazonaws.com/hotel.png"; //
        }
    }
}
