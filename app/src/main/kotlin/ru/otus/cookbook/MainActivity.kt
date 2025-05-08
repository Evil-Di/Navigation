package ru.otus.cookbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.otus.cookbook.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import androidx.activity.addCallback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this) {
            if (!findNavController(R.id.fragment_container_view).popBackStack()) {
                finish()
            }
        }
    }
}