package com.abhrp.foodnearme.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_restaurant_details.*

class RestaurantDetailsActivity : BaseActivity() {

    companion object {
        const val ID = "id"
        fun newInstance(context: Context, restaurantId: String): Intent {
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            intent.putExtra(ID, restaurantId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun online() {

    }

    override fun offline() {

    }
}
