package com.tjcoding.funtimer.data.error_handler

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOutOfMemoryException
import com.tjcoding.funtimer.domain.error_handler.ErrorHandler
import com.tjcoding.funtimer.domain.model.AppError
import java.io.IOException
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor() : ErrorHandler {

    override fun getError(throwable: Throwable): AppError {
        return when (throwable) {
            is IOException -> AppError.IOError
            is SQLiteException -> {
                when(throwable) {
                    is SQLiteOutOfMemoryException -> AppError.OutOfMemoryError
                    else -> AppError.Unknown
                }
            }
            else -> AppError.Unknown
        }
    }
}
