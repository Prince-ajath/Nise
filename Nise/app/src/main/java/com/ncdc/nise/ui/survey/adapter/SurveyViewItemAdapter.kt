package com.ncdc.nise.ui.survey.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncdc.nise.MainActivity
import com.ncdc.nise.databinding.ItemSurveyDetailsBinding
import com.ncdc.nise.interfaces.OnClickListener
import com.ncdc.nise.ui.load.presentation.LoadDetailsActivity
import com.ncdc.nise.ui.photographs.PhotographsActvity
import com.ncdc.nise.ui.survey.model.SurveyDetailsItem
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference


class SurveyViewItemAdapter(private var context: Context, private var listSurveyDetails: ArrayList<SurveyDetailsItem>,var listner:OnClickListener): RecyclerView.Adapter<SurveyViewItemAdapter.MyViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = listSurveyDetails.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setRecords(
        newUsers: ArrayList<SurveyDetailsItem>,

        ) {
        listSurveyDetails.clear()
        listSurveyDetails.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val binding = ItemSurveyDetailsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    inner class MyViewHolder(private val binding: ItemSurveyDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: SurveyDetailsItem) {
            var userId:Int=Integer.parseInt(record.surveyId)
            binding.surveyId.text = record.surveyId
            binding.hfName.text = record.hfName

            var isEdit:Int=record.status
            if(isEdit==1){
                binding.hfStatus.text = "COMPLETED"
                binding.rlBottom.visibility=View.GONE
                binding.tvUpload.visibility=View.GONE
                binding.bottomView.visibility=View.GONE
                binding.paymentLayout.setCardBackgroundColor(Color.parseColor("#5aecab"))
            }else{
                binding.hfStatus.text = "PROGRESS"
                binding.rlBottom.visibility=View.VISIBLE
                binding.tvUpload.visibility=View.VISIBLE
                binding.bottomView.visibility=View.VISIBLE
                binding.paymentLayout.setCardBackgroundColor(Color.parseColor("#ffba52"))
            }
            binding.addLeadsFAB.setOnClickListener{
                context.startActivity(Intent(context, LoadDetailsActivity::class.java).putExtra("surveyId",record.surveyId))
                SharePreference.setIntPref(context,Constants.editSurveyId,userId)

            }
            binding.tvUpload.setOnClickListener {
                context.startActivity(Intent(context, PhotographsActvity::class.java).putExtra("surveyId",record.surveyId))
                SharePreference.setIntPref(context,Constants.editSurveyId,userId)
            }
            binding.surveyEdit.setOnClickListener {
                SharePreference.setIntPref(context,Constants.editSurveyId,userId)
                SharePreference.setBooleanPref(context,Constants.isEdit,true)
                val intent= Intent(context, MainActivity::class.java)
                intent.putExtra("surveyId",record.surveyId)
                intent.putExtra("isEdit",true)
                context.startActivity(intent)

            }
            binding.surveySubmit.setOnClickListener {
                listner.onUpdate(Integer.parseInt(record.surveyId))

            }


        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listSurveyDetails[position])
    }
}