package com.example.workreminder.activities.reminder.trash

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.WorkManager
import com.example.workreminder.R
import com.example.workreminder.activities.reminder.MainActivity
import com.example.workreminder.data.network.model.User
import com.example.workreminder.databinding.FragmentTrashBinding
import com.example.workreminder.usecase.CustomDialog
import com.example.workreminder.usecase.listadapter.WorkAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrashFragment : Fragment() {

    lateinit var b: FragmentTrashBinding
    lateinit var viewModel: TrashFragmentViewModel
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
        b = FragmentTrashBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[TrashFragmentViewModel::class.java]
        localUser = (requireActivity() as MainActivity).getLocalUser()

        viewModel.getLocalData(localUser)

        registerObserver()

        return b.root
    }

    private fun registerObserver() {
        viewModel.works.observe(this, Observer {
            b.apply {
                if (viewModel.works.value != null) {
                    adapter = WorkAdapter(requireContext(),
                        R.layout.work_item,
                        viewModel.works.value!!,
                        onBtnDeleteClick = {
                            CustomDialog.showConfirmationDialog(requireContext(),
                                "Delete this work?",
                                onConfirm = {
                                    viewModel.deleteLocalData(it)
                                    val workManager = WorkManager.getInstance(requireContext())
                                    workManager.cancelUniqueWork(it.id.toString())
                                    Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show()
                                })
                        },
                        onSwitchClick = {
                            Toast.makeText(requireContext(), "Unavailable", Toast.LENGTH_SHORT).show()
                        }
                        )
                    listView.adapter = adapter
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_home_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.home_option -> {
                Navigation.findNavController(b.root).navigate(R.id.action_trash_to_home)
                return true
            }
            else -> return true
        }
    }
}