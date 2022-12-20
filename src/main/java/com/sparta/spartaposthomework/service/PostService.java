package com.sparta.spartaposthomework.service;

import com.sparta.spartaposthomework.dto.PostRequestDto;
import com.sparta.spartaposthomework.entity.Post;
import com.sparta.spartaposthomework.entity.User;
import com.sparta.spartaposthomework.jwt.JwtUtil;
import com.sparta.spartaposthomework.repository.PostRepository;
import com.sparta.spartaposthomework.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    // 전체 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<Post> getPostAll() {
        return postRepository.findAllByOrderByModifiedAtDesc();
    }

    // 게시글 작성
    @Transactional
    public Post createPost(PostRequestDto requestDto, HttpServletRequest request) {
        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 생성 가능
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용해 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 토큰값이 유효하면 게시글 생성 가능
            Post post = new Post(requestDto, user);
            postRepository.save(post);
            return post;
        }
        return null;
    }

    // 선택 게시글 조회
    @Transactional
    public Optional<Post> findPostById(Long id) {
        return Optional.ofNullable(postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("특정 게시글이 없습니다.")
        ));
    }

    // 선택 게시글 수정
    @Transactional
    public Post update(Long id, PostRequestDto requestDto, HttpServletRequest request) {

        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //선택한 게시글 찾기
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정하고자 하는 게시글이 없습니다!")
        );

        // 토큰이 있는 경우에만 게시글 수정 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            if (post.getUsername().equals(user.getUsername())) {
                post.update(requestDto);
            } else throw new RuntimeException("작성한 본인만 게시글 수정이 가능합니다.");
        }
        return null;
    }

    @Transactional
    public String deletePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("삭제하고자 하는 게시글이 없습니다!")
        );

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            if (post.getUsername().equals(user.getUsername())) {
                postRepository.deleteById(id);
            } else throw new RuntimeException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }else {
            return null;
        }
        return "게시글 삭제 성공!";
    }
}