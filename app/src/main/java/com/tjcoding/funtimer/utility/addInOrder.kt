package com.tjcoding.funtimer.utility

fun MutableList<Int>.addInOrder(newNumber: Int){
    this.add(newNumber)
    this.sort()
}