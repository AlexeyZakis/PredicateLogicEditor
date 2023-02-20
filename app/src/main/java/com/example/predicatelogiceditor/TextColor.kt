package com.example.predicatelogiceditor

import android.text.SpannableStringBuilder
import android.util.Log

fun isPartOfName(char: Char): Boolean {
    Log.e("asd", char.toString())
    Log.e("asd", (char.toString() in RESERVED_CHARACTERS).toString())
    return char.toString() in RESERVED_CHARACTERS
}

fun getAllElementsPos(string: String, subString: String, exactMatch: Boolean): ArrayList<Int> {
    var index = 0
    val positionsList = arrayListOf<Int>()

    while (true) {
        index = string.indexOf(subString, index)

        if (index == -1) {
            break
        }
        if (exactMatch &&
            ((index - 1 >= 0 && !isPartOfName(string[index - 1])) ||
            (index + subString.length < string.length && !isPartOfName(string[index + subString.length])))
        ) {
            ++index
            continue
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