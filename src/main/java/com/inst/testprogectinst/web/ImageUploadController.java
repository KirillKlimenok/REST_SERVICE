package com.inst.testprogectinst.web;

import com.inst.testprogectinst.entity.Image;
import com.inst.testprogectinst.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/image")
public class ImageUploadController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImageToUser(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        imageService.uploadImageToUser(file, principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<?> uploadImageToPost(@PathVariable String postId,
                                               @RequestParam MultipartFile file,
                                               Principal principal) throws IOException {
        imageService.uploadImageToPost(file, UUID.fromString(postId), principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profileImg")
    public ResponseEntity<Image> getImageForUser(Principal principal) {
        Image imageToUser = imageService.getImageToUser(principal);

        return new ResponseEntity<>(imageToUser, HttpStatus.OK);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<Image> getImageToPost(@PathVariable String postId) {
        Image imagePost = imageService.getImagePost(UUID.fromString(postId));
        return new ResponseEntity<>(imagePost, HttpStatus.OK);
    }
}
