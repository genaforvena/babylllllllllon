package org.imozerov.babylonapp.di;


import android.app.Application;
import android.arch.persistence.room.Room;

import org.imozerov.babylonapp.db.AppDatabase;
import org.imozerov.babylonapp.db.dao.CommentDao;
import org.imozerov.babylonapp.db.dao.PostDao;
import org.imozerov.babylonapp.db.dao.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton
    @Provides
    AppDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Singleton @Provides
    CommentDao provideCommentDao(AppDatabase db) {
        return db.commentDao();
    }

    @Singleton @Provides
    UserDao provideUserDao(AppDatabase db) {
        return db.userDao();
    }

    @Singleton @Provides
    PostDao providePostDao(AppDatabase db) {
        return db.postDao();
    }
}
