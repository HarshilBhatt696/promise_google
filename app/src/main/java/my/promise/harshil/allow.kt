//package com.example.tipster
//
//import android.os.Bundle
//import android.os.PersistableBundle
//import android.support.design.widget.BottomNavigationView
//import android.support.v7.app.AppCompatActivity
//import android.widget.TextView
//import kotlinx.android.synthetic.main.MatchMake.*
//
//class allow : AppCompatActivity() {
//
//    var NoOfPeopleMatched  = ArrayList<String>()// Usernames of people matched
//
//
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        setContentView(R.layout.MatchMake)
//
//
//        MatchMake.setOnClickListener {
//            MatchMakeClicker()
//        }
//
//
//    }
//
//
//    fun MatchMakeClicker() {
//
//        NoOfPeopleMatched = Datings.GetNumberOfPeopleMatched(3) // Change the number of people got
//
//    }
//
//
//
//}
