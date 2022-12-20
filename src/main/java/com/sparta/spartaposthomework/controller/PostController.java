package com.sparta.spartaposthomework.controller;

import com.sparta.spartaposthomework.dto.PostRequestDto;
import com.sparta.spartaposthomework.entity.Post;
import com.sparta.spartaposthomework.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 전체 게시글 조회
    @GetMapping("/api/posts")
    public List<Post> getPostList() {
        return postService.getPostAll();
    }

    // 게시글 작성
    @PostMapping("/api/post")
    public Post createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    // 선택 게시글 조회
    @GetMapping("/api/post/{id}")
    public Optional<Post> findPost(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    // 선택 게시글 수정
    @PutMapping("/api/post/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.update(id, requestDto, request);
    }

    // 선택 게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public String deletePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.deletePost(id, requestDto, request);
    }
}