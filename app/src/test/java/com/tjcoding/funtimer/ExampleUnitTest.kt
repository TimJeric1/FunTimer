package com.tjcoding.funtimer

import com.tjcoding.funtimer.data.mapper.toEntities
import com.tjcoding.funtimer.domain.model.TimerItem
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun mapper_isCorrect() {
        val timerItem1 = TimerItem(
            selectedNumbers = listOf(1,2,3),
            time = 30,
            unixEndTime = 9999,
        )
        val timerItem2 = TimerItem(
            selectedNumbers = listOf(4,5,6),
            time = 60,
            unixEndTime = 7777,
        )
        val entities1 = timerItem1.toEntities()
        val entities2 = timerItem2.toEntities()

        entities1.second.forEach {
            println(it.timerId)
        }
        entities2.second.forEach {
            println(it.timerId)
        }


        assert(entities1.second[0].timerId == entities1.second[1].timerId)
        assert(entities2.second[0].timerId == entities2.second[1].timerId)


        }


    }