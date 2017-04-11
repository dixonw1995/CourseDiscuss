package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
