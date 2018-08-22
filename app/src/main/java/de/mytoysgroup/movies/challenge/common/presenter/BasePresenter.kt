package de.mytoysgroup.movies.challenge.common.presenter

import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : BasePresenter.View>(
  private val backgroundThread: Scheduler,
  private val mainThread: Scheduler
) {

  protected val disposables = CompositeDisposable()

  var view: T? = null

  open fun resume(view: T) {
    this.view = view
  }

  open fun pause() {
    disposables.clear()
    this.view = null
  }

  protected fun <T> observableSchedulers(): ObservableTransformer<T, T> =
    ObservableTransformer { upstream -> upstream.subscribeOn(backgroundThread).observeOn(mainThread) }

  protected fun <T> singleSchedulers(): SingleTransformer<T, T> =
    SingleTransformer { single -> single.subscribeOn(backgroundThread).observeOn(mainThread) }

  protected fun completableSchedulers(): CompletableTransformer =
    CompletableTransformer { single -> single.subscribeOn(backgroundThread).observeOn(mainThread) }

  interface View {
    fun setLoadingVisibility(visible: Boolean)
    fun showError(error: Throwable)
  }
}
