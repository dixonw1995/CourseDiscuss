package com.jedd.controller;

import com.jedd.exception.EmptyThread;
import com.jedd.exception.PollNotFound;
import com.jedd.exception.ThreadNotFound;
import com.jedd.exception.UserNotFound;
import com.jedd.model.Thread;
import com.jedd.model.Attachment;
import com.jedd.service.AttachmentService;
import com.jedd.service.PollService;
import com.jedd.service.PostService;
import com.jedd.view.DownloadingView;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostService postService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private PollService pollService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(ModelMap model) throws PollNotFound {
        long lectureThreadCount = postService.countThread("Lecture");
        long labThreadCount = postService.countThread("Lab");
        long otherThreadCount = postService.countThread("Other");
        logger.info(String.format("lecture(%d) lab(%d) other(%d)",
                lectureThreadCount, labThreadCount, otherThreadCount));
        model.addAttribute("lectureThreadCount", lectureThreadCount);
        model.addAttribute("labThreadCount", labThreadCount);
        model.addAttribute("otherThreadCount", otherThreadCount);
        if (pollService.countPolls() > 0) {
            model.addAttribute("vote", new VoteForm());
            model.addAttribute("poll", pollService.getPoll());
        }
        return "forum";
    }

    @RequestMapping(value = {"category/{category}"},
            method = RequestMethod.GET)
    public String list(ModelMap model,
            @PathVariable("category") String category) throws PollNotFound {
        list(model);
        logger.info(String.format("browsing %s", category));
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

    @RequestMapping(value = "category/{category}/post",
            method = RequestMethod.GET)
    public ModelAndView create(@PathVariable("category") String category) {
        logger.info(String.format("posting new %s thread", category));
        ModelAndView mav =
                new ModelAndView("post", "thread", new TopicForm());
        mav.addObject("category", category);
        return mav;
    }

    @RequestMapping(value = "category/{category}/post",
            method = RequestMethod.POST)
    public View create(TopicForm form, Principal principal,
            @PathVariable("category") String category)
            throws IOException, UserNotFound {
        String username = principal.getName();
        String title = form.getTitle();
        String content = form.getContent();
        List<MultipartFile> attachments = form.getAttachments();
        long threadId = postService.post(category, title, username,
                content, attachments);
        logger.info(String.format(
                "%s post new %s thread#%d[%s->%s](%d attachments)",
                username, category, threadId,
                title, content, attachments.size()));
        return new RedirectView("/forum/thread/" + threadId + "?post", true);
    }

    @RequestMapping(value = "thread/{threadId}", method = RequestMethod.GET)
    public ModelAndView read(@PathVariable("threadId") long threadId) {
        logger.info(String.format("Forum: browsing thread#%d", threadId));
        Thread thread = postService.getThread(threadId);
        if (thread == null) {
            return new ModelAndView(new RedirectView("/thread", true));
        }
        logger.info("browsing " + thread);
        logger.info(thread.getPosts().toString());
        ModelAndView mav =
                new ModelAndView("thread", "reply", new PostForm());
        mav.addObject("thread", thread);
        return mav;
    }

    @RequestMapping(value = "thread/{threadId}/reply",
            method = RequestMethod.POST)
    public View reply(PostForm form, Principal principal,
            @PathVariable("threadId") long threadId)
            throws IOException, UserNotFound, ThreadNotFound {
        long postId = postService.reply(threadId, principal.getName(),
                form.getContent(), form.getAttachments());
        logger.info(String.format(
                "%s->%s", postService.getThread(threadId),
                postService.getPost(postId)));
        return new RedirectView("/forum/thread/" + threadId + "?reply", true);
    }

    @RequestMapping(value = "post/{postId}/attachment/{attachment:.+}",
            method = RequestMethod.GET)
    public View download(@PathVariable("postId") long postId,
            @PathVariable("attachment") String name) {
        Attachment attachment = attachmentService.getAttachment(postId, name);
        if (attachment != null) {
            return new DownloadingView(attachment.getName(),
                    attachment.getMimeContentType(),
                    attachment.getContents());
        }
        long threadId = postService.getPost(postId).getThread().getId();
        return new RedirectView("/forum/thread/" + threadId, true);
    }

    @RequestMapping(value = "delete/thread/{threadId}",
            method = RequestMethod.GET)
    public View deleteThread(@PathVariable("threadId") long threadId)
            throws ThreadNotFound {
        String category = postService.getThread(threadId).getCategory();
        logger.info("deleting " + postService.getThread(threadId));
        postService.deleteThread(threadId);
        return new RedirectView("/forum/category/" + category, true);
    }

    @RequestMapping(value = "delete/reply/{postId}", method = RequestMethod.GET)
    public View deletePost(@PathVariable("postId") long postId) {
        long threadId = postService.getPost(postId).getThread().getId();
        logger.info(String.format("deleting %s->%s",
                postService.getThread(threadId),
                postService.getPost(postId)));
        try {
            postService.deletePost(postId);
        } catch (EmptyThread ex) {
            logger.warn("Topic post cannot be deleted alone.");
        }
        return new RedirectView("/forum/thread/" + threadId, true);
    }
    
//    private String vote(long responseId, String username) {
//        logger.info(username + " is voting response#" + responseId);
//        try {
//            pollService.vote(responseId, username);
//        } catch (ResponseNotFound ex) {
//            return "?ResponseNotFound";
//        } catch (UserNotFound ex) {
//            return "?UserNotFound";
//        } catch (UsernameExists ex) {
//            return "?UserNotFound";
//        }
//        return "?vote";
//    }
//
//    @RequestMapping(value = {"vote"},
//            method = RequestMethod.POST)
//    public View vote(VoteForm form,
//            Principal principal) {
//        String result = vote(form.getResponseId(), principal.getName());
//        return new RedirectView("/forum" + result, true);
//    }
//
//    @RequestMapping(value = {"category/{category}/vote"},
//            method = RequestMethod.POST)
//    public View vote(@PathVariable("category") String category,
//            VoteForm form,
//            Principal principal) {
//        String result = vote(form.getResponseId(), principal.getName());
//        return new RedirectView("/category/" + category + result, true);
//    }

}
