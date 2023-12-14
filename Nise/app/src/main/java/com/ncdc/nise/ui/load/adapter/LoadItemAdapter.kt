package com.ncdc.nise.ui.load.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.ShowLoaditemBinding
import com.ncdc.nise.interfaces.HomeListener
import com.ncdc.nise.ui.load.model.load.LoadItem
import java.lang.NumberFormatException


class LoadItemAdapter(private var records: ArrayList<LoadItem>,var listner:HomeListener) :
    RecyclerView.Adapter<LoadItemAdapter.MyViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = records.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setRecords(
        newUsers: ArrayList<LoadItem>) {
        records.clear()
        records.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val binding = ShowLoaditemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    inner class MyViewHolder(private val binding: ShowLoaditemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: LoadItem) {
            binding.equipmentName.text = record.equipment
            binding.quantity.text = record.quantity
            binding.useHours.text = record.hoursUse
            binding.load.text=record.wattage
            binding.totalLoad.text=record.totalload
            binding.LoadConsumption.text=record.loadconsumption
            binding.deleteLead.setOnClickListener {
                listner.deleteItem(Integer.parseInt(record.id))

            }

            try {
                var totalPrice:Int = 0
                var totalLoad:String=""

                for (i in 0 until records.size) {
                    totalLoad=records.get(i).totalload
                    val num = totalLoad.toInt()
                    totalPrice += num
                    Log.d("Total",">>>>"+totalPrice)
                }
            }catch (e:NumberFormatException){

            }
            try {
                var totalConsumption:Int = 0
                var totlConsumtion:String=""

                for (i in 0 until records.size) {
                    totlConsumtion=records.get(i).totalload
                    val numb = totlConsumtion.toInt()
                    totalConsumption += numb
                    Log.d("Total",">>>>"+totalConsumption)
                }
            }catch (e:NumberFormatException){

            }



        }
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(records[position])
    }
}