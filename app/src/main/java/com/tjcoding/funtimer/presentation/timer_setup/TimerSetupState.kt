package com.tjcoding.funtimer.presentation.timer_setup

data class TimerSetupState(
    val displayedNumber: Int = 1,
    val selectedNumbers: List<Int> = ArrayList<Int>(100),
    val possibleNumbers: List<Int> = (1..99).toList(),
    val durationOption: DurationOption = DurationOption.THIRTY_MINUTES,
    val durations: Map<DurationOption, Int> = mapOf(DurationOption.THIRTY_MINUTES to 30, DurationOption.SIXTY_MINUTES to 60, DurationOption.CUSTOM to -1)
){
    fun getDuration(): Int {
        return durationOption.toDuration(durations)
    }
    fun getDurationInTimeFormat(): String {
        val duration = getDuration()
        return "$duration:00"
    }
}


enum class DurationOption {
    THIRTY_MINUTES, SIXTY_MINUTES, CUSTOM;

    companion object {
        fun indexToDurationOption(index: Int): DurationOption {
            return when (index) {
                0 -> THIRTY_MINUTES
                1 -> SIXTY_MINUTES
                2 -> CUSTOM
                else -> THIRTY_MINUTES
            }
        }
    }
}

fun DurationOption.toDuration(durations: Map<DurationOption, Int>): Int{
    val duration = durations.get(this)
    if(duration != null) return duration
    return durations.values.first()
}
fun DurationOption.toIndex(): Int{
    return when(this){
        DurationOption.THIRTY_MINUTES -> 0
        DurationOption.SIXTY_MINUTES -> 1
        DurationOption.CUSTOM -> 2
    }
}