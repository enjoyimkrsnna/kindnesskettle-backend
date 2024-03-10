package com.kindnesskattle.bddAtcProject.Controller;


import com.kindnesskattle.bddAtcProject.DTO.LikesSummaryDTO;
import com.kindnesskattle.bddAtcProject.Services.FetchLikesService;
import com.kindnesskattle.bddAtcProject.Services.GetLikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@RestController
@RequestMapping("/")
@Slf4j
public class Controller {

    @Autowired
    private final GetLikesService getLikesService;
    private final FetchLikesService fetchLikesService;

    public Controller(GetLikesService getLikesService, FetchLikesService fetchLikesService) {
        this.getLikesService = getLikesService;
        this.fetchLikesService = fetchLikesService;
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        System.out.println("Welcome to kindnessKettle");
        return ResponseEntity.ok("Welcome to Kindness Kettle! 🌟 Meet our amazing team:\n\n- Krishna Singh\n- Ajay Singh\n- Nisha Jain\n\nWe're here to make your experience delightful. Feel free to explore and share kindness with our community. If you have any questions, reach out to us. Cheers to a journey filled with positivity and warmth! 🚀");

    }



    @PostMapping("/addLikes")
    public ResponseEntity<String> addLike(@RequestParam Long userId, @RequestParam Long postId) {
        try {

            log.info("Log message :- userID= "+userId +"PostID = "+ postId);
            getLikesService.addLike(userId, postId);

            return ResponseEntity.ok("Like added successfully.");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/getLikes")
    public ResponseEntity<List<LikesSummaryDTO>> getLikes(@RequestParam Long postId) {
        try {
            log.info("Log message :- PostID = " + postId);

            // Call FetchLikesService to retrieve likes information
            List<LikesSummaryDTO> likesSummaryList = fetchLikesService.getLikesSummaryByPostId(postId);

            Long totalLikes = Long.valueOf(likesSummaryList.size());

            likesSummaryList.forEach(dto -> dto.setTotalLikes(totalLikes));
            return ResponseEntity.ok(likesSummaryList);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


}





