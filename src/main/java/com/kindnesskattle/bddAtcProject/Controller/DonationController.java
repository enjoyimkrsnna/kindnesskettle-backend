package com.kindnesskattle.bddAtcProject.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindnesskattle.bddAtcProject.DTO.DonationPostDetailsDTO;
import com.kindnesskattle.bddAtcProject.DTO.DontationAddressDTO;
import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Services.CreateDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DonationController {

    @Autowired
    CreateDonationService createDonationPost;


    @GetMapping("/checking_pin_code/{pin_code}")
    public ResponseEntity<?> pincodechecking(@PathVariable String pin_code) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://www.postalpincode.in/api/pincode/" + pin_code;

        String response;
        try {
            response = restTemplate.getForObject(apiUrl, String.class);
            System.out.println(response);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while calling the external API");
        }

        if (response != null && !response.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response);
                String status = jsonNode.get("Status").asText();

                if ("Error".equals(status)) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());

                } else {

                    return ResponseEntity.ok("Success: " + response); // Adjust the response format as needed
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Empty response from the external API");
        }
    }


    @PostMapping("/donationPosts")
    public ResponseEntity<String> createDonationPost(@RequestBody DontationAddressDTO request) {
        try {
            DonationPost donationPost = createDonationPost.createDonationPost(request);
            return ResponseEntity.ok("Donation post added successfully");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add donation post: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/donationPosts/{postId}")
    public ResponseEntity<String> deleteDonationPost(@PathVariable Long postId) {
        try {
            createDonationPost.deleteDonationPost(postId);
            return ResponseEntity.ok("Donation post and associated address deleted successfully");
        } catch (IllegalArgumentException e) {
            throw new  IllegalArgumentException(e.getMessage());
        }
    }

    @GetMapping("/fetchDonationPosts/{postId}")
    public ResponseEntity<DonationPostDetailsDTO> getDonationPostDetails(@PathVariable Long postId) {
        try {
            DonationPostDetailsDTO detailsDTO = createDonationPost.getDonationPostDetails(postId);
            return ResponseEntity.ok(detailsDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @GetMapping("/fetchAllDonationPosts")
    public ResponseEntity<List<DonationPostDetailsDTO>> getAllDonationPostsDetails() {
        List<DonationPostDetailsDTO> donationPostsDetails = createDonationPost.getAllDonationPostsDetails();
        return ResponseEntity.ok(donationPostsDetails);
    }

}
