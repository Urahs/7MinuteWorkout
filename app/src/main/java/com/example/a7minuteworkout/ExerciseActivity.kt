package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle
import com.example.a7minuteworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding.toolBarExercise)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolBarExercise.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}