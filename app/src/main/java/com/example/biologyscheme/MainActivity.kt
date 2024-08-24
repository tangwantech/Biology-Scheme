package com.example.biologyscheme

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.biologyscheme.fragments.AcademicYearAndClassFragment
import com.example.biologyscheme.fragments.ProgressionSheetFragment
import com.example.biologyscheme.fragments.TopicDetailsFragment
import com.example.biologyscheme.models.TopicData
import com.example.biologyscheme.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity(), AcademicYearAndClassFragment.OnButtonClickListener, ProgressionSheetFragment.OnNavigateToTopicDetailsFragmentListener {
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
        gotoAcademicYearAndClassFragment()
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.initClassDataManager(this)

    }

    private fun gotoAcademicYearAndClassFragment(){
        val academicYearAndClassFragment = AcademicYearAndClassFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, academicYearAndClassFragment)
        }
        transaction.commit()
    }

    private fun gotoProgressionSheetFragment(){
        val progressionSheetFragment = ProgressionSheetFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, progressionSheetFragment)
            addToBackStack(null)
        }
        transaction.commit()
    }

    private fun gotoTopicDetailsFragment(itemPosition: Int){
        val topicDetailsFragment = TopicDetailsFragment.newInstance(itemPosition)
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, topicDetailsFragment)
            addToBackStack(null)
        }
        transaction.commit()
    }

    override fun onNextButtonClicked() {
        gotoProgressionSheetFragment()
    }

    override fun onExitButtonClicked() {
        finish()
    }

    override fun onNavigateToTopicDetailsFragment(itemPosition: Int) {
//        println(topicData)
        gotoTopicDetailsFragment(itemPosition)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                println("on back pressed")
                supportFragmentManager.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}