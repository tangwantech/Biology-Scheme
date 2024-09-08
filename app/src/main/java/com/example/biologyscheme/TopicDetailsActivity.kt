package com.example.biologyscheme

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.biologyscheme.databinding.ActivityTopicDetailsBinding
import com.example.biologyscheme.viewmodels.TopicsDetailsViewModel

class TopicDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopicDetailsBinding
    private lateinit var viewModel: TopicsDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopicDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.topic_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
        setupListeners()
        setupObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupObservers() {
        viewModel.topicsDetailsDataAvailable.observe(this){
            if(it){
                binding.progressBarCard.visibility = View.GONE
                setupViews()
            }else{
                binding.progressBarCard.visibility = View.VISIBLE
            }

        }
    }

    private fun setAcademicYearAndClassName(){
        viewModel.setAcademicYear(intent.getStringExtra(Constants.ACADEMIC_YEAR)!!)
        viewModel.setClassName(intent.getStringExtra(Constants.CLASS_NAME)!!)
    }

    private fun setupViewModel(){

        viewModel = ViewModelProvider(this)[TopicsDetailsViewModel::class.java]
        setAcademicYearAndClassName()
        initRoomDatabase()
    }

    private fun initRoomDatabase(){
        viewModel.initRoomDatabaseManager(this)
    }

    private fun setupViews(){

        val itemPosition = intent.getIntExtra(Constants.ITEM_POSITION, -1)
        if (itemPosition > -1){
            val topicData = (viewModel.getTopicDetails(itemPosition))
            binding.tvTopic.tvItem.text = getString(R.string.topic_title, topicData.topicName)
            binding.tvTopic.tvItem.typeface = Typeface.DEFAULT_BOLD

            binding.tvDateRange.text = getString(R.string.date_range, topicData.startDate, topicData.endDate)

            binding.tvModule.tvItem.text = getString(R.string.module, topicData.moduleName)

            binding.tvFamilyOfSituation.tvItem.text = getString(R.string.family_situation, topicData.familyOfSituation)

            binding.tvNumberOfPeriods.tvItem.text = getString(R.string.number_of_periods, topicData.numberOfPeriods.toString())

            val status = if (topicData.isTaught) getString(R.string.completed) else getString(R.string.not_completed)
            binding.tvTopicStatus.tvItem.text = getString(R.string.topic_status, status)

            binding.dropdownCategoryOfAction.tvItem.text = getString(R.string.category_of_action, topicData.categoryOfAction)

            binding.dropdownExampleOfSituation.tvItem.text = getString(R.string.example_of_situation, topicData.exampleOfSituation)


            binding.dropdownAbilities.tvListViewTitle.text = topicData.abilities?.title
            binding.dropdownAbilities.tvValue.text = topicData.abilities?.value

            binding.dropdownSubtopics.tvListViewTitle.text = topicData.subTopics?.title
            binding.dropdownSubtopics.tvValue.text = topicData.subTopics?.value

            binding.dropdownExamplesOfActions.tvListViewTitle.text = topicData.examplesOfActions?.title
            binding.dropdownExamplesOfActions.tvValue.text = topicData.examplesOfActions?.value

            binding.dropdownLifeSkills.tvListViewTitle.text = topicData.lifeSkills?.title
            binding.dropdownLifeSkills.tvValue.text = topicData.lifeSkills?.value

            binding.dropdownOtherResources.tvListViewTitle.text = topicData.otherResources?.title
            binding.dropdownOtherResources.tvValue.text = topicData.otherResources?.value
        }
    }


    private fun setupListeners(){
        binding.dropdownSubtopics.loDropDown.setOnClickListener {
            if (binding.dropdownSubtopics.listViewCard.visibility == View.VISIBLE) {
                binding.dropdownSubtopics.listViewCard.visibility = View.GONE
                binding.dropdownSubtopics.icDown.visibility = View.VISIBLE
                binding.dropdownSubtopics.icUp.visibility = View.GONE
                binding.dropdownSubtopics.divider.visibility = View.GONE
            } else {
                binding.dropdownSubtopics.listViewCard.visibility = View.VISIBLE
                binding.dropdownSubtopics.icDown.visibility = View.GONE
                binding.dropdownSubtopics.icUp.visibility = View.VISIBLE
                binding.dropdownSubtopics.divider.visibility = View.VISIBLE
            }
        }

        binding.dropdownExamplesOfActions.loDropDown.setOnClickListener {
            if(binding.dropdownExamplesOfActions.listViewCard.visibility == View.VISIBLE){
                binding.dropdownExamplesOfActions.listViewCard.visibility = View.GONE
                binding.dropdownExamplesOfActions.icDown.visibility = View.VISIBLE
                binding.dropdownExamplesOfActions.icUp.visibility = View.GONE
                binding.dropdownExamplesOfActions.divider.visibility = View.GONE
            }else{
                binding.dropdownExamplesOfActions.listViewCard.visibility = View.VISIBLE
                binding.dropdownExamplesOfActions.icDown.visibility = View.GONE
                binding.dropdownExamplesOfActions.icUp.visibility = View.VISIBLE
                binding.dropdownExamplesOfActions.divider.visibility = View.VISIBLE
            }

        }

        binding.dropdownAbilities.loDropDown.setOnClickListener {
            if(binding.dropdownAbilities.listViewCard.visibility == View.VISIBLE){
                binding.dropdownAbilities.listViewCard.visibility = View.GONE
                binding.dropdownAbilities.icDown.visibility = View.VISIBLE
                binding.dropdownAbilities.icUp.visibility = View.GONE
                binding.dropdownAbilities.divider.visibility = View.GONE
            }else{
                binding.dropdownAbilities.listViewCard.visibility = View.VISIBLE
                binding.dropdownAbilities.icDown.visibility = View.GONE
                binding.dropdownAbilities.icUp.visibility = View.VISIBLE
                binding.dropdownAbilities.divider.visibility = View.VISIBLE
            }

        }

        binding.dropdownLifeSkills.loDropDown.setOnClickListener {
            if(binding.dropdownLifeSkills.listViewCard.visibility == View.VISIBLE){
                binding.dropdownLifeSkills.listViewCard.visibility = View.GONE
                binding.dropdownLifeSkills.icDown.visibility = View.VISIBLE
                binding.dropdownLifeSkills.icUp.visibility = View.GONE
                binding.dropdownLifeSkills.divider.visibility = View.GONE
            }else{
                binding.dropdownLifeSkills.listViewCard.visibility = View.VISIBLE
                binding.dropdownLifeSkills.icDown.visibility = View.GONE
                binding.dropdownLifeSkills.icUp.visibility = View.VISIBLE
                binding.dropdownLifeSkills.divider.visibility = View.VISIBLE
            }

        }

        binding.dropdownOtherResources.loDropDown.setOnClickListener {
            if (binding.dropdownOtherResources.listViewCard.visibility == View.VISIBLE){
                binding.dropdownOtherResources.listViewCard.visibility =  View.GONE
                binding.dropdownOtherResources.icDown.visibility = View.VISIBLE
                binding.dropdownOtherResources.icUp.visibility = View.GONE
                binding.dropdownOtherResources.divider.visibility = View.GONE
            } else {
                binding.dropdownOtherResources.listViewCard.visibility = View.VISIBLE
                binding.dropdownOtherResources.icDown.visibility = View.GONE
                binding.dropdownOtherResources.icUp.visibility = View.VISIBLE
                binding.dropdownOtherResources.divider.visibility = View.VISIBLE
            }
        }
    }

    companion object{
        fun getIntent(context: Context): Intent{
            return Intent(context, TopicDetailsActivity::class.java)
        }

    }
}