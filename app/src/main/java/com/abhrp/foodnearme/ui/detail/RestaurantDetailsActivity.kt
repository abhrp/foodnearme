package com.abhrp.foodnearme.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.di.factory.ViewModelFactory
import com.abhrp.foodnearme.presentation.state.ResourceState
import com.abhrp.foodnearme.presentation.viewmodel.detail.RestaurantDetailsViewModel
import com.abhrp.foodnearme.ui.base.BaseActivity
import com.abhrp.foodnearme.util.logging.AppLogger
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import javax.inject.Inject

class RestaurantDetailsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var restaurantDetailsViewModel: RestaurantDetailsViewModel

    @Inject
    lateinit var logger: AppLogger

    private var id: String? = null

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
        setIntentExtras()
        setContentView(R.layout.activity_restaurant_details)
        setSupportActionBar(toolbar)

        restaurantDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantDetailsViewModel::class.java)
        observeRestaurantDetails()
        fetchRestaurantDetails(id)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun observeRestaurantDetails() {
        restaurantDetailsViewModel.observerRestuarantDetails().observe(this, Observer { resource ->
            when(resource.state) {
                ResourceState.LOADING -> {}
                ResourceState.SUCCESS -> {}
                ResourceState.ERROR -> {}
            }
        })
    }

    private fun fetchRestaurantDetails(id: String?) {
        if (id != null) {
            restaurantDetailsViewModel.fetchRestaurantDetails(id)
        }
    }

    private fun setIntentExtras() {
        id = intent.getStringExtra(ID)
    }

    override fun online() {

    }

    override fun offline() {

    }
}
