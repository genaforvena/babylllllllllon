package org.imozerov.babylonapp.repository

import org.imozerov.babylonapp.AppExecutors
import org.imozerov.babylonapp.api.BabylonService
import org.imozerov.babylonapp.api.model.CommentJson
import org.imozerov.babylonapp.api.model.PostJson
import org.imozerov.babylonapp.api.model.UserJson
import org.imozerov.babylonapp.db.dao.CommentDao
import org.imozerov.babylonapp.db.dao.PostDao
import org.imozerov.babylonapp.db.dao.UserDao
import org.imozerov.babylonapp.db.entities.CommentEntity
import org.imozerov.babylonapp.db.entities.PostEntity
import org.imozerov.babylonapp.db.entities.UserEntity
import org.imozerov.babylonapp.model.Post
import javax.inject.Inject

class PostsRepository @Inject
constructor(private val postDao: PostDao,
            private val commentDao: CommentDao,
            private val userDao: UserDao,
            private val babylonService: BabylonService,
            private val executors: AppExecutors) {

    fun posts() = object : NetworkBoundResource<List<Post>, List<PostJson>>(executors) {
        override fun saveCallResult(items: List<PostJson>) {
            postDao.insertAll(items.map { it.toEntity() })
        }

        // TODO check if data still valid before sending api request
        override fun shouldFetch(data: List<Post>?) = true

        override fun loadFromDb() = postDao.all

        override fun createCall() = babylonService.posts()
    }
}

internal fun PostJson.toEntity(): PostEntity {
    val post = PostEntity()
    post.id = id
    post.authorId = authorId
    post.body = body
    post.title = title
    return post
}

internal fun UserJson.toEntity(): UserEntity {
    val entity = UserEntity()
    entity.id = id
    entity.email = email
    entity.name = name
    return entity
}

internal fun CommentJson.toEntity(): CommentEntity {
    val entity = CommentEntity()
    entity.id = id
    entity.body = body
    entity.title = name
    entity.postId = postId
    return entity
}