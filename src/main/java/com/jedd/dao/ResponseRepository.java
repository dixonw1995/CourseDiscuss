package com.jedd.dao;

import com.jedd.model.Response;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    public List<Response> findByPollId(long pollId);
}
