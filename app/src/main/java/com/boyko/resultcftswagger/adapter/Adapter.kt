package com.boyko.resultcftswagger.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.models.Loan

private const val APPROVED = "APPROVED"
private const val REJECTED = "REJECTED"
private const val REGISTERED = "REGISTERED"

class Adapter(var items: List<Loan>, val callback: Callback) : RecyclerView.Adapter<Adapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.loan_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val amount = itemView.findViewById<TextView>(R.id.id_amount)
        private val persrnt = itemView.findViewById<TextView>(R.id.id_percent)
        private val period = itemView.findViewById<TextView>(R.id.id_period)
        private val state = itemView.findViewById<TextView>(R.id.id_state)

        fun bind(item: Loan) {
            amount.text = item.amount.toString()
            persrnt.text = item.percent.toString()
            period.text = item.period.toString()
            when(item.state){
                REGISTERED-> state.text = "ЗАРЕГИСТРИРОВАНО"
                APPROVED ->  state.text = "ОДОБРЕНО"
                REJECTED ->  state.text = "ОТКЛОНЕНА"
                else -> state.text = "ОШИБКА В ЗАЯВКЕ"
            }
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }

            itemView.setBackgroundResource(
                    when(item.state){
                        REGISTERED-> R.drawable.bg_loan_item_reg
                        APPROVED ->  R.drawable.bg_loan_item_approved
                        REJECTED ->  R.drawable.bg_loan_item_reject
                        else -> R.drawable.bg_loan_item_reg
                    })
        }
    }

    interface Callback {
        fun onItemClicked(item: Loan)
    }

    fun update(listLoan: List<Loan>) {
        items = listLoan
        notifyDataSetChanged()
    }
}