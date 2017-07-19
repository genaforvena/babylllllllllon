package org.imozerov.babylonapp.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import org.imozerov.babylonapp.AppExecutors
import org.imozerov.babylonapp.api.BabylonService
import org.imozerov.babylonapp.api.model.CommentJson
import org.imozerov.babylonapp.api.model.PostJson
import org.imozerov.babylonapp.api.model.UserJson
import org.imozerov.babylonapp.db.dao.CommentDao
import org.imozerov.babylonapp.db.dao.PostDao
import org.imozerov.babylonapp.db.dao.UserDao
import org.imozerov.babylonapp.model.Post
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatcher
import org.mockito.Mockito
import java.util.concurrent.Executor

@RunWith(JUnit4::class)
class PostsRepositoryTest {
    @JvmField
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: PostsRepository

    private lateinit var postDao: PostDao
    private lateinit var commentDao: CommentDao
    private lateinit var userDao: UserDao
    private lateinit var babylonService: BabylonService
    private lateinit var executors: AppExecutors

    private val postsFromDb = MutableLiveData<List<Post>>()
    private val postsDbData = generatePosts(10)

    private val postsFromService = MutableLiveData<List<PostJson>>()
    private val postsServiceData = generatePostJsons(10)

    private val usersFromService = MutableLiveData<List<UserJson>>()
    private val usersServiceData = generateUserJsons(10)

    private val commentsFromService = MutableLiveData<List<CommentJson>>()
    private val commentsServiceData = generateUserJsons(10)

    @Before
    fun setUp() {
        postDao = Mockito.mock(PostDao::class.java)
        commentDao = Mockito.mock(CommentDao::class.java)
        userDao = Mockito.mock(UserDao::class.java)
        babylonService = Mockito.mock(BabylonService::class.java)
        executors = AppExecutors(CurrentThreadExecutor(),
                CurrentThreadExecutor(), CurrentThreadExecutor())

        repo = PostsRepository(postDao, commentDao, userDao, babylonService, executors)
    }

    @Test
    fun `posts are fetched from db if no service responses`() {
        repo.posts()

        Mockito.verify(postDao).all
    }

    @Test
    fun `users are fetched from service when posts downloaded`() {
        postsFromDb.value = postsDbData
        usersFromService.value = usersServiceData

        repo.posts().observeForever {

        }

        Mockito.verify(userDao.insertAll(Mockito.argThat { it.map { it.id }.containsAll( usersServiceData.map { it.id })}))
    }

    private fun generatePostJsons(count: Long) =
        (0..count).map { id ->
            PostJson().apply {
                this.id = id
                this.body = id.toString()
                this.title = id.toString()
                this.authorId = id
            }
        }

    private fun generateUserJsons(count: Long) =
        (0..count).map { id ->
            UserJson().apply {
                this.id = id
                this.email = id.toString()
                this.name = id.toString()
            }
        }

    private fun generateCommentJsons(count: Long) =
        (0..count).map { id ->
            CommentJson().apply {
                this.id = id
                this.email = id.toString()
                this.body = id.toString()
                this.name = id.toString()
                this.postId = id
            }
        }

    private fun generatePosts(count: Long) =
        (0..count).map { id ->
            Post().apply {
                this.id = id
                this.body = id.toString()
                this.title = id.toString()
                this.comments = arrayListOf()
                this.userName = id.toString()
                this.userAvatar = id.toString()
            }
        }

    inner class CurrentThreadExecutor : Executor {
        override fun execute(r: Runnable) {
            r.run()
        }
    }
}