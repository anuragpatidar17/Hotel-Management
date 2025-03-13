package com.organisation.hotelManagement.scheduler;

import com.organisation.hotelManagement.model.Hotel;
import com.organisation.hotelManagement.repository.HotelRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class HotelScheduler {
    private final HotelRepository hotelRepository;
    private static final Logger logger = Logger.getLogger(HotelScheduler.class.getName());

    public HotelScheduler(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    //
    @Scheduled(fixedRate = 3600000) // Every 1 hour
    public void publishScheduledHotels() {
        List<Hotel> hotelsToPublish = hotelRepository.findByPublishedFalseAndScheduledPublishDateBefore(LocalDateTime.now());

        for (Hotel hotel : hotelsToPublish) {
            hotel.setPublished(true);
            hotelRepository.save(hotel);
            logger.info("Published hotel: " + hotel.getName() + " at " + LocalDateTime.now());
        }
    }
}
