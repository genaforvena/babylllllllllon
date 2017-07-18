package org.imozerov.babylonapp.repository

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
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
            liveData.addSource(babylonService.posts()) { result ->
                Log.v("ILYA", "finished posts")
                if (result?.isSuccessful() == true) {
                    executors.diskIO.execute { postDao.insertAll(result.body!!.map { it.toEntity() }) }
                }
            }
            liveData.addSource(babylonService.users()) { result ->
                Log.v("ILYA", "finished users")
                if (result?.isSuccessful() == true) {
                    executors.diskIO.execute { userDao.insertAll(result.body!!.map { it.toEntity() }) }
                }
            }
            liveData.addSource(babylonService.comments()) { result ->
                Log.v("ILYA", "finished comments")
                if (result?.isSuccessful() == true) {
                    executors.diskIO.execute { commentDao.insertAll(result.body!!.map { it.toEntity() }) }
                }
            }

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