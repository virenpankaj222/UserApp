package s.com.userapp.MainDashboard.Model;

public class PostModel {
    String postId,userId,postTitle,name,email,phone,skypeId,telegram,callDate,callTime,costRange,discription;

    public PostModel() {
    }

    public PostModel(String postId, String userId, String postTitle, String name, String email, String phone, String skypeId, String telegram, String callDate, String callTime, String costRange, String discription) {
        this.postId = postId;
        this.userId = userId;
        this.postTitle = postTitle;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.skypeId = skypeId;
        this.telegram = telegram;
        this.callDate = callDate;
        this.callTime = callTime;
        this.costRange = costRange;
        this.discription = discription;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCostRange() {
        return costRange;
    }

    public void setCostRange(String costRange) {
        this.costRange = costRange;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
