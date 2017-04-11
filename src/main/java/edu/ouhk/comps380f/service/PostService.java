package edu.ouhk.comps380f.service;

import edu.ouhk.comps380f.dao.AttachmentRepository;
import edu.ouhk.comps380f.exception.ThreadNotFound;
import edu.ouhk.comps380f.model.Thread;
import edu.ouhk.comps380f.model.Post;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import edu.ouhk.comps380f.dao.PostRepository;
import edu.ouhk.comps380f.dao.ThreadRepository;
import edu.ouhk.comps380f.dao.UserRepository;
import edu.ouhk.comps380f.exception.EmptyThread;
import edu.ouhk.comps380f.exception.UserNotFound;
import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.User;
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

    @Transactional
    public void deleteThread(long id) {
        Thread deletedThread = threadRepo.findOne(id);
        threadRepo.delete(deletedThread);
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

    @Transactional
    public void deletePost(long id) throws EmptyThread {
        Post deletedPost = postRepo.findOne(id);
        Thread thread = deletedPost.getThread();
        if (thread.getPosts().indexOf(deletedPost) == 0) {
            throw new EmptyThread();
        }
//        User user = deletedPost.getUser();
        deletedPost.destroy();
//        userRepo.save(user);
        threadRepo.save(thread);
    }
}
