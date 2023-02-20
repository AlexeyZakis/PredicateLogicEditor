package com.example.predicatelogiceditor

import android.text.SpannableStringBuilder
import java.util.*

// Return pos of first wrong bracket else -1
fun getPosOfWrongBracket(string: String): Int {
    val bracketsStack = Stack<Pair<Char, Int>>()
    string.forEachIndexed { index, char ->
        if (char.toString() == OPEN_BRACKET_SYMBOL) {
            bracketsStack.push(Pair(char, index))
        }
        else if (char.toString() == CLOSE_BRACKET_SYMBOL) {
            if (bracketsStack.empty()) {
                return index
            }
            bracketsStack.pop()
        }
    }
    if (!bracketsStack.empty()) {
        return bracketsStack.peek().second
    }
    return -1
}

fun getTextWithColoredWrongBrackets(string: SpannableStringBuilder): SpannableStringBuilder {
    val posOfWrongBracket = getPosOfWrongBracket(string.toString())
    if (posOfWrongBracket == -1) {
        return SpannableStringBuilder(string)
    }
    return getColorText(string, WRONG_BRACKETS_COLOR, posOfWrongBracket, posOfWrongBracket + 1)
}

fun getTextWithColoredCorrectBrackets(string: SpannableStringBuilder): SpannableStringBuilder {
    if (getPosOfWrongBracket(string.toString()) != -1) {
        return SpannableStringBuilder(string)
    }

    val posAndColorsOfBrackets = arrayListOf<Pair<Int, Int>>()
    val bracketsStack = Stack<Pair<Int, Int>>()

    var colorArrayIndex = 0

    string.forEachIndexed { index, char ->
        if (char.toString() == OPEN_BRACKET_SYMBOL) {
            bracketsStack.push(Pair(index, BRACKETS_COLORS[colorArrayIndex]))
            posAndColorsOfBrackets.add(Pair(index, BRACKETS_COLORS[colorArrayIndex]))
            colorArrayIndex = (colorArrayIndex + 1) % BRACKETS_COLORS.size
        }
        else if (char.toString() == CLOSE_BRACKET_SYMBOL) {
            posAndColorsOfBrackets.add(Pair(index, bracketsStack.peek().second))
            bracketsStack.pop()
            colorArrayIndex = (colorArrayIndex + BRACKETS_COLORS.size - 1) % BRACKETS_COLORS.size
        }
    }

    var text = SpannableStringBuilder(string)
    posAndColorsOfBrackets.forEach {
        text = getColorText(text, it.second, it.first, it.first + 1)
    }
    return text
}