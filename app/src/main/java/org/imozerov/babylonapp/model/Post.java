package org.imozerov.babylonapp.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Relation;

import org.imozerov.babylonapp.db.entities.CommentEntity;

import java.util.List;

public class Post {
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "body")
    private String body;
    @ColumnInfo(name = "name")
    private String userName;
    @ColumnInfo(name = "avatar")
    private String userAvatar;
    @Relation(parentColumn = "id", entityColumn = "postId")
    private List<CommentEntity> comments;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != post.id) return false;
        if (title != null ? !title.equals(post.title) : post.title != null) return false;
        if (body != null ? !body.equals(post.body) : post.body != null) return false;
        if (userName != null ? !userName.equals(post.userName) : post.userName != null)
            return false;
        if (userAvatar != null ? !userAvatar.equals(post.userAvatar) : post.userAvatar != null)
            return false;
        return comments != null ? comments.equals(post.comments) : post.comments == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userAvatar != null ? userAvatar.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }
}
