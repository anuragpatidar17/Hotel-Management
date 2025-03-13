package com.organisation.hotelManagement.service;

import com.organisation.hotelManagement.repository.HotelRepository;
import org.springframework.stereotype.Service;
import com.organisation.hotelManagement.model.Hotel;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;
import java.util.logging.Logger;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }


    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Transactional
    public Hotel addHotel(Hotel hotel) {
        Logger logger = Logger.getLogger(HotelService.class.getName());

        try {
            //validate all the fields
            if (!StringUtils.hasText(hotel.getName())) {
                throw new IllegalArgumentException("Hotel name cannot be empty.");
            }


            if (!StringUtils.hasText(hotel.getLocation())) {
                throw new IllegalArgumentException("Hotel location cannot be empty.");
            }


            if (hotel.getContact() == null || !hotel.getContact().matches("\\d{10,}")) {
                throw new IllegalArgumentException("Invalid contact number. Must be at least 10 digits.");
            }


            Optional<Hotel> existingHotel = hotelRepository.findByNameAndLocation(hotel.getName(), hotel.getLocation());
            if (existingHotel.isPresent()) {
                throw new IllegalStateException("A hotel with this name already exists at this location.");
            }


            hotel.setContact(hotel.getContact().replaceAll("[^0-9]", ""));


            logger.info("Adding new hotel: " + hotel.getName() + " in " + hotel.getLocation());


            Hotel savedHotel = hotelRepository.save(hotel);


            logger.info("Hotel successfully added with ID: " + savedHotel.getId());
            return savedHotel;

        } catch (Exception e) {
            logger.severe("Error adding hotel: " + e.getMessage());
            throw e;  // Re-throw the error for global exception handling
        }
    }

        public Hotel updateHotel(Long id, Hotel updatedHotel) {
        Optional<Hotel> existingHotel = hotelRepository.findById(id);
        if (existingHotel.isPresent()) {
            Hotel hotel = existingHotel.get();

            if (updatedHotel.getName() != null) {
                hotel.setName(updatedHotel.getName());
            }
            if (updatedHotel.getLocation() != null) {
                hotel.setLocation(updatedHotel.getLocation());
            }
            if (updatedHotel.getContact() != null) {
                hotel.setContact(updatedHotel.getContact());
            }
            if (updatedHotel.getDescription() != null) {
                hotel.setDescription(updatedHotel.getDescription());
            }

            return hotelRepository.save(hotel);
        } else {
            throw new RuntimeException("Hotel with ID " + id + " not found.");
        }

    }

    @Transactional
    public boolean deleteHotel(Long id) {
        if (hotelRepository.existsById(id)) {
            hotelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Hotel> searchHotelsByName(String name) {
        return hotelRepository.searchByName(name);
    }


}
