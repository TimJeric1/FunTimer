package com.tjcoding.funtimer.presentation.common


enum class LayoutView {
    STANDARD, ALTERNATIVE;


    companion object {
        fun fromString(value: String): LayoutView {
            return when (value) {
                "STANDARD" -> STANDARD
                "ALTERNATIVE" -> ALTERNATIVE
                else -> {
                    STANDARD
                }
            }
        }
    }
}
