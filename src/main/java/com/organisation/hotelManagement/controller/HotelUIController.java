package com.organisation.hotelManagement.controller;

import com.organisation.hotelManagement.model.Hotel;
import com.organisation.hotelManagement.service.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/hotels")
public class HotelUIController {
    private final HotelService hotelService;

    public HotelUIController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // Display All Hotels
    @GetMapping
    public String listHotels(Model model, @ModelAttribute("message") String message) {
        List<Hotel> hotels = hotelService.getAllHotels();
        model.addAttribute("hotels", hotels);

        // Ensure message is only added when it has content
        if (message != null && !message.trim().isEmpty()) {
            model.addAttribute("message", message);
        }

        return "hotel-list";
    }

    // Show Add Hotel Form
    @GetMapping("/add")
    public String showAddHotelForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "hotel-form";
    }

    // Add a New Hotel with Flash Message
    @PostMapping("/save")
    public String addHotel(@ModelAttribute Hotel hotel, RedirectAttributes redirectAttributes) {
        hotelService.addHotel(hotel);
        redirectAttributes.addFlashAttribute("message", "Hotel added successfully!");
        return "redirect:/hotels";
    }

    // Show Edit Hotel Form
    @GetMapping("/edit/{id}")
    public String showEditHotelForm(@PathVariable Long id, Model model) {
        model.addAttribute("hotel", hotelService.getHotelById(id).orElse(null));
        return "hotel-form";
    }

    // Update Hotel with Flash Message
    @PostMapping("/update/{id}")
    public String updateHotel(@PathVariable Long id, @ModelAttribute Hotel hotel, RedirectAttributes redirectAttributes) {
        hotelService.updateHotel(id, hotel);
        redirectAttributes.addFlashAttribute("message", "Hotel updated successfully!");
        return "redirect:/hotels";
    }

    // Delete Hotel with Flash Message
    @GetMapping("/delete/{id}")
    public String deleteHotel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = hotelService.deleteHotel(id);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "Hotel deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Hotel not found!");
        }
        return "redirect:/hotels";
    }
}
