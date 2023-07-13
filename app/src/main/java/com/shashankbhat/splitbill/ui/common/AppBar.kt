package com.shashankbhat.splitbill.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.shashankbhat.splitbill.R

class AppBar : LinearLayout {

    lateinit var appBarText: AppCompatTextView
    lateinit var backButton: AppCompatImageView
    private var parentConstraint: ConstraintLayout? = null
    private lateinit var menu: LinearLayout

    var margin = 24

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) {

//        val attributes = context.obtainStyledAttributes(attrs, R.styleable.AppBar,
//            defStyle, 0)
//        titleText = attributes.getString(R.styleable.AppBar_title_text)
//        backEnabled = attributes.getBoolean(R.styleable.AppBar_back_enabled, false)
//        attributes?.recycle()

        parentConstraint = ConstraintLayout(context)
        appBarText = AppCompatTextView(context)
        backButton = AppCompatImageView(context)
        menu = LinearLayout(context)

        addConstraintLayout()
        addNavigationBackUi()
        addTitleText()
        addMenuLayout()
    }

    private fun addConstraintLayout() {
        this.addView(parentConstraint)

        parentConstraint?.id = ViewCompat.generateViewId()
        parentConstraint?.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        parentConstraint?.setPadding(10, 15, 10, 15)
    }

    private fun addTitleText() {
        parentConstraint?.addView(appBarText)

        appBarText.setTextAppearance(context, R.style.TextStyleHeadMediumBold)
        appBarText.setTextColor(ContextCompat.getColor(context, R.color.app_title_text_color))
        appBarText.id = ViewCompat.generateViewId()
        appBarText.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        val constrainSet = ConstraintSet()
        constrainSet.clone(parentConstraint)

        constrainSet.constrainWidth(appBarText.id, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.constrainHeight(appBarText.id, ConstraintSet.WRAP_CONTENT)

        constrainSet.connect(
            appBarText.id,
            ConstraintSet.START,
            backButton.id,
            ConstraintSet.END,
            margin
        )
        constrainSet.connect(
            appBarText.id,
            ConstraintSet.TOP,
            parentConstraint?.id ?: -1,
            ConstraintSet.TOP
        )
        constrainSet.connect(
            appBarText.id,
            ConstraintSet.BOTTOM,
            parentConstraint?.id ?: -1,
            ConstraintSet.BOTTOM
        )
        constrainSet.connect(
            appBarText.id,
            ConstraintSet.END,
            menu.id,
            ConstraintSet.START
        )

        constrainSet.applyTo(parentConstraint)
    }

    private fun addNavigationBackUi() {
        parentConstraint?.addView(backButton)

        backButton.id = ViewCompat.generateViewId()

        val backIconDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_back)!!
        val drawableTint = DrawableCompat.wrap(backIconDrawable).mutate()
        DrawableCompat.setTint(
            drawableTint,
            ContextCompat.getColor(context, R.color.app_title_text_color)
        )
        backButton.setImageDrawable(drawableTint)
//        navigationCard.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

        val constrainSet = ConstraintSet()
        constrainSet.clone(parentConstraint)

        constrainSet.connect(
            backButton.id,
            ConstraintSet.START,
            parentConstraint?.id ?: -1,
            ConstraintSet.START,
            margin
        )
        constrainSet.connect(
            backButton.id,
            ConstraintSet.TOP,
            parentConstraint?.id ?: -1,
            ConstraintSet.TOP
        )
        constrainSet.connect(
            backButton.id,
            ConstraintSet.BOTTOM,
            parentConstraint?.id ?: -1,
            ConstraintSet.BOTTOM
        )

        constrainSet.applyTo(parentConstraint)

    }

    private fun addMenuLayout(){
        parentConstraint?.addView(menu)

        menu.id = ViewCompat.generateViewId()
        menu.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

        val constrainSet = ConstraintSet()
        constrainSet.clone(parentConstraint)

        constrainSet.connect(
            menu.id,
            ConstraintSet.TOP,
            appBarText.id,
            ConstraintSet.TOP
        )
        constrainSet.connect(
            menu.id,
            ConstraintSet.BOTTOM,
            appBarText.id,
            ConstraintSet.BOTTOM
        )
        constrainSet.connect(
            menu.id,
            ConstraintSet.END,
            parentConstraint?.id ?: -1,
            ConstraintSet.END,
            margin
        )

        constrainSet.applyTo(parentConstraint)
    }

    fun back(): AppCompatImageView {
        return backButton
    }

    fun setBackButtonEnabled(isEnabled: Boolean) {
        if (isEnabled)
            backButton.visibility = View.VISIBLE
        else backButton.visibility = View.GONE
    }

    fun setTitle(title: String){
        appBarText.text = title
    }


    fun addMenuItem(menuItem : View){
        menu.addView(menuItem)
    }

    fun clearMenu() {
        menu.removeAllViews()
    }


}