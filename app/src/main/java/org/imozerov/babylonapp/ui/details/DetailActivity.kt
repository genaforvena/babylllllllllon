package org.imozerov.babylonapp.ui.details

import android.arch.lifecycle.*
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail.*
import org.imozerov.babylonapp.R
import org.imozerov.babylonapp.model.Post
import javax.inject.Inject


class DetailActivity : AppCompatActivity(), LifecycleRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle() = lifecycleRegistry

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val postId = intent?.postId()
        val detailsVM = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
        detailsVM.setPostId(postId ?: 0)

        detailsVM.postInfo.observe(this, Observer<Post> { post ->
            if (post == null) {
                return@Observer
            }

            bind(post)
        })
    }

    fun bind(post: Post) {
        with (post) {
            supportActionBar?.title = title
            details_author_name.text = userName
            details_body.text = body
            details_title.text = title

            // TODO Use plurals string res
            details_comments_number.text = comments.size.toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        internal val POST_ID = "org.imozerov.POST_ID"
    }
}

fun Intent.postId() : Long {
    return getLongExtra(DetailActivity.POST_ID, 0L)
}

fun Intent.setPost(post: Post) {
    putExtra(DetailActivity.POST_ID, post.id)
}

