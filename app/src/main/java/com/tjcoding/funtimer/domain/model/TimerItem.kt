package com.tjcoding.funtimer.domain.model

data class TimerItem(
    val selectedNumbers: List<Int>,
    val time: Int,
    val unixEndTime: Long,
    val id: Int? = null,
    )