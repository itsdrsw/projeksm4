package com.example.cantaraapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cantaraapps.data.ImageDataBanner
import com.example.cantaraapps.databinding.ItemSlideBannerBinding

class ImageAdapterSlider(
    private val context: Context,
    private val items: List<ImageDataBanner>) :
    RecyclerView.Adapter<ImageAdapterSlider.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemSlideBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ImageDataBanner) {
            val drawableId = data.imageDrawableId
            val drawable = ContextCompat.getDrawable(context, drawableId)
            binding.ivvSlider.setImageDrawable(drawable)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ItemSlideBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }
}