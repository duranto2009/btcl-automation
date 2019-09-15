package entity.comment;

import annotation.Transactional;
import common.RequestFailureException;
import exception.NoDataFoundException;
import global.GlobalService;
import requestMapping.Service;
import user.UserRepository;
import util.TransactionType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommentService {
    @Service
    GlobalService globalService;

    /**
     *
     * @param stateId Long, Id of the state
     * @param moduleId Integer, Id of Module, will be found at ModuleConstants file
     * @param entityId Long, Id of entity( app, link etc.)
     * @return all comments
     * @throws Exception
     */
    @Transactional(transactionType=TransactionType.READONLY)
    public List<Comment> getCommentByState(int stateId, int moduleId, long entityId) throws Exception {
        List<Comment> Comment= globalService.getAllObjectListByCondition(Comment.class,
                    new CommentConditionBuilder()
                            .Where()
                            .stateIdEquals(stateId)
                            .moduleIdEquals(moduleId)
                            .entityIdEquals(entityId)
                            .getCondition()
                );
//        if(Comment == null) {
//            throw new RequestFailureException("No Data found ");
//        }

        return Comment;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public Comment getLatestCommentByState(int stateId, int moduleId, long entityId) throws Exception {
        List<Comment> comments= globalService.getAllObjectListByCondition(Comment.class,
                new CommentConditionBuilder()
                        .Where()
                        .stateIdEquals(stateId)
                        .moduleIdEquals(moduleId)
                        .entityIdEquals(entityId)
                        .orderBysequenceIdDesc()
                        .limit(1)
                        .getCondition()

        );
        return comments.stream()
                .findFirst()
                .orElse(null);
//                .orElseThrow(()-> new NoDataFoundException("No Latest Comment Found with blah blah blah"));

    }
    public List<Comment> getCommentByEntityId(int moduleId, long entityId) throws Exception {
        List<Comment> Comment= globalService.getAllObjectListByCondition(Comment.class,
                new CommentConditionBuilder()
                        .Where()
//                        .stateIdEquals(stateId)
                        .moduleIdEquals(moduleId)
                        .entityIdEquals(entityId)
                        .getCondition()
        );
        if(Comment == null) {
            throw new RequestFailureException("No Comments found on the entity Id: " + entityId + ", moduleId: " + moduleId);
        }
        return Comment.stream().map(comment -> {
             comment.setCommentProviderName(UserRepository.getInstance().getUserDTOByUserID(comment.getUserId()).getFullName());
             return comment;
        }).collect(Collectors.toList());
//        return Comment;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<Comment> getCommentByUser(long userId, int moduleId) throws Exception {
        List<Comment> Comment= globalService.getAllObjectListByCondition(Comment.class,
                new CommentConditionBuilder()
                        .Where()
                        .userIdEquals(userId)
                        .moduleIdEquals(moduleId)
                        .getCondition()
        );
        if(Comment == null) {
            throw new RequestFailureException("No Data found ");
        }

        return Comment;
    }

    @Transactional
    public void insertComment(Comment comment) throws Exception {
        Comment lastComment = this.getLatestCommentByState(comment.getStateId(),comment.getModuleId(),comment.getEntityId());
        if(lastComment!=null){
            comment.setSequenceId(lastComment.getSequenceId()+1);
        }else{
            comment.setSequenceId(1L);
        }
        globalService.save(comment);
    }
    @Transactional
    public void insertComment(String comment, long entityId, long userId,int moduleId, int stateId){

        //insert the comment
        Comment commentObject = Comment.builder()
                .comment(comment)
                .entityId(entityId)
                .stateId(stateId)
                .moduleId(moduleId)
                .submissionDate(System.currentTimeMillis())
                .userId(userId)
                .build();
        try {
            this.insertComment(commentObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
