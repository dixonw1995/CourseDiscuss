package com.jedd.service;

import com.jedd.dao.PollRepository;
import com.jedd.dao.ResponseRepository;
import com.jedd.dao.UserRepository;
import com.jedd.dao.VoteRepository;
import com.jedd.exception.PollNotFound;
import com.jedd.exception.ResponseNotFound;
import com.jedd.exception.ThreadNotFound;
import com.jedd.exception.UserNotFound;
import com.jedd.exception.UsernameExists;
import com.jedd.model.Poll;
import com.jedd.model.Response;
import com.jedd.model.User;
import com.jedd.model.Vote;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PollService {

    private static Random random = new Random();

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

    @Transactional(rollbackFor = PollNotFound.class)
    public Poll getPoll() throws PollNotFound {
        if (pollRepo.count() == 0) {
            throw new PollNotFound();
        }
        List<Poll> polls = getPolls();
        return polls.get(random.nextInt(polls.size()));
    }

    @Transactional
    public long countPolls() {
        return pollRepo.count();
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

    @Transactional
    public boolean hasVoted(long responseId, String username) {
        return hasVoted(
                responseRepo.findOne(responseId).getPoll(), username);
    }

    @Transactional
    public boolean hasVoted(Poll poll, String username) {
        for (Response response : poll.getResponses()) {
            if (voteRepo.existsByResponseIdAndUsername(
                    response.getId(), username)) {
                return true;
            }
        }
        return false;
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
        if (hasVoted(responseId, username)) {
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
