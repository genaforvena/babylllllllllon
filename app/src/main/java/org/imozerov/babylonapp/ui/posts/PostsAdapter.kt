package org.imozerov.babylonapp.ui.posts

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_post.view.*
import org.imozerov.babylonapp.R
import org.imozerov.babylonapp.model.Post


class PostsAdapter(private val chatClickCallback: (Post) -> Unit) :
        RecyclerView.Adapter<PostsAdapter.ChatViewHolder>() {
    private var posts: List<Post>? = null

    fun setPosts(newPosts: List<Post>) {
        if (posts == null) {
            posts = newPosts
            notifyItemRangeInserted(0, newPosts.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return posts!!.size
                }

                override fun getNewListSize(): Int {
                    return newPosts.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val old = posts!![oldItemPosition]
                    val new = newPosts[newItemPosition]
                    return old.id == new.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val new = newPosts[newItemPosition]
                    val old = newPosts[oldItemPosition]
                    return new == old
                }
            })
            posts = newPosts
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ChatViewHolder(binding, chatClickCallback)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(posts!![position])
    }

    override fun getItemCount() = if (posts == null) 0 else posts!!.size

    class ChatViewHolder(view: View, private val chatClickCallback: (Post) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(post: Post) {
            with(itemView) {
                item_post_body.text = post.body
                item_post_title.text = post.title
                // TODO fix me with plurals
                item_post_comments_count.text = post.comments.size.toString()

                item_user_name.text = post.author.name
                // TODO load avatar
//                item_user_avatar.load()

                setOnClickListener { chatClickCallback.invoke(post) }
            }
        }
    }
}

