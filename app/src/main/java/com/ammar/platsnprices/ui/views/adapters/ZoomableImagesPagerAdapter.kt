package com.ammar.platsnprices.ui.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ammar.platsnprices.ui.views.ZoomableImageView

class ZoomableImagesPagerAdapter(
    private val urls: List<String>,
) : RecyclerView.Adapter<ZoomableImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ZoomableImageViewHolder(ZoomableImageView(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    })

    override fun onBindViewHolder(holder: ZoomableImageViewHolder, position: Int) = holder.bind(urls[position])

    override fun getItemCount(): Int = urls.size
}

class ZoomableImageViewHolder internal constructor(
    private val imageView: ZoomableImageView
) : RecyclerView.ViewHolder(imageView.rootView) {
    internal fun bind(url: String) {
        imageView.load(url)
    }
}