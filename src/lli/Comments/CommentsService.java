package lli.Comments;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import login.LoginDTO;
import util.TransactionType;

import java.util.List;

public class CommentsService {

    @DAO
    private CommentsDAO commentsDAO;

    @Transactional(transactionType=TransactionType.READONLY)
    public List<Comments> getCommentsByState(long stateId,long applicationID) throws Exception {
        List<Comments> comments= commentsDAO.getCommentsByState(stateId,applicationID);
        if(comments.isEmpty()) {
           return  null;
        }
        return comments;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<RevisedComment> getRevisedCommentsByState(long stateId,long applicationID) throws Exception {
        return commentsDAO.getRevisedCommentsByState(stateId,applicationID);
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<Comments> getCommentsByUser(long userId) throws Exception {
        List<Comments> comments= commentsDAO.getCommentsByUser(userId);
        if(comments == null) {
            throw new RequestFailureException("No Data found ");
        }

        return comments;
    }


    @Transactional
    public void insertComments(Comments comments, LoginDTO loginDTO) throws Exception {

//        if(!loginDTO.getIsAdmin() && lliApplication.getClientID()!=loginDTO.getAccountID()){
//            throw new RequestFailureException("You can not submit other client's application.");
//        }

//        comments.setSubmissionDate(System.currentTimeMillis());
        //lliApplication.setStatus(LLIConnectionConstants.STATUS_APPLIED);
        commentsDAO.insertComments(comments);
    }

    @Transactional
    public void insertComments(RevisedComment comment, LoginDTO loginDTO) throws Exception {
        commentsDAO.insertComments(comment);
    }

    @Transactional
    public void updateApplicaton(Comments comments) throws Exception{
        commentsDAO.updateIFRLLIApplication(comments);
    }
}
