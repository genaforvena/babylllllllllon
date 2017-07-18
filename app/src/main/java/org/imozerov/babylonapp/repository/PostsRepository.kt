package org.imozerov.babylonapp.repository

import org.imozerov.babylonapp.db.dao.CommentDao
import org.imozerov.babylonapp.db.dao.PostDao
import org.imozerov.babylonapp.db.dao.UserDao
import java.util.concurrent.Executors
import javax.inject.Inject

class PostsRepository @Inject
constructor(private val postDao: PostDao,
            private val commentDao: CommentDao,
            private val userDao: UserDao,
            private val executors: Executors) {

//    fun posts() =

}