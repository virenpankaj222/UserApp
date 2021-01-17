package s.com.userapp.MainDashboard.Model;

public class AdminCredentials_model {
    String userId,password,deviceId;
    boolean login;

    public AdminCredentials_model() {
    }

    public AdminCredentials_model(String userId, String password,String deviceId, boolean login) {
        this.userId = userId;
        this.password = password;
        this.login = login;
        this.deviceId=deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
