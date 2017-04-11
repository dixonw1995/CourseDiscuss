package edu.ouhk.comps380f.service;

import edu.ouhk.comps380f.model.Attachment;

public interface AttachmentService {

    public Attachment getAttachment(long postId, String name);
}
