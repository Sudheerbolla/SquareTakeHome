package com.squaretakehome.utils.views

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.squaretakehome.BaseApplication
import com.squaretakehome.R
import com.squaretakehome.models.EmployeeModel

object BindingAdapterUtils {

    /**
     *  For loading image into imageview from URL using Glide/Custom approach
     */
    @JvmStatic
    @BindingAdapter("app:imageUrlItem")
    fun bindImage(
        imageView: AppCompatImageView,
        imageModel: EmployeeModel?
    ) {
//        Check and load image from Url into our imageview
        imageModel?.photoUrlSmall?.let {
            if (BaseApplication.useGlide!!) {
                Glide.with(imageView.context).load(it).centerCrop()
                    .transform(RoundedCorners(15))
                    .error(R.drawable.ic_image_error).placeholder(R.drawable.ic_image_loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(imageView)
            } else {
                BaseApplication.imageLoader?.displayImage(it, true, imageView)
            }
        }
    }

}