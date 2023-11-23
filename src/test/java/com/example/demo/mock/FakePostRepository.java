package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostRepository implements PostRepository {
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final List<Post> data = new ArrayList<>();
    @Override
    public Optional<Post> findById(long id) {
        return data.stream()
                .filter(it -> Objects.equals(it.getId(), id))
                .findAny();
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0) {
            Post newPost = Post.builder()
                    .id(idGenerator.incrementAndGet())
                    .content(post.getContent())
                    .writer(post.getWriter())
                    .modifiedAt(post.getModifiedAt())
                    .createdAt(post.getCreatedAt())
                    .build();
            data.add(newPost);
            return newPost;
        } else {
            data.removeIf(it -> Objects.equals(it.getId(), post.getId()));
            data.add(post);
            return post;
        }
    }
}
