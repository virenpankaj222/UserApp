package s.com.userapp.MainDashboard.Full_Details;

public class CommentModel {
    String commentId,userId,comment,userName;

    public CommentModel() {
    }

    public CommentModel(String commentId, String userId, String comment, String userName) {
        this.commentId = commentId;
        this.userId = userId;
        this.comment = comment;
        this.userName = userName;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
