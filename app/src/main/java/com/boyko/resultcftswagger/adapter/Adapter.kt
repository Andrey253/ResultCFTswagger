package com.boyko.resultcftswagger.adapter

//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.models.Loan


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
            state.text = item.state
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: Loan)
    }

}