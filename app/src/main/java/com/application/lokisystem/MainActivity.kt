package com.application.lokisystem

import android.app.ActionBar
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.fragment.app.Fragment
import com.application.lokisystem.databinding.ActivityMainBinding
import com.application.lokisystem.fragment.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var navigationItemSelectFlag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(0)

        binding.bottomNavigationView.setOnItemSelectedListener {
            if(it.itemId == R.id.navigation_model && navigationItemSelectFlag != 0) {
                replaceFragment(0)
            } else if(it.itemId == R.id.navigation_theme && navigationItemSelectFlag != 1) {
                replaceFragment(1)
            } else if(it.itemId == R.id.navigation_search && navigationItemSelectFlag != 2) {
                replaceFragment(2)
            }
            true
        }
    }

    private fun replaceFragment(flag : Int){
        supportActionBar?.apply {
            customView = actionBarCustomView(flag)
            displayOptions = DISPLAY_SHOW_CUSTOM
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF000000")))
        }
        navigationItemSelectFlag = when (flag) {
            1 -> {replaceFragment(Category())
                1
            }
            2 -> {replaceFragment(Search())
                2
            }
            else -> {replaceFragment(Model())
                0
            }
        }

    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun actionBarCustomView(flag : Int): TextView {
        return TextView(binding.root.context).apply {
            // Action bar title text
            text = when (flag) {
                1 -> {
                    "Category"
                }
                2 -> {
                    "Search"
                }
                else -> {
                    "Model"
                }
            }

            // Initialize layout parameters for text view
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )

            // Apply layout params for text view
            layoutParams = params

            // Center align the text
            gravity = Gravity.CENTER

            // Title text appearance
            setTextAppearance(
                android.R.style
                    .TextAppearance_Material_Widget_ActionBar_Title
            )

            // Action bar title text color
            setTextColor(Color.WHITE)

            // Bold title text
             setTypeface(typeface,Typeface.BOLD)
        }
    }

}