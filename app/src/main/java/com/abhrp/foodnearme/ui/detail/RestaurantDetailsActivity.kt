package com.abhrp.foodnearme.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.di.factory.ViewModelFactory
import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.detail.RestaurantTiming
import com.abhrp.foodnearme.presentation.state.ResourceState
import com.abhrp.foodnearme.presentation.viewmodel.detail.RestaurantDetailsViewModel
import com.abhrp.foodnearme.ui.base.BaseActivity
import com.abhrp.foodnearme.util.DisplayMetricsHelper
import com.abhrp.foodnearme.util.logging.AppLogger
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import kotlinx.android.synthetic.main.content_detail.*
import javax.inject.Inject


class RestaurantDetailsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var restaurantDetailsViewModel: RestaurantDetailsViewModel

    @Inject
    lateinit var displayMetricsHelper: DisplayMetricsHelper

    @Inject
    lateinit var logger: AppLogger

    private var id: String? = null
    private var socialInfoCount = 0

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
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = ""
        }
        restaurantDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantDetailsViewModel::class.java)
        observeRestaurantDetails()
        fetchRestaurantDetails(id)
    }

    private fun observeRestaurantDetails() {
        restaurantDetailsViewModel.observerRestuarantDetails().observe(this, Observer { resource ->
            when(resource.state) {
                ResourceState.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                ResourceState.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    val data = resource.data
                    setupDetailsUI(data)
                }
                ResourceState.ERROR -> {
                    progressBar.visibility = View.GONE
                    showError(resource.error)
                }
            }
        })
    }

    private fun setupDetailsUI(data: RestaurantDetails?) {
        if (data != null) {
            val imageUrl = data.imageUrl
            setHeaderImage(imageUrl)
            restaurantName.text = data.name
            cuisine.text = data.categories
            address.text = data.address
            setOpenStatus(data.isOpenNow ?: false)
            setLikesCount(data.noOfLikes)
            setRating(data.rating, data.ratingColor, data.totalRatings)
            setPricing(data.affordability)
            setUpMenuLink(data.menuUrl)
            setupFormattedPhone(data.formattedPhone)
            setUpTimings(data.timeZone, data.currentStatus, data.timings)
            setUpSocialBar(data.phone, data.twitter, data.facebook, data.latitude, data.longitude)
        }
    }

    private fun setOpenStatus(isOpen: Boolean) {
        if (isOpen) {
            openStatus.text = getString(R.string.open_now)
            openStatus.setTextColor(resources.getColor(R.color.colorOpenNow))
        } else {
            openStatus.text = getString(R.string.closed_now)
            openStatus.setTextColor(resources.getColor(R.color.colorClosedNow))
        }
    }

    private fun setLikesCount(likes: Int?) {
        likesCount.text = getString(R.string.likes_count, likes ?: 0)
    }

    private fun setRating(rating: Double?, ratingColor: String?, totalRating: Int?) {
        if (rating != null && totalRating != null) {
            ratings.text = rating.toString()
            ratings.setTextColor(Color.parseColor("#${ratingColor ?: "000000"}"))
            totalRatings.text = getString(R.string.ratings_count, totalRating)
        } else {
            ratings.text = "0"
            ratings.setTextColor(resources.getColor(R.color.colorPrimaryText))
            totalRatings.text = getString(R.string.ratings_count, 0)
        }
    }

    private fun setPricing(pricing: String?) {
        affordability.text = getString(R.string.pricing, pricing)
    }

    private fun setUpMenuLink(link: String?) {
        if (link != null) {
            viewMenu.paintFlags = viewMenu.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            viewMenu.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        } else {
            viewMenu.visibility = View.GONE
            viewMenu.isEnabled = false
        }
    }

    private fun setupFormattedPhone(phone: String?) {
        if (phone != null) {
            phoneText.text = getString(R.string.formatted_phone, phone)
        } else {
            phoneText.visibility = View.GONE
        }
    }

    private fun setUpTimings(timeZone: String?, currentStatus: String?, timings: List<RestaurantTiming>?) {
        timingHeader.text = getString(R.string.timings, timeZone)
        if (currentStatus != null) {
            todayTiming.text = getString(R.string.today_timing, currentStatus)
        } else {
            todayTiming.visibility = View.GONE
        }

        setupDailyTimings(timings)
    }

    private fun setupDailyTimings(timings: List<RestaurantTiming>?) {
        if (timings != null && timings.isNotEmpty()) {
            timings.forEach {
                val timingString = "${it.days}: ${it.timing}"
                val textView =
                    layoutInflater.inflate(R.layout.layout_timing_item, null) as TextView
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.bottomMargin = displayMetricsHelper.pixelFromDp(8)
                textView.layoutParams = layoutParams
                textView.text = timingString
                dailyTimingsContainer.addView(textView)
            }
        } else {
            timingHeader.visibility = View.GONE
            dailyTimingsContainer.visibility = View.GONE
        }
    }

    private fun setUpSocialBar(phone: String?, twitter: String?, facebook: String?, latitude: Double?, longitude: Double?) {
        setUpPhoneCallIcon(phone)

        setUpTwitterIcon(twitter)

        setUpFacebookIcon(facebook)

        setUpGoogleMapDirection(latitude, longitude)

        if(socialInfoCount == 0) {
            socialBar.visibility = View.GONE
        }
    }

    private fun setUpPhoneCallIcon(phone: String?) {
        if (phone != null) {
            socialInfoCount++
            phoneCallIcon.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            }
        } else {
            phoneCallIcon.visibility = View.GONE
        }
    }

    private fun setUpTwitterIcon(twitter: String?) {
        if (twitter != null) {
            socialInfoCount++
            twitterIcon.setOnClickListener {
                try {
                    packageManager.getPackageInfo("com.twitter.android", 0)
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=$twitter"))
                    startActivity(intent)
                } catch (e: Exception) {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/$twitter"))
                    startActivity(intent)
                }
            }
        } else {
            twitterIcon.visibility = View.GONE
        }
    }

    private fun setUpFacebookIcon(facebook: String?) {
        if (facebook != null) {
            socialInfoCount++
            facebookIcon.setOnClickListener {
                try {
                    packageManager.getPackageInfo("com.facebook.katana", 0)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/${facebook}"))
                    startActivity(intent)
                } catch (e: Exception) {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/$facebook"))
                    startActivity(intent)
                }
            }
        } else {
            facebookIcon.visibility = View.GONE
        }
    }

    private fun setUpGoogleMapDirection(latitude: Double?, longitude: Double?) {
        if (latitude != null && longitude != null) {
            socialInfoCount++
            googleMapIcon.setOnClickListener {
                val gmmIntentUri: Uri? = Uri.parse("geo:$latitude,$longitude?z=17")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                }
            }
        } else {
            googleMapIcon.visibility = View.GONE
        }
    }

    @Suppress("DEPRECATION")
    private fun setHeaderImage(imageUrl: String?) {
        if(imageUrl != null && imageUrl.isNotEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(ColorFilterTransformation(resources.getColor(R.color.colorImage)))
                .into(headerImage)
        }
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
        dismissOfflineSnackBar()
    }

    override fun offline() {
        showOffLineSnackBar(detailsContainer)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
