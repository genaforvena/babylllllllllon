package org.imozerov.babylonapp.ui.details

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import org.imozerov.babylonapp.db.dao.PostDao
import org.imozerov.babylonapp.model.Post
import org.imozerov.babylonapp.util.AbsentLiveData
import javax.inject.Inject

class DetailsViewModel @Inject
constructor(app: Application, private val postDao: PostDao): AndroidViewModel(app) {
    private val postId = MutableLiveData<Long>()

    // TODO We'e fetching here from db. In a real world we would initiate api call
    // like we did in PostsViewModel. Result in this case should also be wrapped in Result class.
    val postInfo = Transformations.switchMap(postId) { postId ->
        if (postId == 0L) {
            return@switchMap AbsentLiveData.create<Post>()
        } else {
            return@switchMap postDao.get(postId)
        }
    }

    fun setPostId(id: Long) {
        postId.value = id
    }
}