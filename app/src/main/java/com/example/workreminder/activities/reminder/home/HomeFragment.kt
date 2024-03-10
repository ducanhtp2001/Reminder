package com.example.workreminder.activities.reminder.home

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.*
import com.example.workreminder.R
import com.example.workreminder.activities.reminder.MainActivity
import com.example.workreminder.data.local.model.WorkEntity
import com.example.workreminder.data.network.model.User
import com.example.workreminder.databinding.FragmentHomeBinding
import com.example.workreminder.usecase.CustomDialog
import com.example.workreminder.usecase.JsonAdapter
import com.example.workreminder.usecase.TimeAdapter
import com.example.workreminder.usecase.listadapter.WorkAdapter
import com.example.workreminder.usecase.workmanaget.ExcuteWorkRequest
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var b: FragmentHomeBinding
    lateinit var viewModel: HomeFragmentViewModel
    lateinit var localUser: User

    lateinit var adapter: WorkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]

        localUser = (requireActivity() as MainActivity).getLocalUser()

        viewModel.getAllWork(localUser)

        registerUserEvent()
        registerObserver()

        return b.root
    }

    private fun registerObserver() {

        viewModel.works.observe(this, Observer {
            b.apply {
                displayListView()
                startSchedule()
            }
        })

        viewModel.isGetRequest.observe(this, Observer {
            b.loadWorkProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun startSchedule() {
        for (item in viewModel.works.value!!) {
            if (item.isUnable) {
                val currentCalendar = Calendar.getInstance()
                val targetCalendar = Calendar.getInstance()
                targetCalendar.timeInMillis = TimeAdapter.getTimeMillis(item.targetTime)
                val delay = targetCalendar.timeInMillis - currentCalendar.timeInMillis
                if (delay > 0) reminder(item, delay)
            } else {
                val workManager = WorkManager.getInstance(requireContext())
                workManager.cancelUniqueWork(item.id.toString())
            }
        }
    }

    private fun reminder(item: WorkEntity, delay: Long) {
        val data = Data.Builder()
            .putString("workTitle", item.title)
            .putInt("notificationId", item.id)
            .build()

        val workManager = WorkManager.getInstance(requireContext())
        val workRequest = OneTimeWorkRequestBuilder<ExcuteWorkRequest>()
            .setConstraints(
                Constraints.Builder().build()
            ).setInputData(data)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        workManager.enqueueUniqueWork(item.id.toString(), ExistingWorkPolicy.REPLACE , workRequest)
    }

    private fun displayListView() {
        adapter = WorkAdapter(requireContext(),
            R.layout.work_item,
            viewModel.works.value!!,
            onSwitchClick = {
                it.isUnable = !it.isUnable
                viewModel.updateWork(it)
                Toast.makeText(requireContext(), "update", Toast.LENGTH_SHORT).show()
            },
            onBtnDeleteClick = {
                CustomDialog.showConfirmationDialog(requireContext(),
                    "Delete this work?",
                    onConfirm = {
                        viewModel.deleteWork(it, localUser)
                        val workManager = WorkManager.getInstance(requireContext())
                        workManager.cancelUniqueWork(it.id.toString())
                        Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show()
                    })
            })

        b.listView.adapter = adapter
    }

    private fun registerUserEvent() {
        b.apply {

            listView.setOnItemClickListener { parent, view, position, id ->
                val item = JsonAdapter<WorkEntity>().toJson(viewModel.works.value!!.get(position))
                val bundle = Bundle()
                bundle.putString("item", item)
                Navigation.findNavController(b.root).navigate(R.id.action_home_to_update, bundle)
            }

            fabAdd.setOnClickListener {
                Navigation.findNavController(b.root).navigate(R.id.action_home_to_add)
            }

            btnCancelSearchView.setOnClickListener {
                searchView.visibility = View.GONE
                adapter.list = viewModel.works.value!!
                adapter.notifyDataSetChanged()
            }

            btnClearText.setOnClickListener {
                autoComplete.setText("")
            }

            val titles: List<String> = viewModel.works.value!!.map { it.title }
            val autoAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, titles)
            autoComplete.setAdapter(autoAdapter)

            btnSearch.setOnClickListener {
                adapter.list = viewModel.search(autoComplete.text.toString())
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.search_option -> {
                b.apply {
                    searchView.visibility = View.VISIBLE
                }
                return true
            }
            R.id.trash_option -> {
                Navigation.findNavController(b.root).navigate(R.id.action_home_to_trash)
                return true
            }
            R.id.setting_option -> {
                Navigation.findNavController(b.root).navigate(R.id.action_home_to_setting)
                return true
            }
            else -> return true
        }
    }
}