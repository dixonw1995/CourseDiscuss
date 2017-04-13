package com.jedd.service;

import com.jedd.dao.AttachmentRepository;
import com.jedd.exception.ThreadNotFound;
import com.jedd.model.Thread;
import com.jedd.model.Post;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.jedd.dao.PostRepository;
import com.jedd.dao.ThreadRepository;
import com.jedd.dao.UserRepository;
import com.jedd.exception.EmptyThread;
import com.jedd.exception.UserNotFound;
import com.jedd.model.Attachment;
import com.jedd.model.User;
import javax.persistence.FetchType;

@Service
public class PostService {

    @Resource
    private UserRepository userRepo;

    @Resource
    private ThreadRepository threadRepo;

    @Resource
    private PostRepository postRepo;

    @Resource
    private AttachmentRepository attachmentRepo;

    @Transactional(rollbackFor = UserNotFound.class)
    public long post(String category, String title, String username,
            String content, List<MultipartFile> attachments)
            throws IOException, UserNotFound {
        if (!userRepo.exists(username)) {
            throw new UserNotFound();
        }
        Thread thread = new Thread(category, title);
        User user = userRepo.findOne(username);
        Post post = new Post(content, thread, user);

        for (MultipartFile filePart : attachments) {
            Attachment attachment = new Attachment(
                    filePart.getOriginalFilename(),
                    filePart.getContentType(),
                    filePart.getBytes(),
                    post);
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                post.addAttachment(attachment);
            }
        }

//        user.addPost(post);
//        User updatedUser = userRepo.save(user);
        thread.addPost(post);
        Thread savedThread = threadRepo.save(thread);
        return savedThread.getId();
    }

    @Transactional
    public List<Thread> getThreads() {
        List<Thread> threads = threadRepo.findAll();
        return threads;
    }

    @Transactional
    public List<Thread> getThreads(String category) {
        List<Thread> threads = threadRepo.findByCategory(category);
        return threads;
    }

    @Transactional
    public Thread getThread(long id) {
        Thread thread = threadRepo.findOne(id);
        return thread;
    }

    @Transactional
    public long countThread() {
        return threadRepo.count();
    }

    @Transactional
    public long countThread(String category) {
        return threadRepo.countByCategory(category);
    }

    @Transactional(rollbackFor = UserNotFound.class)
    public void deleteThread(long id) throws ThreadNotFound {
        if (!threadRepo.exists(id)) {
            throw new ThreadNotFound();
        }
        threadRepo.delete(id);
    }

    @Transactional(rollbackFor = {ThreadNotFound.class, UserNotFound.class})
    public long reply(long threadId, String username,
            String content, List<MultipartFile> attachments)
            throws IOException, ThreadNotFound, UserNotFound {
        if (!threadRepo.exists(threadId)) {
            throw new ThreadNotFound();
        }
        if (!userRepo.exists(username)) {
            throw new UserNotFound();
        }
        Thread thread = threadRepo.findOne(threadId);
        User user = userRepo.findOne(username);
        Post post = new Post(content, thread, user);

        for (MultipartFile filePart : attachments) {
            Attachment attachment = new Attachment(
                    filePart.getOriginalFilename(),
                    filePart.getContentType(),
                    filePart.getBytes(),
                    post);
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                post.addAttachment(attachment);
            }
        }

//        user.addPost(post);
//        User updatedUser = userRepo.save(user);
        thread.addPost(post);
        Thread updatedThread = threadRepo.save(thread);
        return updatedThread.getId();
    }

    @Transactional
    public List<Post> getPosts() {
        List<Post> posts = postRepo.findAll();
        return posts;
    }

    @Transactional
    public List<Post> getPosts(long threadId) {
        List<Post> posts = postRepo.findByThreadId(threadId);
        return posts;
    }

    @Transactional
    public Post getPost(long id) {
        Post post = postRepo.findOne(id);
        return post;
    }

    @Transactional(rollbackFor = EmptyThread.class)
    public void deletePost(long id) throws EmptyThread {
        Post deletedPost = postRepo.findOne(id);
        Thread thread = deletedPost.getThread();
        if (thread.isTopic(deletedPost)) {
            throw new EmptyThread();
        }
//        User user = deletedPost.getUser();
//        deletedPost.destroy();
//        userRepo.save(user);
        thread.deletePost(deletedPost);
        threadRepo.save(thread);
    }
}
