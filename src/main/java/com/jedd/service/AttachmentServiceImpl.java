package com.jedd.service;

import com.jedd.dao.AttachmentRepository;
import com.jedd.model.Attachment;
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
