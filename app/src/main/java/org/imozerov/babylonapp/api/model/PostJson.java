package org.imozerov.babylonapp.api.model;

import com.google.gson.annotations.SerializedName;

public class PostJson {
    @SerializedName("id")
    private long id;
    @SerializedName("userId")
    private long authorId;
    @SerializedName("body")
    private String body;
    @SerializedName("title")
    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
