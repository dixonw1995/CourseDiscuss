package com.jedd.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "thread_id", insertable = false, updatable = false)
    private long threadId;

    @Column(insertable = false, updatable = false)
    private String username;

    private String content;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "id")
    private Set<Attachment> attachments = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "thread_id")
    private Thread thread;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    public Post() {
    }

    public Post(String content, Thread thread, User user) {
        this.content = content;
        this.thread = thread;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void deleteAttachment(Attachment attachment) {
        attachment.setPost(null);
        this.attachments.remove(attachment);
    }

    public void deleteAttachments() {
        for (Attachment attachment : this.attachments) {
            attachment.setPost(null);
        }
        this.attachments.clear();
    }

//    public void destroy() {
//        if (null != this.user) {
//            this.user.deletePost(this);
//            this.user = null;
//        }
//        this.thread.deletePost(this);
//        this.thread = null;
//    }
    @Override
    public String toString() {
        return String.format("post#%d[%s](%d attachments)-%s",
                id, content, attachments.size(), username);
    }
}
