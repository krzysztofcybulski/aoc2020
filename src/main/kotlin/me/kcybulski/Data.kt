package me.kcybulski

class Data

fun lines(name: String, splitter: String = "\n") = Data::class.java
    .getResource("/${name}.txt")
    .readText()
    .split(splitter)
    .filter { it.isNotBlank() }

