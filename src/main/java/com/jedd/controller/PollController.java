package com.jedd.controller;

import com.jedd.exception.EmptyThread;
import com.jedd.exception.PollNotFound;
import com.jedd.exception.ResponseNotFound;
import com.jedd.exception.UserNotFound;
import com.jedd.exception.UsernameExists;
import com.jedd.model.Poll;
import com.jedd.service.PollService;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("poll")
public class PollController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PollService pollService;

    @RequestMapping(value = {"vote"},
            method = RequestMethod.POST)
    public View vote(VoteForm form,
            Principal principal) {
        long responseId = form.getResponseId();
        String username = principal.getName();
        logger.info(username + " is voting response#" + responseId);
        String result = "?vote";
        try {
            pollService.vote(responseId, username);
        } catch (ResponseNotFound ex) {
            result = "?ResponseNotFound";
        } catch (UserNotFound ex) {
            result = "?UserNotFound";
        } catch (UsernameExists ex) {
            result = "?UsernameExists";
        }
        return new RedirectView("/poll" + result, true);
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        List<Poll> polls = pollService.getPolls();
        logger.info("browsing" + polls.size() + " polls");
        model.addAttribute("vote", new VoteForm());
        model.addAttribute("polls", pollService.getPolls());
        return "poll";
    }

    public static class PollForm {

        private String question;
        private String[] responses;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String[] getResponses() {
            return responses;
        }

        public void setResponses(String[] responses) {
            this.responses = responses;
        }
        
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        logger.info("creating new poll");
        ModelAndView mav
                = new ModelAndView("createPoll", "poll", new PollForm());
        return mav;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(PollForm form) {
        String question = form.getQuestion();
        String[] responses = form.getResponses();
        long pollId = pollService.createPoll(question, responses);
        logger.info(String.format("new poll#%d[%s], %d responses->%s",
                pollId, question, responses.length, responses));
        return new RedirectView("/poll" + "?create", true);
    }

    @RequestMapping(value = "delete/{pollId}",
            method = RequestMethod.GET)
    public View delete(@PathVariable("pollId") long pollId)
            throws PollNotFound {
        logger.info("deleting " + pollService.getPoll(pollId));
        pollService.deletePoll(pollId);
        return new RedirectView("/poll" + "?delete", true);
    }

//    public class VoteForm {
//
//        private long responseId;
//
//        public long getResponseId() {
//            return responseId;
//        }
//
//        public void setResponseId(long responseId) {
//            this.responseId = responseId;
//        }
//
//    }
//
//    private void poll(ModelMap model) {
//        model.addAttribute("vote", new VoteForm());
//        model.addAttribute("poll", pollService.getPoll());
//    }
//
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

class VoteForm {

    private long responseId;

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

}
