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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.predicatelogiceditor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    var cursorLastPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.title = ""
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(TOOLBAR_BGC))

        var textLength = binding.textField.text.toString().length

        val constantsList = arrayListOf<String>("Ali")
        val predicatesList = arrayListOf<String>("isTime", "isGood")
        val functionsList = arrayListOf<String>("getBest")

        val logicalConnectivesList = arrayListOf(AND_SYMBOL, OR_SYMBOL, NOT_SYMBOL, IMPLICATION_SYMBOL)
        val quantifierList = arrayListOf(ANY_SYMBOL, EXIST_SYMBOL)

        binding.textField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.textField.text.toString().length != textLength) {
                    var text = SpannableStringBuilder(binding.textField.text.toString())

                    text = getTextWithColoredWrongBrackets(text)
                    text = getTextWithColoredCorrectBrackets(text)

                    text = getTextWithColoredElements(text, constantsList, CONSTANTS_COLOR)
                    text = getTextWithColoredElements(text, predicatesList, PREDICATES_COLOR)
                    text = getTextWithColoredElements(text, functionsList, FUNCTIONS_COLOR)

                    text = getTextWithColoredElements(text, logicalConnectivesList, LOGICAL_CONNECTIVES_COLOR, false)
                    text = getTextWithColoredElements(text, quantifierList, QUANTIFIERS_COLOR, false)

                    cursorLastPos = binding.textField.getSelectionStart()
                    textLength = binding.textField.text.toString().length

                    binding.textField.setText(text)
                    binding.textField.setSelection(cursorLastPos)
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        binding.constantsBtn.setOnClickListener {
            changeVisibility(binding.constantsLayout)
        }
        binding.predicatesBtn.setOnClickListener {
            changeVisibility(binding.predicatesLayout)
        }
        binding.functionsBtn.setOnClickListener {
            changeVisibility(binding.functionsLayout)
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
}