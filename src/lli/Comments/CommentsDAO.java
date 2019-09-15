package lli.Comments;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class CommentsDAO {

    List<Comments> getCommentsByState(long stateId, long applicationID) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(Comments.class,
                new CommentsConditionBuilder()
                        .Where()
                        .stateIDEquals(stateId)
                        .applicationIDEquals(applicationID)
                        .orderBysequenceIDDesc()
                        .getCondition()
        );


    }

    List<RevisedComment> getRevisedCommentsByState(long stateId, long applicationID) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(RevisedComment.class,
                new RevisedCommentConditionBuilder()
                        .Where()
                        .stateIDEquals(stateId)
                        .applicationIDEquals(applicationID)
                        .orderBysequenceIDDesc()
                        .getCondition()
        );
    }

    List<Comments> getCommentsByUser(long userId) throws Exception {

        if (userId > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(Comments.class,
                            new CommentsConditionBuilder()
                                    .Where()
                                    .userIDEquals(userId)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(Comments.class);
        }

    }

    public List<Comments> getCommentsByApplication(long applicationID) throws Exception {

        if (applicationID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(Comments.class,
                            new CommentsConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(applicationID)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(Comments.class);
        }

    }


    public void insertComments(Comments comments) throws Exception {
        List<Comments> commentsList =
                ModifiedSqlGenerator.getAllObjectList(Comments.class,
                        new CommentsConditionBuilder()
                                .Where()
                                .stateIDEquals(comments.getStateID())
                                .applicationIDEquals(comments.getApplicationID())
                                .orderBysequenceIDDesc()
                                .getCondition()
                );


        if (commentsList.size() >= 1) comments.setSequenceID(commentsList.get(0).getSequenceID() + 1);
        insert(comments);
    }

    public void insertComments(RevisedComment comment) throws Exception {
        List<RevisedComment> commentsList =
                ModifiedSqlGenerator.getAllObjectList(RevisedComment.class,
                        new RevisedCommentConditionBuilder()
                                .Where()
                                .stateIDEquals(comment.getStateID())
                                .applicationIDEquals(comment.getApplicationID())
                                .orderBysequenceIDDesc()
                                .getCondition()
                );


        if (commentsList.size() >= 1) comment.setSequenceID(commentsList.get(0).getSequenceID() + 1);
        insert(comment);
    }

    void updateIFRLLIApplication(Comments comments) throws Exception {
        ModifiedSqlGenerator.updateEntity(comments);
    }

}


