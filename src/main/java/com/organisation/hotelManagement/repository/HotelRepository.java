package com.organisation.hotelManagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.organisation.hotelManagement.model.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Hotel> searchByName(@Param("name") String name);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) = LOWER(:name) AND LOWER(h.location) = LOWER(:location)")
    Optional<Hotel> findByNameAndLocation(@Param("name") String name, @Param("location") String location);

    List<Hotel> findByPublishedFalseAndScheduledPublishDateBefore(LocalDateTime now);
}
