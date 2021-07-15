package ka.el.teleclone.utils

import android.media.MediaPlayer
import ka.el.teleclone.database.getFileFromStorage
import java.io.File

class AppVoicePlayer {
    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File

    fun init() {
        mMediaPlayer = MediaPlayer()
    }

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {
        mFile = File(APP_ACTIVITY.filesDir, messageKey)
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            startPlay { function() }
        } else {
            mFile.createNewFile()
            getFileFromStorage(mFile, fileUrl) {
                startPlay { function() }
            }
        }

    }

    private fun startPlay(function: () -> Unit) {
        mMediaPlayer.setDataSource(mFile.absolutePath)
        mMediaPlayer.prepare()
        mMediaPlayer.start()
        mMediaPlayer.setOnCompletionListener {
            stop { function() }
        }

    }

    fun stop(function: () -> Unit) {
        mMediaPlayer.stop()
        mMediaPlayer.reset()
        function()
    }

    fun release() {
        mMediaPlayer.release()
    }
}