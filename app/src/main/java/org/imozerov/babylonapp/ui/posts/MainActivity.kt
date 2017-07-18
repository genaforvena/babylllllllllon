package org.imozerov.babylonapp.ui.posts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.imozerov.babylonapp.R
import org.imozerov.babylonapp.model.Post

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RecyclerView.Adapter<PostsAdapter.ChatViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = PostsAdapter(this::openDetails)
        posts_list.adapter = adapter
    }

    private fun openDetails(post: Post) {

    }
}
