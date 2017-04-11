package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    public Attachment findByPostIdAndName(long postId, String name);
}
