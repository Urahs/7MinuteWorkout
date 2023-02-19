package com.example.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore.Audio.Media
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import com.example.a7minuteworkout.databinding.ActivityExerciseBinding
import com.example.a7minuteworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityExerciseBinding
    private var timer: CountDownTimer? = null
    private var timerProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePos = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    val restTime = 10
    val exerciseTime = 30
    var resting = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding.toolBarExercise)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolBarExercise.setNavigationOnClickListener{
        customDialogForBackButton()
        //onBackPressed()
        }

        exerciseList = Constants.defaultExerciseList()
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding.exerciseStatus.adapter = exerciseAdapter

        tts = TextToSpeech(this, this, "")
        createRestSound()
        setUpProgressView()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun setUpProgressView(){

        var countDownValue = 0

        fun setUp(time: Int, titleText: String){
            binding.tvTitle.text = titleText
            binding.progressBar.max = time
            binding.progressBar.progress = time
            binding.timerTV.text = time.toString()
            countDownValue = time
            binding.imageIV.visibility = if(resting) View.INVISIBLE else View.VISIBLE
            binding.upcomingExerciseTV.visibility = if(resting) View.VISIBLE else View.INVISIBLE
        }

        if(resting){
            try {
                if(currentExercisePos >= 0){
                    exerciseList!![currentExercisePos].setIsSelected(false)
                    exerciseList!![currentExercisePos].setIsCompleted(true)

                    exerciseAdapter!!.notifyDataSetChanged()
                }

                currentExercisePos++
                binding.upcomingExerciseTV.text = "Upcoming Exercise:\n" + exerciseList!![currentExercisePos].getName()

                player?.start()
                speakOut("Please rest for 10 seconds")
                setUp(restTime, "Get Ready!")

            } catch (e:java.lang.Exception){
                startActivity(Intent(this, FinishActivity::class.java))
                finish()
            }
        }
        else{
            exerciseList!![currentExercisePos].setIsSelected(true)
            exerciseAdapter!!.notifyDataSetChanged()

            speakOut(exerciseList!![currentExercisePos].getName())
            setUp(exerciseTime, exerciseList!![currentExercisePos].getName())
            binding.imageIV.setImageResource(exerciseList!![currentExercisePos].getImage())
        }


        timer?.cancel()
        timerProgress = 0


        setProgressBar(countDownValue)
    }

    private fun createRestSound(){
        try {
            val soundURI = Uri.parse("android.resource://com.example.a7minuteworkout/" + R.raw.app_src_main_res_raw_press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
        } catch (e: Exception){}
    }

    private fun setProgressBar(countDownValue: Int){
        binding.progressBar.progress = timerProgress

        timer = object: CountDownTimer(countDownValue * 1000L, 1000){

            override fun onTick(p0: Long) {
                timerProgress++
                binding.progressBar.progress = countDownValue - timerProgress
                binding.timerTV.text = (countDownValue - timerProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePos < exerciseList!!.size){
                    resting = !resting
                    setUpProgressView()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timerProgress = 0

        tts!!.stop()
        tts!!.shutdown()

        player!!.stop()
        player = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            tts!!.setLanguage(Locale.ENGLISH)
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}