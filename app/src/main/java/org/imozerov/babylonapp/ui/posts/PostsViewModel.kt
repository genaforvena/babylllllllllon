package org.imozerov.babylonapp.ui.posts

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import org.imozerov.babylonapp.repository.PostsRepository
import javax.inject.Inject


class PostsViewModel @Inject
constructor(application: Application,
            repo: PostsRepository)
    : AndroidViewModel(application) {

}