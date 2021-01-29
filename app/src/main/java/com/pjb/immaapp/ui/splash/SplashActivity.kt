package com.pjb.immaapp.ui.splash

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.pjb.immaapp.MainActivity
import com.pjb.immaapp.R
import java.lang.ref.WeakReference

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

        val task = MyAsyncTask(this)
        task.execute()

    }

    companion object {
        class MyAsyncTask internal constructor(context: SplashActivity): AsyncTask<Void, Void, Void>() {

            private val activityReference : WeakReference<SplashActivity> = WeakReference(context)

            override fun onPreExecute() {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                Log.d("pre", "Berhasil")
            }

            override fun onPostExecute(result: Void?) {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                Log.d("post", "Berhasil")
                val intent = Intent(activity , MainActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }

            override fun doInBackground(vararg p0: Void?) : Void? {
                try{
                    Thread.sleep(3000)
                }catch (e: Exception){
                    Log.d("thread", e.message.toString())
                }

                return null
            }
        }
    }
}