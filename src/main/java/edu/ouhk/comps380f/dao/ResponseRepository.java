package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Response;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    
    public List<Response> findByPollId(long pollId);
}
