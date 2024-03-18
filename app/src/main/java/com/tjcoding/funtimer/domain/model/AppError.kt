package com.tjcoding.funtimer.domain.model

sealed class AppError: Throwable() {
    data object IOError : AppError() {
        private fun readResolve(): Any = IOError
    }

    data object OutOfMemoryError: AppError() {
        private fun readResolve(): Any = OutOfMemoryError
    }

    data object Unknown : AppError() {
        private fun readResolve(): Any = Unknown
    }
}