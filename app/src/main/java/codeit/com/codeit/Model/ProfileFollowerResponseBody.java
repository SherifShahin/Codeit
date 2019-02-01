package codeit.com.codeit.Model;

public class ProfileFollowerResponseBody
{
    private String name;
    private String username;
    private String img;
    private Boolean isfollowing;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Boolean getIsfollowing() {
        return isfollowing;
    }

    public void setIsfollowing(Boolean isfollowing) {
        this.isfollowing = isfollowing;
    }

}
