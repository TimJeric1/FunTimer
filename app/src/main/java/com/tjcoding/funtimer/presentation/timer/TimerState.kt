package com.tjcoding.funtimer.presentation.timer

data class SetupState(
    val displayedNumber: Int = 1,
    val selectedNumbers: List<Int> = ArrayList<Int>(100),
    val possibleNumbers: List<Int> = (1..99).toList(),
    val durationOption: DurationOption = DurationOption.ONE,
    val durations: Map<DurationOption, Int> = mapOf(DurationOption.ONE to 30, DurationOption.TWO to 60, DurationOption.CUSTOM to 5)
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
    ONE, TWO, CUSTOM;

    companion object {
        fun indexToDurationOption(index: Int): DurationOption {
            return when (index) {
                0 -> ONE
                1 -> TWO
                2 -> CUSTOM
                else -> ONE
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
        DurationOption.ONE -> 0
        DurationOption.TWO -> 1
        DurationOption.CUSTOM -> 2
    }
}