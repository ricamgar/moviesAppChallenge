package de.mytoysgroup.movies.challenge.common.domain.usecase

abstract class UseCase<in Params, out ReturnType> where ReturnType : Any {
  abstract fun execute(params: Params): ReturnType
}
