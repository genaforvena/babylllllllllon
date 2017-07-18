package org.imozerov.babylonapp.di;


import android.app.Application;
import android.arch.persistence.room.Room;

import org.imozerov.babylonapp.api.BabylonService;
import org.imozerov.babylonapp.db.AppDatabase;
import org.imozerov.babylonapp.db.dao.CommentDao;
import org.imozerov.babylonapp.db.dao.PostDao;
import org.imozerov.babylonapp.db.dao.UserDao;
import org.imozerov.babylonapp.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton @Provides
    BabylonService provideBabylonService() {
        return new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(BabylonService.class);
    }

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
