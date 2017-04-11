package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Thread;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    
    public List<Thread> findByCategory(String category);
    
    public int countByCategory(String category);
}
