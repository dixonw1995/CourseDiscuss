package edu.ouhk.comps380f.service;

import edu.ouhk.comps380f.dao.PollRepository;
import edu.ouhk.comps380f.dao.ResponseRepository;
import edu.ouhk.comps380f.dao.UserRepository;
import edu.ouhk.comps380f.dao.VoteRepository;
import edu.ouhk.comps380f.exception.ResponseNotFound;
import edu.ouhk.comps380f.exception.ThreadNotFound;
import edu.ouhk.comps380f.exception.UserNotFound;
import edu.ouhk.comps380f.exception.UsernameExists;
import edu.ouhk.comps380f.model.Poll;
import edu.ouhk.comps380f.model.Response;
import edu.ouhk.comps380f.model.User;
import edu.ouhk.comps380f.model.Vote;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PollService {

    @Resource
    private UserRepository userRepo;

    @Resource
    private PollRepository pollRepo;

    @Resource
    private ResponseRepository responseRepo;

    @Resource
    private VoteRepository voteRepo;

    @Transactional
    public long createPoll(String question) {
        Poll poll = new Poll(question);
        Poll savedPoll = pollRepo.save(poll);
        return savedPoll.getId();
    }

    @Transactional
    public long createPoll(String question, String[] responses) {
        Poll poll = new Poll(question);
        for (String response : responses) {
            poll.addResponse(new Response(response, poll));
        }
        Poll savedPoll = pollRepo.save(poll);
        return savedPoll.getId();
    }

    @Transactional
    public List<Poll> getPolls() {
        return pollRepo.findAll();
    }

    @Transactional
    public Poll getPoll(long id) {
        return pollRepo.findOne(id);
    }

    @Transactional
    public void deletePoll(long id) {
        Poll deletedPoll = pollRepo.findOne(id);
        pollRepo.delete(deletedPoll);
    }

    @Transactional
    public long addResponse(long id, String[] responses) {
        Poll poll = pollRepo.findOne(id);
        for (String response : responses) {
            poll.addResponse(new Response(response, poll));
        }
        Poll savedPoll = pollRepo.save(poll);
        return savedPoll.getId();
    }

    @Transactional
    public List<Response> getResponses() {
        return responseRepo.findAll();
    }

    @Transactional
    public List<Response> getResponses(long pollId) {
        return responseRepo.findByPollId(pollId);
    }

    @Transactional
    public Response getResponse(long id) {
        return responseRepo.findOne(id);
    }

    @Transactional
    public void deleteResponse(long id) {
        Response deletedResponse = responseRepo.findOne(id);
        Poll poll = deletedResponse.getPoll();
        poll.deleteResponse(deletedResponse);
        pollRepo.save(poll);
    }

    @Transactional(rollbackFor = {ThreadNotFound.class,
        UserNotFound.class, UsernameExists.class})
    public long vote(long responseId, String username)
            throws ResponseNotFound, UserNotFound, UsernameExists {
        if (!responseRepo.exists(responseId)) {
            throw new ResponseNotFound();
        }
        if (!userRepo.exists(username)) {
            throw new UserNotFound();
        }
        if (voteRepo.existsByResponseIdAndUsername(responseId, username)) {
            throw new UsernameExists();
        }
        Response response = responseRepo.findOne(responseId);
        User user = userRepo.findOne(username);
        Vote vote = new Vote(response, user);
//        user.addVote(vote);
//        User updatedUser = userRepo.save(user);
        response.addVote(vote);
        Poll updatedPoll = pollRepo.save(response.getPoll());
        return vote.getId();
    }

    @Transactional
    public List<Vote> getVotes() {
        return voteRepo.findAll();
    }

    @Transactional
    public List<Vote> getVotes(long responseId) {
        return voteRepo.findByResponseId(responseId);
    }

    @Transactional
    public Vote getVote(long id) {
        return voteRepo.findOne(id);
    }

    @Transactional
    public void deleteVote(long id) {
        Vote deletedVote = voteRepo.findOne(id);
        Response response = deletedVote.getResponse();
//        User user = deletedVote.getUser();
//        user.deleteVote(deletedVote);
//        userRepo.save(user);
        response.deleteVote(deletedVote);
        pollRepo.save(response.getPoll());
    }

}
