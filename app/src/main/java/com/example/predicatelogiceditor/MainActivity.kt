package com.example.predicatelogiceditor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.predicatelogiceditor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    var cursorLastPos: Int = 0

    val constantsList = arrayListOf<String>()
    val predicatesList = arrayListOf<String>()
    val functionsList = arrayListOf<String>()

    var variablesList = arrayListOf<Pair<Int, String>>()

    var textLength = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.title = ""
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(TOOLBAR_BGC))

        textLength = binding.textField.text.toString().length

        binding.textField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.textField.text.toString().length != textLength) {
                    updateTextColors()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        binding.constantsBtn.setOnClickListener {
            changeVisibility(binding.constantsLayout)
            if (binding.constantsLayout.visibility == View.GONE) {
                fillArray(binding.constantsList, constantsList)
                updateTextColors()
            }
        }
        binding.predicatesBtn.setOnClickListener {
            changeVisibility(binding.predicatesLayout)
            if (binding.constantsLayout.visibility == View.GONE) {
                fillArray(binding.predicatesList, predicatesList)
                updateTextColors()
            }
        }
        binding.functionsBtn.setOnClickListener {
            changeVisibility(binding.functionsLayout)
            if (binding.constantsLayout.visibility == View.GONE) {
                fillArray(binding.functionsList, functionsList)
                updateTextColors()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var text = binding.textField.text.toString()
        var cursorPos = binding.textField.getSelectionStart()

        when (item.itemId) {
            android.R.id.home -> {
                text = ""
                cursorPos = -1
            }
            R.id.any -> text = insertSubstring(text, ANY_SYMBOL, cursorPos)
            R.id.exist -> text = insertSubstring(text, EXIST_SYMBOL, cursorPos)
            R.id.and -> text = insertSubstring(text, AND_SYMBOL, cursorPos)
            R.id.or -> text = insertSubstring(text, OR_SYMBOL, cursorPos)
            R.id.not -> text = insertSubstring(text, NOT_SYMBOL, cursorPos)
            R.id.implication -> text = insertSubstring(text, IMPLICATION_SYMBOL, cursorPos)
            R.id.openBracket -> text = insertSubstring(text, OPEN_BRACKET_SYMBOL, cursorPos)
            R.id.closeBracket -> text = insertSubstring(text, CLOSE_BRACKET_SYMBOL, cursorPos)
        }
        binding.textField.setText(text)
        binding.textField.setSelection(cursorPos + 1)
        return true
    }

    fun changeVisibility(layout: ConstraintLayout) {
        if (layout.visibility == View.VISIBLE) {
            layout.visibility = View.GONE
        }
        else {
            layout.visibility = View.VISIBLE
        }
    }

    fun insertSubstring(string: String, subString: String, position: Int): String {
        if (position > string.length) {
            error("Insert position bigger then string length")
        }

        var newStr = ""
        var i = 0
        while (i != position) {
            newStr += string[i++]
        }
        subString.forEach {
            newStr += it
        }
        while (i != string.length) {
            newStr += string[i++]
        }
        return newStr
    }

    fun fillArray(edText: EditText, arr: ArrayList<String>) {
        var temp: String
        arr.clear()
        edText.text.split(",").forEach {
            temp = it.trim()
            if (temp.isNotEmpty() && temp !in arr) {
                arr.add(temp)
            }
        }
    }

    fun updateTextColors() {
        var text = SpannableStringBuilder(binding.textField.text.toString())

        text = getTextWithColoredWrongBrackets(text)
        text = getTextWithColoredCorrectBrackets(text)

        text = getTextWithColoredElements(text, constantsList, CONSTANTS_COLOR)
        text = getTextWithColoredElements(text, predicatesList, PREDICATES_COLOR)
        text = getTextWithColoredElements(text, functionsList, FUNCTIONS_COLOR)

        text = getTextWithColoredElements(text, logicalConnectivesList, LOGICAL_CONNECTIVES_COLOR, false)
        text = getTextWithColoredElements(text, quantifierList, QUANTIFIERS_COLOR, false)

        variablesList = getAllVariables(text.toString())

        text = getTextWithColoredElements(text, variablesList, VARIABLES_COLOR)

        text = getTextWithColoredWrongNames(text, WRONG_NAMES_COLOR, constantsList, predicatesList, functionsList, variablesList)

        cursorLastPos = binding.textField.getSelectionStart()
        textLength = binding.textField.text.toString().length

        binding.textField.setText(text)
        binding.textField.setSelection(cursorLastPos)
    }
}