package com.jedd.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Thread implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String category;

    private String title;

    @OneToMany(mappedBy = "thread", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "id")
    private Set<Post> posts = new LinkedHashSet<>();

    public Thread() {
    }

    public Thread(String category, String title) {
        this.category = category;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void deletePost(Post post) {
        post.setThread(null);
        this.posts.remove(post);
        if (post.getUser() == null) {
            return;
        }
        post.getUser().deletePost(post);
        post.setUser(null);
    }

    public void deletePosts() {
        for (Post post : this.posts) {
            post.setThread(null);
        }
        this.posts.clear();
    }

    public boolean isTopic(Post post) {
        if (posts == null || posts.isEmpty()) {
            return false;
        }
        return this.posts.iterator().next() == post;
    }

    @Override
    public String toString() {
        return String.format("%s thread#%d[%s]", category, id, title);
    }

}
