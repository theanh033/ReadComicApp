package theanh.android.readcomicapp.Object;

import java.util.HashMap;
import java.util.Map;

public class Comics {
    private String id;
    private String name;
    private String image;
    private String category;

    public Comics(String name, String image, String category) {
        this.name = name;
        this.image = image;
        this.category = category;
    }

    public Comics(String id, String name, String image, String category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.category = category;
    }

    public Comics() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> map() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("image", image);
        hashMap.put("category", category);

        return hashMap;
    }
}
