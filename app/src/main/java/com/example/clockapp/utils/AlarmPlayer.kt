package com.example.clockapp.utils


import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import javax.inject.Singleton

/**
 * Simple helper to play/stop an alarm sound.
 * This implementation is NOT annotated with @Inject so it matches
 * your MapModule.provideAlarmPlayer(...) provider.
 */
class AlarmPlayer(private val context: Context) {

    private var ringtone: Ringtone? = null

    fun play() {
        try {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ringtone = RingtoneManager.getRingtone(context.applicationContext, uri)
            ringtone?.play()
        } catch (t: Throwable) {
            // fallback or log
            t.printStackTrace()
        }
    }

    fun stop() {
        try {
            ringtone?.stop()
            ringtone = null
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun isPlaying(): Boolean = ringtone?.isPlaying ?: false
}