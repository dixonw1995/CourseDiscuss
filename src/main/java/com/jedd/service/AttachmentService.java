package com.jedd.service;

import com.jedd.model.Attachment;

public interface AttachmentService {

    public Attachment getAttachment(long postId, String name);
}
