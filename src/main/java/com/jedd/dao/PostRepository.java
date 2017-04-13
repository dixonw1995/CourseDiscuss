package com.jedd.dao;

import com.jedd.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    public List<Post> findByThreadId(long threadId);

    public int countByThreadId(long threadId);
}
