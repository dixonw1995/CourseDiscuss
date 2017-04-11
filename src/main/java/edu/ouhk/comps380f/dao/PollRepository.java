package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
}
