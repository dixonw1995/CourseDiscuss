package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.exception.EmptyThread;
import edu.ouhk.comps380f.exception.ThreadNotFound;
import edu.ouhk.comps380f.exception.UserNotFound;
import edu.ouhk.comps380f.model.Thread;
import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.service.AttachmentService;
import edu.ouhk.comps380f.service.PostService;
import edu.ouhk.comps380f.view.DownloadingView;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("forum")
public class ForumController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostService postService;

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(ModelMap map) {
        map.addAttribute("lectureThreadCount", postService.countThread("Lecture"));
        map.addAttribute("labThreadCount", postService.countThread("Lab"));
        map.addAttribute("otherThreadCount", postService.countThread("Other"));
        return "forum";
    }

    @RequestMapping(value = {"category/{category}"}, method = RequestMethod.GET)
    public String list(ModelMap model,
            @PathVariable("category") String category) {
        model.addAttribute("category", category);
        model.addAttribute("threads", postService.getThreads(category));
        return "forum";
    }

    public static class PostForm {

        private String content;
        private List<MultipartFile> attachments;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
    }

    public static class TopicForm extends PostForm {

        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    @RequestMapping(value = "category/{category}/post", method = RequestMethod.GET)
    public ModelAndView create(@PathVariable("category") String category) {
        ModelAndView mav = new ModelAndView("post", "thread", new TopicForm());
        mav.addObject("category", category);
        return mav;
    }

    @RequestMapping(value = "category/{category}/post", method = RequestMethod.POST)
    public View create(TopicForm form, Principal principal,
            @PathVariable("category") String category) throws IOException, UserNotFound {
        long threadId = postService.post(category, form.getTitle(),
                principal.getName(), form.getContent(), form.getAttachments());
        return new RedirectView("/forum/thread/" + threadId + "?post", true);
    }

    @RequestMapping(value = "thread/{threadId}", method = RequestMethod.GET)
    public ModelAndView readThread(@PathVariable("threadId") long threadId) {
        Thread thread = postService.getThread(threadId);
        if (thread == null) {
            return new ModelAndView(new RedirectView("/thread", true));
        }
        ModelAndView mav = new ModelAndView("thread", "reply", new PostForm());
        mav.addObject("thread", thread);
        return mav;
    }

    @RequestMapping(value = "thread/{threadId}/reply", method = RequestMethod.POST)
    public View reply(PostForm form, Principal principal,
            @PathVariable("threadId") long threadId) throws IOException, UserNotFound, ThreadNotFound {
        long postId = postService.reply(threadId, principal.getName(),
                form.getContent(), form.getAttachments());
        return new RedirectView("/forum/thread/" + threadId + "?reply", true);
    }

    @RequestMapping(value = "post/{postId}/attachment/{attachment:.+}",
            method = RequestMethod.GET)
    public View download(@PathVariable("postId") long postId,
            @PathVariable("attachment") String name) {
        Attachment attachment = attachmentService.getAttachment(postId, name);
        if (attachment != null) {
            return new DownloadingView(attachment.getName(),
                    attachment.getMimeContentType(), attachment.getContents());
        }
        long threadId = postService.getPost(postId).getThread().getId();
        return new RedirectView("/forum/thread/" + threadId, true);
    }

    @RequestMapping(value = "delete/thread/{threadId}", method = RequestMethod.GET)
    public View deleteThread(@PathVariable("threadId") long threadId) throws ThreadNotFound {
        postService.deleteThread(threadId);
        return new RedirectView("/forum", true);
    }

    @RequestMapping(value = "delete/reply/{postId}", method = RequestMethod.GET)
    public View deletePost(@PathVariable("postId") long postId) {
        long threadId = postService.getPost(postId).getThread().getId();
        try {
            postService.deletePost(postId);
        } catch (EmptyThread ex) {
            logger.info("Topic post cannot be deleted alone.");
        }
        return new RedirectView("/forum/thread/" + threadId, true);
    }

}
