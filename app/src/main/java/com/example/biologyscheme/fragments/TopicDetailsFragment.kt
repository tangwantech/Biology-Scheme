package com.example.biologyscheme.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.biologyscheme.R
import com.example.biologyscheme.databinding.FragmentTopicDetailsBinding
import com.example.biologyscheme.viewmodels.ProgressionSheetViewModel

class TopicDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTopicDetailsBinding
    private lateinit var progressionSheetViewModel: ProgressionSheetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupToolBar()
//        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        setupToolBar()
        setupViewModel()
        setupViews()
    }

    private fun setupViewModel(){
        progressionSheetViewModel = ViewModelProvider(requireActivity())[ProgressionSheetViewModel::class.java]
    }

    private fun setupViews(){
        val itemPosition = arguments?.getInt(ITEM_POSITION)
        itemPosition?.let {
            val topicData = (progressionSheetViewModel.getTopicDetails(it))
            binding.tvTopic.text = requireContext().getString(R.string.topic_name, topicData.topicName)
            binding.tvDateRange.text = requireContext().getString(R.string.date_range, topicData.startDate, topicData.endDate)
            binding.tvFamilyOfSituation.text = requireContext().getString(R.string.family_situation, topicData.familyOfSituation)
            binding.tvNumberOfPeriods.text = requireContext().getString(R.string.number_of_periods, topicData.numberOfPeriods.toString())


            val status = if (topicData.isTaught) requireContext().getString(R.string.completed) else requireContext().getString(R.string.not_completed)
            binding.tvTopicStatus.text = requireContext().getString(R.string.topic_status, status)

            val objectivesAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, topicData.objectives)
            binding.listViewObjectives.adapter = objectivesAdapter

            val subtopicsAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, topicData.subTopics)
            binding.listViewSubTopics.adapter = subtopicsAdapter
        }
    }

    private fun setupToolBar(){
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolBar.toolBar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.title = requireContext().getString(R.string.topic_details)

    }

    companion object {
        const val FRAGMENT_NAME = "TopicDetailsFragment"
        const val ITEM_POSITION = "Item position"
        fun newInstance(itemPosition: Int) =
            TopicDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ITEM_POSITION, itemPosition)
                }
            }
    }

}