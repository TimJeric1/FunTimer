package com.tjcoding.funtimer.presentation.timer

data class SetupState(
    val displayedNumber: Int = 1,
    val selectedNumbers: List<Int> = ArrayList<Int>(100),
    val possibleNumbers: List<Int> = (1..99).toList(),
    val durationOption: DurationOption = DurationOption.ONE,
    val durations: Map<DurationOption, Int> = mapOf(DurationOption.ONE to 30, DurationOption.TWO to 60)
)


sealed class Duration2 {
    data class OPTION_ONE(val duration: Int): Duration2()
    data class OPTION_TWO(val duration: Int): Duration2()
    data class CUSTOM(val duration: Int): Duration2()
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
        fun durationOptionToDuration(durationOption: DurationOption, durations: Map<DurationOption, Int>): Int {
            val duration = durations.get(durationOption)
            if(duration != null) return duration
            return durations.values.first()
        }
        fun durationOptionToIndex(durationOption: DurationOption): Int{
            return when(durationOption){
                ONE -> 0
                TWO -> 1
                CUSTOM -> 2
            }
        }
    }
}