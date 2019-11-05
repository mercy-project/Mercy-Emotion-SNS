package com.content.mercy

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.content.mercy.main.fragment.CalenderFragment
import com.content.mercy.main.fragment.FriendFragment
import com.content.mercy.main.fragment.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    private var backKeyPressedTime: Long = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation = findViewById<BottomNavigationView>(R.id.navigationView)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = MainFragment()
        addFragment(fragment)
        // ActionBar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu_24px)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.app_name)
            setDisplayShowTitleEnabled(true)
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home_item -> {
                val fragment = MainFragment()
                addFragment(fragment)

                return@OnNavigationItemSelectedListener true
            }
            R.id.friend_item -> {
                val fragment = FriendFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.calener_item -> {
                val fragment = CalenderFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //android.R.id.home -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onBackPressed() {
        val delay = 500
        when {
            System.currentTimeMillis() > backKeyPressedTime + delay -> {
                backKeyPressedTime = System.currentTimeMillis()
                return
            }
            System.currentTimeMillis() <= backKeyPressedTime + delay -> {
                this.finish()
            }
        }
    }
}
