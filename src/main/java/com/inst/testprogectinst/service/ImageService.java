package com.inst.testprogectinst.service;

import com.inst.testprogectinst.entity.Image;
import com.inst.testprogectinst.entity.Post;
import com.inst.testprogectinst.entity.User;
import com.inst.testprogectinst.repo.ImageRepository;
import com.inst.testprogectinst.repo.PostRepository;
import com.inst.testprogectinst.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public Image uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);

        Image imageProfile = imageRepository.findImageByUserId(user.getId()).orElse(null);

        if (!ObjectUtils.isEmpty(imageProfile)) {
            imageRepository.delete(imageProfile);
        }

        return imageRepository.save(Image.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .name(file.getOriginalFilename())
                .imageBytes(/*compressBytes(*/file.getBytes()/*)*/)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build());
    }

    public Image uploadImageToPost(MultipartFile multipartFile, UUID postId, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);

        Post post = postRepository.findPostByIdAndUserId(postId, user.getId())
                .stream()
                .collect(toSinglePostCollectors());

        return imageRepository.save(Image.builder()
                .id(UUID.randomUUID())
                .postId(post.getId())
                .name(multipartFile.getOriginalFilename())
                .imageBytes(multipartFile.getBytes())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build());
    }

    public Image getImageToUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        Image image = imageRepository.findImageByUserId(user.getId()).orElse(null);

        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(/*decompressBytes(*/image.getImageBytes()/*)*/);
        }

        return image;
    }

    public Image getImagePost(UUID postId) {
        Image image = imageRepository.findImageByPostId(postId)
                .orElseThrow(() -> new NullPointerException("image not found"));

        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(/*decompressBytes(*/image.getImageBytes()/*)*/);
        }
        return image;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("cannot compress bytes");
        }

        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                byteArrayOutputStream.write(buffer, 0, count);
            }
            byteArrayOutputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error(e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }

    private <T> Collector<T, ?, T> toSinglePostCollectors() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            if (list.size() != 1) {
                throw new IllegalStateException();
            }
            return list.get(0);
        });
    }


    private User getUserByPrincipal(Principal principal) {
        String login = principal.getName();
        return userRepository.findUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Login not found"));
    }
}
