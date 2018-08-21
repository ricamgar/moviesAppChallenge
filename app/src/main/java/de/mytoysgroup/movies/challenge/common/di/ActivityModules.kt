package de.mytoysgroup.movies.challenge.common.di

import android.content.Context
import android.support.v7.app.AppCompatActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

fun baseActivityModule(activity: AppCompatActivity) = Kodein.Module {
  bind<Context>(overrides = true) with provider { activity }
}