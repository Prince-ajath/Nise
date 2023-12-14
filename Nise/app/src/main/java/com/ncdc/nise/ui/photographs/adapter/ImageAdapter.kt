package com.ncdc.nise.ui.photographs.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ncdc.nise.databinding.CustomItemLayoutBinding
import java.io.File


class ImageAdapter(var context: Context, var pictureList:ArrayList<File>):RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {


   /* fun setRecords(newUsers: ArrayList<Uri>) {
        pictureList.clear()
        pictureList.addAll(newUsers)


    }*/

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val binding = CustomItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    inner class MyViewHolder(private val binding: CustomItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: File) {
            Log.d("Records",">>>>>"+record.absolutePath)
            Glide.with(context).load(record.absolutePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(binding.imgUploadCompletedFront)

        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("Records",">>>>>"+pictureList.size)
        holder.bind(pictureList[position])

    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount()= pictureList.size


}