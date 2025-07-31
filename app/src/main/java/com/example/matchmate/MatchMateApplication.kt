package com.example.matchmate

import android.app.Application
import com.example.matchmate.data.local.UserDatabase

class MatchMateApplication : Application() {
    companion object {
        lateinit var database: UserDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = UserDatabase.getInstance(this)
    }
}
