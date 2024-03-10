package com.example.workreminder.activities.reminder.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.workreminder.R
import com.example.workreminder.activities.reminder.MainActivity
import com.example.workreminder.data.network.model.User
import com.example.workreminder.data.network.model.WorkForJson
import com.example.workreminder.databinding.FragmentAddBinding
import com.example.workreminder.usecase.CustomDialog
import com.example.workreminder.usecase.TimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddFragment : Fragment() {

    lateinit var b: FragmentAddBinding
    lateinit var localUser: User
    lateinit var viewModel: AddFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentAddBinding.inflate(inflater, container, false)

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }

        viewModel = ViewModelProvider(requireActivity())[AddFragmentViewModel::class.java]
        viewModel.isGetSuccess.value = false
        localUser = (requireActivity() as MainActivity).getLocalUser()


        registerUserEvent()
        registerObserver()

        return b.root
    }

    private fun registerObserver() {

        viewModel.isGetRequest.observe(this, Observer {
            b.progressBarAdd.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.isGetSuccess.observe(this, Observer {
            viewModel.addWorkPart.value = 1
            if (it) {
                Navigation.findNavController(b.root).navigate(R.id.action_add_to_home)
            }
        })

        viewModel.addWorkPart.observe(this, Observer {
            when(it) {
                1 -> {
                    b.layoutDescription.visibility = View.VISIBLE
                    b.layoutDate.visibility = View.GONE
                    b.fabPrePart.visibility = View.GONE
                }
                2 -> {
                    b.layoutDescription.visibility = View.GONE
                    b.layoutDate.visibility = View.VISIBLE
                    b.layoutTime.visibility = View.GONE
                    b.fabNextPart.setImageResource(R.drawable.ic_next_part)
                    b.fabPrePart.visibility = View.VISIBLE
                }
                3 -> {
                    b.layoutTime.visibility = View.VISIBLE
                    b.layoutDate.visibility = View.GONE
                    b.fabNextPart.setImageResource(R.drawable.ic_save)
                    b.fabPrePart.visibility = View.VISIBLE
                }
                else -> {
                    saveNewWork()
                }
            }
        })
    }

    private fun saveNewWork() {
        b.apply {

            val title = inputToAddTitle.text.toString()
            val description = inputToAddDescription.text.toString()
            val date = datePickerToAdd.dayOfMonth
            val month = datePickerToAdd.month
            val year = datePickerToAdd.year
            val hour = timePickerToAdd.hour
            val minute = timePickerToAdd.minute
            val updateTime = Calendar.getInstance()
            val targetTime = Calendar.getInstance()
            targetTime.set(year, month, date, hour, minute, Calendar.AM)
            val targetTimeSrt = TimeAdapter.getTimeString(targetTime)
            val updateTimeSrt = TimeAdapter.getTimeString(updateTime)
            val isEnable = true
            val workForJson = WorkForJson(title, description, updateTimeSrt, targetTimeSrt, isEnable, localUser.id!!)

            viewModel.insertWork(workForJson)
        }
    }

    private fun registerUserEvent() {
        b.apply {
            fabNextPart.setOnClickListener {
                if(viewModel.addWorkPart.value == 1
                    && inputToAddTitle.text.isEmpty()
                    && inputToAddDescription.text.isEmpty())
                {
                    Toast.makeText(requireContext(), "Fill info of task", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.nextPart()
                }
            }

            fabPrePart.setOnClickListener {
                viewModel.prePart()
            }
        }
    }

    private fun restoreState(savedInstanceState: Bundle) {
        b.apply {
            inputToAddTitle.setText(savedInstanceState.getString("title"))
            inputToAddDescription.setText(savedInstanceState.getString("description"))
            datePickerToAdd.updateDate(
                savedInstanceState.getInt("year"),
                savedInstanceState.getInt("month"),
                savedInstanceState.getInt("date")
            )
            timePickerToAdd.hour = savedInstanceState.getInt("hour")
            timePickerToAdd.minute = savedInstanceState.getInt("minute")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        b.apply {
            val title = inputToAddTitle.text.toString()
            outState.putString("title", title)
            val description = inputToAddDescription.text.toString()
            outState.putString("description", description)
            val date = datePickerToAdd.dayOfMonth
            outState.putInt("date", date)
            val month = datePickerToAdd.month + 1
            outState.putInt("month", month)
            val year = datePickerToAdd.year
            outState.putInt("year", year)
            val hour = timePickerToAdd.hour
            outState.putInt("hour", hour)
            val minute = timePickerToAdd.minute
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
                        viewModel.addWorkPart.value = 1
                        Navigation.findNavController(b.root).navigate(R.id.action_add_to_home)
                    })
                return true
            }
            else -> return true
        }

    }
}