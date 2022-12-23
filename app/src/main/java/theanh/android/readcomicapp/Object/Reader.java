package theanh.android.readcomicapp.Object;

public class Reader {
    private String userName;
    private String userEmail;

    public Reader(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Reader() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
