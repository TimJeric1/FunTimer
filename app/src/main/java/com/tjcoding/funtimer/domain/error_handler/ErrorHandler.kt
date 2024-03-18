package com.tjcoding.funtimer.domain.error_handler

import com.tjcoding.funtimer.domain.model.AppError

interface ErrorHandler {

    fun getError(throwable: Throwable): AppError
}