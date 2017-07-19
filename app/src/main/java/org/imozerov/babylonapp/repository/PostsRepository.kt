package org.imozerov.babylonapp.repository

import android.arch.lifecycle.MediatorLiveData
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
import org.imozerov.babylonapp.model.Result
import javax.inject.Inject

class PostsRepository @Inject
constructor(private val postDao: PostDao,
            private val commentDao: CommentDao,
            private val userDao: UserDao,
            private val babylonService: BabylonService,
            private val executors: AppExecutors) {
    private val liveData = MediatorLiveData<Result<List<Post>>>()

    init {
        liveData.value = Result.loading(null)
        val dbSource = postDao.all
        liveData.addSource(dbSource) {
            liveData.removeSource(dbSource)

            // TODO this naive implementation. We'e just fetching everything from backend on each db update
            val fetchPosts = babylonService.posts()
            liveData.addSource(fetchPosts) { result ->
                liveData.removeSource(fetchPosts)
                if (result?.isSuccessful() == true) {
                    executors.diskIO.execute { postDao.insertAll(result.body!!.map { it.toEntity() }) }
                }
            }

            val fetchUsers = babylonService.users()
            liveData.addSource(fetchUsers) { result ->
                liveData.removeSource(fetchUsers)
                if (result?.isSuccessful() == true) {
                    executors.diskIO.execute { userDao.insertAll(result.body!!.map { it.toEntity() }) }
                }
            }

            val fetchComments = babylonService.comments()
            liveData.addSource(fetchComments) { result ->
                liveData.removeSource(fetchComments)
                if (result?.isSuccessful() == true) {
                    executors.diskIO.execute { commentDao.insertAll(result.body!!.map { it.toEntity() }) }
                }
            }

            // TODO We're optimistically saying that everything is always fine. In real word we should
            // propagate API response status to the UI through VM with wrapper class' Result status.
            liveData.addSource(dbSource) { result ->
                liveData.postValue(Result.success(result))
            }
        }
    }

    fun posts() = liveData
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