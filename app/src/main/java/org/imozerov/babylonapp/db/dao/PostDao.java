package org.imozerov.babylonapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.imozerov.babylonapp.db.entities.PostEntity;
import org.imozerov.babylonapp.model.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PostEntity> posts);

    @Query("Select posts.id, posts.title, posts.body, users.name, users.avatar " +
            "from posts left join users on posts.authorId = users.id")
    LiveData<List<Post>> getAll();

    @Query("SELECT posts.id, posts.title, posts.body, users.name, users.avatar " +
            "from posts left join users on posts.authorId = users.id " +
            "where posts.id = :postId")
    LiveData<Post> get(long postId);
}
