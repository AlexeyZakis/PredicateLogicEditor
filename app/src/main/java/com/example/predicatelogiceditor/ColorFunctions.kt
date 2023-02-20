package com.example.predicatelogiceditor

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

fun getColorText(string: SpannableStringBuilder, color: Int, start: Int, end: Int): SpannableStringBuilder {
    val text = SpannableStringBuilder(string)
    val style = ForegroundColorSpan(color)
    text.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    return text
}