package com.tjcoding.funtimer.service.alarm

import android.app.Service
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager



internal object AlarmHorn : MediaPlayer.OnPreparedListener {
    private val VIBRATE_PATTERN = longArrayOf(500, 500)
    private var sStarted = false
    private var mAudioManager: AudioManager? = null
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    fun stop(context: Context) {
        if (sStarted) {
            sStarted = false
            getMediaPlayer(context).stop()
            getVibrator(context).cancel()
        }
    }

    fun start(context: Context) {
        stop(context)
        vibrate(getVibrator(context))
        playRingtone(getMediaPlayer(context), context)
        sStarted = true
    }

    private fun vibrate(vibrator: Vibrator) {

        val vibrationEffect = VibrationEffect.createWaveform(VIBRATE_PATTERN, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            vibrator.vibrate(
                vibrationEffect, VibrationAttributes.Builder()
                    .setUsage(VibrationAttributes.USAGE_ALARM)
                    .setFlags(VibrationAttributes.FLAG_BYPASS_INTERRUPTION_POLICY, 1)
                    .build()
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(
                VIBRATE_PATTERN,
                0,
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
        }
    }

    private fun getVibrator(context: Context): Vibrator {
        if(vibrator != null) return vibrator!!

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Service.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        }

        return vibrator!!
    }

    private fun getMediaPlayer(context: Context): MediaPlayer {
        if (mediaPlayer == null) {
            val alarmNoise = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, alarmNoise)
                prepareAsync()
            }
        } else {
            if(!mediaPlayer!!.isPlaying) mediaPlayer!!.prepareAsync()
        }

        return mediaPlayer as MediaPlayer
    }

    private fun playRingtone(mMediaPlayer: MediaPlayer, context: Context) {

        val alarmAudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        if (mAudioManager == null) {
            mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }

        mMediaPlayer.setAudioAttributes(alarmAudioAttributes)
        mMediaPlayer.isLooping = true
        mAudioManager!!.requestAudioFocus(
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(alarmAudioAttributes)
                .build()
        )
        mMediaPlayer.setOnPreparedListener(this)


    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }

}