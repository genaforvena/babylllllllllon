package org.imozerov.babylonapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.imozerov.babylonapp.db.dao.CommentDao;
import org.imozerov.babylonapp.db.dao.PostDao;
import org.imozerov.babylonapp.db.dao.UserDao;
import org.imozerov.babylonapp.db.entities.CommentEntity;
import org.imozerov.babylonapp.db.entities.PostEntity;
import org.imozerov.babylonapp.db.entities.UserEntity;

@Database(entities = {PostEntity.class,
        CommentEntity.class, UserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "imozerov-babylon-db";

    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    public abstract PostDao postDao();
}
