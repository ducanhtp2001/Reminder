package com.example.workreminder.activities.reminder.update

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.workreminder.R
import com.example.workreminder.activities.reminder.MainActivity
import com.example.workreminder.data.network.model.User
import com.example.workreminder.databinding.FragmentUpdateBinding
import com.example.workreminder.usecase.CustomDialog
import com.example.workreminder.usecase.TimeAdapter
import androidx.lifecycle.Observer
import com.example.workreminder.data.local.model.WorkEntity
import com.example.workreminder.usecase.JsonAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateFragment : Fragment() {

    lateinit var b: FragmentUpdateBinding
    lateinit var localUser: User
    lateinit var viewModel: UpdateFragmentViewModel
    lateinit var workToUpdate: WorkEntity
    var workId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentUpdateBinding.inflate(inflater, container, false)

        val bundle = arguments
        if (bundle != null) {
            val item = bundle.getString("item")
            if (item?.isNotEmpty() == true) {
                val workEntity = JsonAdapter<WorkEntity>().toObject(item!!, WorkEntity::class.java)
                if (workEntity != null) {
                    workId = workEntity.id
                    b.apply {
                        b.inputToUpdateTitle.setText(workEntity.title)
                        b.inputToUpdateDescription.setText(workEntity.description)
                        var calendar: Calendar = Calendar.getInstance()
                        calendar.timeInMillis = TimeAdapter.getTimeMillis(workEntity.targetTime)
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        val hour = calendar.get(Calendar.HOUR)
                        val minute = calendar.get(Calendar.MINUTE)
                        val am_pm = calendar.get(Calendar.AM_PM)

                        b.datePickerToUpdate.updateDate(year, month, day)
                        b.timePickerToUpdate.hour = hour
                        b.timePickerToUpdate.minute = minute
                    }
                }
            }
        }

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }

        viewModel = ViewModelProvider(requireActivity())[UpdateFragmentViewModel::class.java]
        viewModel.isGetSuccess.value = false
        localUser = (requireActivity() as MainActivity).getLocalUser()


        registerUserEvent()
        registerObserver()

        return b.root
    }

    private fun registerObserver() {

        viewModel.isGetRequest.observe(this, Observer {

        })

        viewModel.isGetSuccess.observe(this, Observer {
            viewModel.addWorkPart.value = 1
            if (it) Navigation.findNavController(b.root).navigate(R.id.action_update_to_home)
        })

        viewModel.addWorkPart.observe(this, Observer {
            when(it) {
                1 -> {
                    b.layoutDescriptionUpdate.visibility = View.VISIBLE
                    b.layoutDateUpdate.visibility = View.GONE
                    b.fabPrePartUpdate.visibility = View.GONE
                }
                2 -> {
                    b.layoutDescriptionUpdate.visibility = View.GONE
                    b.layoutDateUpdate.visibility = View.VISIBLE
                    b.layoutTimeUpdate.visibility = View.GONE
                    b.fabNextPartUpdate.setImageResource(R.drawable.ic_next_part)
                    b.fabPrePartUpdate.visibility = View.VISIBLE
                }
                3 -> {
                    b.layoutTimeUpdate.visibility = View.VISIBLE
                    b.layoutDateUpdate.visibility = View.GONE
                    b.fabNextPartUpdate.setImageResource(R.drawable.ic_save)
                    b.fabPrePartUpdate.visibility = View.VISIBLE
                }
                else -> {
                    saveNewWork()
                }
            }
        })
    }

    private fun saveNewWork() {
        b.apply {

            val title = inputToUpdateTitle.text.toString()
            val description = inputToUpdateDescription.text.toString()
            val date = datePickerToUpdate.dayOfMonth
            val month = datePickerToUpdate.month
            val year = datePickerToUpdate.year
            val hour = timePickerToUpdate.hour
            val minute = timePickerToUpdate.minute
            val updateTime = Calendar.getInstance()
            val targetTime = Calendar.getInstance()
            targetTime.set(year, month, date, hour, minute)
            val targetTimeSrt = TimeAdapter.getTimeString(targetTime)
            val updateTimeSrt = TimeAdapter.getTimeString(updateTime)
            val isEnable = true
            val work = WorkEntity(workId, title, description, updateTimeSrt, targetTimeSrt, isEnable, localUser.id!!)

            viewModel.updateWork(work)
        }
    }

    private fun registerUserEvent() {
        b.apply {
            fabNextPartUpdate.setOnClickListener {
                if(viewModel.addWorkPart.value == 1
                    && inputToUpdateTitle.text.isEmpty()
                    && inputToUpdateDescription.text.isEmpty())
                {
                    Toast.makeText(requireContext(), "Fill info of task", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.nextPart()
                }
            }

            fabPrePartUpdate.setOnClickListener {
                viewModel.prePart()
            }
        }
    }

    private fun restoreState(savedInstanceState: Bundle) {
        b.apply {
            inputToUpdateTitle.setText(savedInstanceState.getString("title"))
            inputToUpdateTitle.setText(savedInstanceState.getString("description"))
            datePickerToUpdate.updateDate(
                savedInstanceState.getInt("year"),
                savedInstanceState.getInt("month"),
                savedInstanceState.getInt("date")
            )
            timePickerToUpdate.hour = savedInstanceState.getInt("hour")
            timePickerToUpdate.minute = savedInstanceState.getInt("minute")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        b.apply {
            val title = inputToUpdateTitle.text.toString()
            outState.putString("title", title)
            val description = inputToUpdateDescription.text.toString()
            outState.putString("description", description)
            val date = datePickerToUpdate.dayOfMonth
            outState.putInt("date", date)
            val month = datePickerToUpdate.month + 1
            outState.putInt("month", month)
            val year = datePickerToUpdate.year
            outState.putInt("year", year)
            val hour = timePickerToUpdate.hour
            outState.putInt("hour", hour)
            val minute = timePickerToUpdate.minute
            outState.putInt("minute", minute)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_home_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.home_option -> {
                CustomDialog.showConfirmationDialog(requireContext(),
                    "Returning to home will delete all current data, do you want continue?",
                    onConfirm = {
                        Navigation.findNavController(b.root).navigate(R.id.action_update_to_home)
                    })
                return true
            }
            else -> return true
        }

    }
}