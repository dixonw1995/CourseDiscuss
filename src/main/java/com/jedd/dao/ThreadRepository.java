package com.jedd.dao;

import com.jedd.model.Thread;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Long> {

    public List<Thread> findByCategory(String category);

    public int countByCategory(String category);
}
