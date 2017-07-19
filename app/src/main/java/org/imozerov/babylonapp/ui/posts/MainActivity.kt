package org.imozerov.babylonapp.ui.posts

import android.arch.lifecycle.*
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import org.imozerov.babylonapp.R
import org.imozerov.babylonapp.model.Post
import org.imozerov.babylonapp.model.Result
import org.imozerov.babylonapp.ui.details.DetailActivity
import org.imozerov.babylonapp.ui.details.setPost
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LifecycleRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle() = lifecycleRegistry

    private lateinit var adapter: PostsAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = PostsAdapter(this::openDetails)
        adapter.setHasStableIds(true)
        posts_list.adapter = adapter

        val postsVM = ViewModelProviders.of(this, viewModelFactory).get(PostsViewModel::class.java)
        postsVM.posts.observe(this, Observer<Result<List<Post>>> { posts ->
            // TODO handle error or loading status here
            adapter.setPosts(posts?.data ?: listOf())
        })
    }

    private fun openDetails(post: Post) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.setPost(post)
        startActivity(intent)
    }
}
