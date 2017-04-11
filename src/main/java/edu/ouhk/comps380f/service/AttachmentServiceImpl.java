package edu.ouhk.comps380f.service;

import edu.ouhk.comps380f.dao.AttachmentRepository;
import edu.ouhk.comps380f.model.Attachment;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Resource
    private AttachmentRepository attachmentRepo;

    @Override
    @Transactional
    public Attachment getAttachment(long postId, String name) {
        return attachmentRepo.findByPostIdAndName(postId, name);
    }
}
