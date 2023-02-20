package com.example.predicatelogiceditor

import android.graphics.Color

val CONSTANTS_COLOR = Color.rgb(140,115,162)
val PREDICATES_COLOR = Color.rgb(247,194,108)
val FUNCTIONS_COLOR = Color.rgb(247,194,108)

val VARIABLES_COLOR = Color.rgb(91,138,180)

val LOGICAL_CONNECTIVES_COLOR = Color.rgb(202,120,50)
val QUANTIFIERS_COLOR = Color.rgb(202,120,50)

val WRONG_BRACKETS_COLOR = Color.rgb(206, 50, 38)
val BRACKETS_COLORS = arrayOf(
    Color.rgb(255, 255, 255),
    Color.rgb(47, 235, 210),
    Color.rgb(153, 51, 255),
)

val TOOLBAR_BGC = Color.rgb(47, 47, 47)

const val OPEN_BRACKET_SYMBOL = "("
const val CLOSE_BRACKET_SYMBOL = ")"

const val AND_SYMBOL = "&"
const val OR_SYMBOL = "|"
const val NOT_SYMBOL = "¬"
const val IMPLICATION_SYMBOL = "→"

const val ANY_SYMBOL = "∀"
const val EXIST_SYMBOL = "∃"

val RESERVED_CHARACTERS = arrayOf(
    OPEN_BRACKET_SYMBOL,
    CLOSE_BRACKET_SYMBOL,
    AND_SYMBOL,
    OR_SYMBOL,
    NOT_SYMBOL,
    IMPLICATION_SYMBOL,
    ANY_SYMBOL,
    EXIST_SYMBOL,
    ",",
    ".",
    ";",
    " ",
)