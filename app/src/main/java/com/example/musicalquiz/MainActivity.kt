package com.example.musicalquiz
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.musicalquiz.R
import com.example.musicalquiz.databinding.ActivityMainBinding
import com.example.musicalquiz.ui.playlist.PlaylistFragment
import com.example.musicalquiz.ui.quiz.QuizFragment
import com.example.musicalquiz.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(SearchFragment())
        val bottomNav: BottomNavigationView = binding.bottomNav
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_search -> {
                    loadFragment(SearchFragment())
                    true
                }
                R.id.navigation_playlist -> {
                    loadFragment(PlaylistFragment())
                    true
                }
                R.id.navigation_quiz -> {
                    loadFragment(QuizFragment())
                    true
                }
                else -> false
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}