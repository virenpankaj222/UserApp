package s.com.userapp.MainDashboard;

import s.com.userapp.MainDashboard.Model.PostModel;

public interface PostListener {
    void onPostSelect(PostModel model);
    void onEdit(PostModel model);
    void onDelete(PostModel model);
    void onStatusChange(PostModel model);
    void onCall(String phone);
}
