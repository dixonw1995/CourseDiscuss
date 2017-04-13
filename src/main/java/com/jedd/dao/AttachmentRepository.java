package com.jedd.dao;

import com.jedd.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    public Attachment findByPostIdAndName(long postId, String name);
}
