package com.tjcoding.funtimer.presentation.common

enum class DurationOption {
    FIRST, SECOND, THIRD;

    companion object {
        fun indexToDurationOption(index: Int): DurationOption {
            return when (index) {
                0 -> FIRST
                1 -> SECOND
                2 -> THIRD
                else -> FIRST
            }
        }
    }
}

fun DurationOption.toDuration(durations: Map<DurationOption, Int>): Int {
    val duration = durations[this]
    if (duration != null) return duration
    return durations.values.first()
}

fun DurationOption.toIndex(): Int {
    return when (this) {
        DurationOption.FIRST -> 0
        DurationOption.SECOND -> 1
        DurationOption.THIRD -> 2
    }
}