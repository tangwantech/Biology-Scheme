package com.example.biologyscheme.fragments

import android.graphics.Typeface
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
        setupListeners()
    }

    private fun setupViewModel(){
        progressionSheetViewModel = ViewModelProvider(requireActivity())[ProgressionSheetViewModel::class.java]
    }

    private fun setupViews(){
        val itemPosition = arguments?.getInt(ITEM_POSITION)
        itemPosition?.let {
            val topicData = (progressionSheetViewModel.getTopicDetails(it))
            binding.tvTopic.tvItem.text = requireContext().getString(R.string.topic_title, topicData.topicName)
            binding.tvTopic.tvItem.typeface = Typeface.DEFAULT_BOLD

            binding.tvDateRange.text = requireContext().getString(R.string.date_range, topicData.startDate, topicData.endDate)

            binding.tvModule.tvItem.text = requireContext().getString(R.string.module, topicData.moduleName)

            binding.tvFamilyOfSituation.tvItem.text = requireContext().getString(R.string.family_situation, topicData.familyOfSituation)

            binding.tvNumberOfPeriods.tvItem.text = requireContext().getString(R.string.number_of_periods, topicData.numberOfPeriods.toString())

            val status = if (topicData.isTaught) requireContext().getString(R.string.completed) else requireContext().getString(R.string.not_completed)
            binding.tvTopicStatus.tvItem.text = requireContext().getString(R.string.topic_status, status)

            binding.dropdownCategoryOfAction.tvItem.text = requireContext().getString(R.string.category_of_action, topicData.categoryOfAction)

            binding.dropdownExampleOfSituation.tvItem.text = requireContext().getString(R.string.example_of_situation, topicData.exampleOfSituation)


            binding.dropdownObjectives.tvListViewTitle.text = topicData.objectives.title
            val objectivesAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, topicData.objectives.listData)
            binding.dropdownObjectives.listView.adapter = objectivesAdapter

            binding.dropdownSubtopics.tvListViewTitle.text = topicData.subTopics.title
            val subtopicsAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, topicData.subTopics.listData)
            binding.dropdownSubtopics.listView.adapter = subtopicsAdapter

            binding.dropdownExamplesOfActions.tvListViewTitle.text = topicData.examplesOfActions.title
            val examplesOfActionsAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, topicData.examplesOfActions.listData)
            binding.dropdownExamplesOfActions.listView.adapter = examplesOfActionsAdapter

            binding.dropdownLifeSkills.tvListViewTitle.text = topicData.lifeSkills.title
            val lifeSkillsAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, topicData.lifeSkills.listData)
            binding.dropdownLifeSkills.listView.adapter = lifeSkillsAdapter

            binding.dropdownOtherResources.tvListViewTitle.text = topicData.otherResources.title
            val otherResourcesAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, topicData.otherResources.listData)
            binding.dropdownOtherResources.listView.adapter = otherResourcesAdapter
        }
    }

    private fun setupToolBar(){
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolBar.toolBar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.title = requireContext().getString(R.string.topic_details)

    }

    private fun setupListeners(){
        binding.dropdownSubtopics.loDropDown.setOnClickListener {
            if (binding.dropdownSubtopics.listViewCard.visibility == View.VISIBLE) {
                binding.dropdownSubtopics.listViewCard.visibility = View.GONE
                binding.dropdownSubtopics.icDown.visibility = View.VISIBLE
                binding.dropdownSubtopics.icUp.visibility = View.GONE
            } else {
                binding.dropdownSubtopics.listViewCard.visibility = View.VISIBLE
                binding.dropdownSubtopics.icDown.visibility = View.GONE
                binding.dropdownSubtopics.icUp.visibility = View.VISIBLE
            }
        }

        binding.dropdownExamplesOfActions.loDropDown.setOnClickListener {
            if(binding.dropdownExamplesOfActions.listViewCard.visibility == View.VISIBLE){
                binding.dropdownExamplesOfActions.listViewCard.visibility = View.GONE
                binding.dropdownExamplesOfActions.icDown.visibility = View.VISIBLE
                binding.dropdownExamplesOfActions.icUp.visibility = View.GONE
            }else{
                binding.dropdownExamplesOfActions.listViewCard.visibility = View.VISIBLE
                binding.dropdownExamplesOfActions.icDown.visibility = View.GONE
                binding.dropdownExamplesOfActions.icUp.visibility = View.VISIBLE
            }

        }

        binding.dropdownObjectives.loDropDown.setOnClickListener {
            if(binding.dropdownObjectives.listViewCard.visibility == View.VISIBLE){
                binding.dropdownObjectives.listViewCard.visibility =View.GONE
                binding.dropdownObjectives.icDown.visibility = View.VISIBLE
                binding.dropdownObjectives.icUp.visibility = View.GONE
            }else{
                binding.dropdownObjectives.listViewCard.visibility = View.VISIBLE
                binding.dropdownObjectives.icDown.visibility = View.GONE
                binding.dropdownObjectives.icUp.visibility = View.VISIBLE
            }

        }

        binding.dropdownLifeSkills.loDropDown.setOnClickListener {
            if(binding.dropdownLifeSkills.listViewCard.visibility == View.VISIBLE){
                binding.dropdownLifeSkills.listViewCard.visibility = View.GONE
                binding.dropdownLifeSkills.icDown.visibility = View.VISIBLE
                binding.dropdownLifeSkills.icUp.visibility = View.GONE
            }else{
                binding.dropdownLifeSkills.listViewCard.visibility = View.VISIBLE
                binding.dropdownLifeSkills.icDown.visibility = View.GONE
                binding.dropdownLifeSkills.icUp.visibility = View.VISIBLE
            }

        }

        binding.dropdownOtherResources.loDropDown.setOnClickListener {
            if (binding.dropdownOtherResources.listViewCard.visibility == View.VISIBLE){
                binding.dropdownOtherResources.listViewCard.visibility =  View.GONE
                binding.dropdownOtherResources.icDown.visibility = View.VISIBLE
                binding.dropdownOtherResources.icUp.visibility = View.GONE
            } else {
                binding.dropdownOtherResources.listViewCard.visibility = View.VISIBLE
                binding.dropdownOtherResources.icDown.visibility = View.GONE
                binding.dropdownOtherResources.icUp.visibility = View.VISIBLE
            }
        }
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