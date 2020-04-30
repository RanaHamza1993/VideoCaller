package com.havelisolutions.videocaller.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.havelisolutions.videocaller.R

fun provideRequestOptions(): RequestOptions {
    return RequestOptions.placeholderOf(R.drawable.white_background)
        .error(R.drawable.white_background)
}
@BindingAdapter("image")
fun loadImage(view: ImageView,url:String?){
    Glide.with(view)
        .setDefaultRequestOptions(provideRequestOptions())
        .load(url)
        .into(view)
}
@BindingAdapter("drawable")
fun loadImage(view: ImageView,resource:Int?){
    Glide.with(view)
        .setDefaultRequestOptions(provideRequestOptions())
        .load(resource)
        .into(view)

}