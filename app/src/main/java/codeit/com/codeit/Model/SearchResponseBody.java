package codeit.com.codeit.Model;

import java.util.List;

public class SearchResponseBody
{
    private List<search_posts> posts;
    private List<search_users> users;


    public List<search_posts> getPosts() {
        return posts;
    }

    public void setPosts(List<search_posts> posts) {
        this.posts = posts;
    }

    public List<search_users> getUsers() {
        return users;
    }

    public void setUsers(List<search_users> users) {
        this.users = users;
    }
}
