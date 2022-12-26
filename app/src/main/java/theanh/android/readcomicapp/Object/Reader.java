package theanh.android.readcomicapp.Object;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> map() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", userName);
        hashMap.put("email", userEmail);

        return hashMap;
    }
}
