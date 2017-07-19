package org.imozerov.babylonapp.di;

import org.imozerov.babylonapp.ui.details.DetailActivity;
import org.imozerov.babylonapp.ui.posts.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity contributeDetailActivity();
}
