package com.organisation.hotelManagement.controller;

import com.organisation.hotelManagement.model.Hotel;
import com.organisation.hotelManagement.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "*")
public class HotelController {
    private final HotelService hotelService;
    private static final Logger logger = Logger.getLogger(HotelController.class.getName());

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // Get all hotels
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    // Get a single hotel by ID
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Search hotels by name
    @GetMapping("/search")
    public ResponseEntity<List<Hotel>> searchHotels(@RequestParam String name) {
        List<Hotel> hotels = hotelService.searchHotelsByName(name);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping
    public ResponseEntity<?> addHotel(@RequestBody Hotel hotel) {
        try {
            // Validate required fields
            if (hotel.getName() == null || hotel.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel name cannot be empty.");
            }
            if (hotel.getLocation() == null || hotel.getLocation().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel location cannot be empty.");
            }
            if (hotel.getContact() == null || hotel.getContact().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel contact cannot be empty.");
            }

            if (hotel.getImage() == null || hotel.getImage().isEmpty()) {
                hotel.setImage("https://hotel-management-images-new.s3.ap-south-1.amazonaws.com/hotel.png");
            }

            // Check if hotel is scheduled for future publishing
            if (hotel.getScheduledPublishDate() != null && hotel.getScheduledPublishDate().isAfter(LocalDateTime.now())) {
                hotel.setPublished(false);  // Not published yet
            } else {
                hotel.setPublished(true);   // Publish immediately
            }

            // Save the hotel
            Hotel savedHotel = hotelService.addHotel(hotel);
            logger.info("Hotel added successfully: " + savedHotel.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedHotel);
        } catch (Exception e) {
            logger.severe("Error saving hotel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving hotel: " + e.getMessage());
        }
    }

    // Full Update (PUT) - Update all fields
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Long id, @RequestBody Hotel updatedHotel) {
        Optional<Hotel> existingHotel = hotelService.getHotelById(id);
        if (existingHotel.isPresent()) {
            Hotel updated = hotelService.updateHotel(id, updatedHotel);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found.");
        }
    }

    // Partial Update (PATCH) - Update only non-null fields
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateHotelPartial(@PathVariable Long id, @RequestBody Hotel updatedHotel) {
        Optional<Hotel> existingHotel = hotelService.getHotelById(id);
        if (existingHotel.isPresent()) {
            Hotel updated = hotelService.updateHotel(id, updatedHotel);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found.");
        }
    }

    // Delete a hotel
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        boolean isDeleted = hotelService.deleteHotel(id);
        return isDeleted ? ResponseEntity.ok("Hotel deleted successfully.")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found.");
    }

    // Handle CORS Preflight Requests (OPTIONS)
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptionsRequest() {
        return ResponseEntity.ok().build();
    }
}
