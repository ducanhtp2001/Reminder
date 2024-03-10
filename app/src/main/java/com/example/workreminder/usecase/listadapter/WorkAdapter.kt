package com.example.workreminder.usecase.listadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.workreminder.R
import com.example.workreminder.data.local.model.WorkEntity
import com.example.workreminder.usecase.TimeAdapter

class WorkAdapter(
    val context: Context,
    val layoutId: Int,
    var list: List<WorkEntity>,
    val onSwitchClick: (WorkEntity) -> Unit,
    val onBtnDeleteClick: (WorkEntity) -> Unit
) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return list.get(position).id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View
        var holder: WorkViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(layoutId, null)
            holder = WorkViewHolder(view)
            view.setTag(holder)
        } else {
            view = convertView
            holder = convertView.tag as WorkViewHolder
        }
        holder.apply {
            title.text = list[position].title
            updateTime.text = list[position].updateTime
            time.text = TimeAdapter.getTime(list[position].targetTime)
            date.text = TimeAdapter.getDate(list[position].targetTime)
            btnDelete.setOnClickListener {
                onBtnDeleteClick(list[position])
            }
            switch.isChecked = list[position].isUnable
            switch.setOnClickListener {
                onSwitchClick(list[position])
            }
        }

        return view
    }

    inner class WorkViewHolder(view: View) {
        var title: TextView = view.findViewById(R.id.txt_work_title)
        var updateTime: TextView = view.findViewById(R.id.txt_work_update_time)
        var time: TextView = view.findViewById(R.id.txt_work_time)
        var date: TextView = view.findViewById(R.id.txt_work_calendar)
        var switch: SwitchCompat = view.findViewById(R.id.switch_enable_reminder)
        var btnDelete: ImageView = view.findViewById(R.id.btn_delete_work)
    }
}