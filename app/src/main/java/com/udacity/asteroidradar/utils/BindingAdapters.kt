package com.udacity.asteroidradar

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.AsteroidAdapter
import java.lang.Exception


@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: AppCompatImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: AppCompatTextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: AppCompatTextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: AppCompatTextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("picOfDay")
fun AppCompatImageView.bindAPOD(url:String?){
    url?.let {
        val imgUri = url.toUri().buildUpon().scheme("https").build()
        Picasso.get()
                .load(imgUri)
                .error(R.drawable.ic_connection_error)
                .into(this, object :Callback{
                    override fun onSuccess() {
                       //
                    }

                    override fun onError(e: Exception?) {
                        this@bindAPOD.contentDescription = context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
                    }

                })

    }
}

@BindingAdapter("hidePBar")
fun hideProgressBarIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("itemName")
fun AppCompatTextView.bindListItemName(obj:Asteroid){
    this.text = obj.codename
}

@BindingAdapter("itemDate")
fun AppCompatTextView.bindItemDateApproach(obj:Asteroid){
    this.text = obj.closeApproachDate
}

@BindingAdapter("itemIcon")
fun AppCompatImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        this.setImageResource(R.drawable.ic_status_potentially_hazardous)
        this.contentDescription = context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        this.setImageResource(R.drawable.ic_status_normal)
        this.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astList")
fun RecyclerView.bindRecyclerView(data:List<Asteroid>?){
    val adapter = this.adapter as AsteroidAdapter
    adapter.submitList(data)
}
