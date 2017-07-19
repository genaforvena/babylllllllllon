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
import org.imozerov.babylonapp.testutil.successCall
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.concurrent.Executor

@RunWith(JUnit4::class)
class PostsRepositoryTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: PostsRepository

    private lateinit var postDao: PostDao
    private lateinit var commentDao: CommentDao
    private lateinit var userDao: UserDao
    private lateinit var babylonService: BabylonService
    private lateinit var executors: AppExecutors

    private val postsFromDb = MutableLiveData<List<Post>>()
    private val postsDbData = generatePosts(10)

    private val postsServiceData = generatePostJsons(10)
    private val usersServiceData = generateUserJsons(10)
    private val commentsServiceData = generateCommentJsons(10)

    @Before
    fun setUp() {
        postDao = Mockito.mock(PostDao::class.java)
        userDao = Mockito.mock(UserDao::class.java)
        commentDao = Mockito.mock(CommentDao::class.java)
        babylonService = Mockito.mock(BabylonService::class.java)

        Mockito.`when`(postDao.all).thenReturn(postsFromDb)
        Mockito.`when`(babylonService.users()).thenReturn(successCall(usersServiceData))
        Mockito.`when`(babylonService.comments()).thenReturn(successCall(commentsServiceData))
        Mockito.`when`(babylonService.posts()).thenReturn(successCall(postsServiceData))

        executors = AppExecutors(CurrentThreadExecutor(),
                CurrentThreadExecutor(), CurrentThreadExecutor())

        repo = PostsRepository(postDao, commentDao, userDao, babylonService, executors)
    }

    @Test
    fun `all data is fetched from service when posts changed in db`() {
        postsFromDb.value = postsDbData

        repo.posts().observeForever {}

        Mockito.verify(babylonService).users()
        Mockito.verify(babylonService).comments()
        Mockito.verify(babylonService).posts()
    }

    @Test
    fun `all data stored in db as soon as fetched`() {
        Mockito.`when`(babylonService.users()).thenReturn(successCall(usersServiceData))

        postsFromDb.value = postsDbData

        repo.posts().observeForever {}

        Mockito.verify(commentDao).insertAll(Mockito.argThat { it.size == commentsServiceData.size })
        Mockito.verify(userDao).insertAll(Mockito.argThat { it.size == usersServiceData.size })
        Mockito.verify(postDao).insertAll(Mockito.argThat { it.size == postsServiceData.size })
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