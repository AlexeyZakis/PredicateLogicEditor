package com.example.predicatelogiceditor

import android.text.SpannableStringBuilder
import android.util.Log
import java.util.Collections.sort

fun isReservedChar(char: Char): Boolean {
    return char.toString() in RESERVED_CHARACTERS
}

// Return pair of declaration pos and variable name
fun getAllVariables(string: String): ArrayList<Pair<Int, String>> {
    val positionsList = arrayListOf<Pair<Int, String>>()
    var variable: String

    var index = 0

    while (index < string.length) {
        if (string[index].toString() == ANY_SYMBOL || string[index].toString() == EXIST_SYMBOL) {
            variable = ""

            while (index < string.length && string[index].toString() in RESERVED_CHARACTERS) {
                ++index
            }
            while (index < string.length && string[index].toString() !in RESERVED_CHARACTERS) {
                variable += string[index++]
            }
            if (variable != "") {
                positionsList.add(Pair(index - variable.length, variable))
            }
        }
        ++index
    }
    return positionsList
}

// Return positions of all substrings
fun getAllElementsPos(string: String, subString: String, exactMatch: Boolean): ArrayList<Int> {
    var index = 0
    val positionsList = arrayListOf<Int>()

    while (true) {
        index = string.indexOf(subString, index)

        if (index == -1) {
            break
        }
        if (exactMatch &&
            ((index - 1 >= 0 && !isReservedChar(string[index - 1])) ||
            (index + subString.length < string.length && !isReservedChar(string[index + subString.length])))
        ) {
            ++index
            continue
        }
        if (positionsList.isNotEmpty() && positionsList[positionsList.size - 1] == index) {
            break
        }
        positionsList.add(index)
        ++index
    }
    return positionsList
}

fun getTextWithColoredElements(string: SpannableStringBuilder, elements: ArrayList<String>, color: Int, exactMatch: Boolean = true): SpannableStringBuilder {
    var text = SpannableStringBuilder(string)

    elements.forEach { element ->
        getAllElementsPos(string.toString(), element, exactMatch).forEach { pos ->
            text = getColorText(text, color, pos, pos + element.length)
        }
    }

   return text
}

fun getTextWithColoredElements(string: SpannableStringBuilder, elements: ArrayList<Pair<Int, String>>, color: Int): SpannableStringBuilder {
    var text = SpannableStringBuilder(string)

    elements.forEach { element ->
        getAllElementsPos(string.toString(), element.second, true).forEach { pos ->
            text = getColorText(text, color, pos, pos + element.second.length)
        }
    }

    return text
}

fun getTextWithColoredWrongNames(string: SpannableStringBuilder, color: Int, constantsList: ArrayList<String>, predicatesList: ArrayList<String>, functionsList: ArrayList<String>, variablesList: ArrayList<Pair<Int, String>>): SpannableStringBuilder {
    val allIndexList = arrayListOf<Int>()
    constantsList.forEach {getAllElementsPos(string.toString(), it, true).forEach {allIndexList.add(it)}}
    predicatesList.forEach {getAllElementsPos(string.toString(), it, true).forEach {allIndexList.add(it)}}
    functionsList.forEach {getAllElementsPos(string.toString(), it, true).forEach {allIndexList.add(it)}}
    variablesList.forEach {getAllElementsPos(string.toString(), it.second, true).forEach {allIndexList.add(it)}}

    logicalConnectivesList.forEach {getAllElementsPos(string.toString(), it, false).forEach {allIndexList.add(it)}}
    quantifierList.forEach {getAllElementsPos(string.toString(), it, false).forEach {allIndexList.add(it)}}

    sort(allIndexList)

    var index = 0

    val allWrongNames = arrayListOf<Pair<Int, Int>>()

    var start: Int
    var end: Int

    while (index < string.length) {
        if (!isReservedChar(string[index])) {
            if (index !in allIndexList) {
                start = index
                while (index < string.length && !isReservedChar(string[index])) {
                    ++index
                }
                end = index
                allWrongNames.add(Pair(start, end))
            }
            else {
                while (index < string.length && !isReservedChar(string[index])) {
                    ++index
                }
            }
        }
        ++index
    }

    var text = SpannableStringBuilder(string)
    allWrongNames.forEach {
        text = getColorText(text, color, it.first, it.second)
    }

    return text
}