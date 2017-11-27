package br.ufs.hiring.nubankchallenge.chargeback

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import br.ufs.hiring.nubankchallenge.R
import br.ufs.nubankchallenge.core.presentation.chargeback.ReasonRowModel

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class ReclaimReasonsAdapter(val reasons: List<ReasonRowModel>) :
        RecyclerView.Adapter<Holder>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(reasons[position])
    }

    override fun getItemCount() = reasons.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent?.context)

        val row = inflater.inflate(
                R.layout.view_item_chargeback_reclaim_reason,
                parent,
                false
        )

        return Holder(row)
    }

}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: ReasonRowModel) = with(itemView as Switch) {
        text = model.description
        isChecked = model.choosedByUser
        setOnCheckedChangeListener { _, isChecked -> model.choosedByUser = isChecked }
    }
}