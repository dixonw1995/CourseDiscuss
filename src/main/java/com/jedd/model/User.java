package com.jedd.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    private String username;

    private String password;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<UserRole> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.DETACH, orphanRemoval = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy(value = "id")
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.DETACH, orphanRemoval = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy(value = "id")
    private Set<Vote> votes = new LinkedHashSet<>();

    public User() {
    }

    public User(String username, String password, String[] roles) {
        this.username = username;
        this.password = password;
        for (String role : roles) {
            this.roles.add(new UserRole(this, role));
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public void addRole(String role) {
        this.roles.add(new UserRole(this, role));
    }

    public void removeRole(UserRole role) {
        role.setUser(null);
        this.roles.remove(role);
    }
    
    public void removeRoles() {
        for(UserRole role: this.roles) {
            role.setUser(null);
        }
        roles.clear();
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void deletePost(Post post) {
        this.posts.remove(post);
    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void deleteVote(Vote vote) {
        this.votes.remove(vote);
    }
    
    @Override
    public String toString() {
        return String.format("user#%s posts(%d) votes(%d)", username, posts.size(), votes.size());
    }
}
