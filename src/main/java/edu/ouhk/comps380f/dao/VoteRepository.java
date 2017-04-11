package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Vote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    
    public List<Vote> findByResponseId(long responseId);
    
    public int countByResponseId(long responseId);

    public boolean existsByResponseIdAndUsername(long responseId, String username);
}
