package com.example.biologyscheme

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biologyscheme.databinding.ActivityProgressionSheetBinding
import com.example.biologyscheme.databinding.StatisticsBinding
import com.example.biologyscheme.recycleradapters.ProgressionSheetRecyclerAdapter
import com.example.biologyscheme.viewmodels.ProgressionSheetViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProgressionSheetActivity : AppCompatActivity(),
    ProgressionSheetRecyclerAdapter.OnRecyclerItemClickListener,
    ProgressionSheetRecyclerAdapter.OnItemCheckStateChangeListener,
    ProgressionSheetRecyclerAdapter.OnEditDateButtonClickListener
{

    private lateinit var binding: ActivityProgressionSheetBinding
    private lateinit var viewModel:  ProgressionSheetViewModel
    private lateinit var recyclerAdapter: ProgressionSheetRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressionSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.progression_sheet)
        setupViewModels()
        setupHeader()
        setupObservers()
        setupListeners()
        setupRecyclerView()

    }

    private fun setAcademicYearAndClassName(){
        viewModel.setAcademicYear(intent.getStringExtra(Constants.ACADEMIC_YEAR)!!)
        viewModel.setClassName(intent.getStringExtra(Constants.CLASS_NAME)!!)
    }

    private fun setupHeader(){
        binding.tvAcademicYear.text = viewModel.getAcademicYear()
        binding.tvClassName.text = viewModel.getClassName()
    }

    private fun setupViewModels(){

        viewModel = ViewModelProvider(this)[ProgressionSheetViewModel::class.java]
        setAcademicYearAndClassName()
        initRoomDatabase()
    }

    private fun initRoomDatabase(){
        viewModel.initRoomDatabaseManager(this)
    }

    private fun setupObservers(){
        viewModel.progressionSheetDataAvailable.observe(this){
            binding.progressbar.visibility = View.VISIBLE
            if(it){
                recyclerAdapter.updateData(viewModel.getProgressionSheet())
                recyclerAdapter.notifyDataSetChanged()
                binding.tvProgressionUnavailable.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE

            }else{
                binding.tvProgressionUnavailable.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.progressbar.visibility  = if (binding.tvProgressionUnavailable.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
//            showHideMainDisplay(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }else ->

            return super.onOptionsItemSelected(item)
        }
    }
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }

    private fun showHideMainDisplay(dataAvailable: Boolean){
        if(dataAvailable){
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvProgressionUnavailable.visibility = View.GONE
            println("Showing recycler view")
        }else{
            binding.recyclerView.visibility = View.GONE
            binding.tvProgressionUnavailable.visibility = View.VISIBLE
            println("Hiding recycler view")
        }
    }

    private fun setupRecyclerView(){
        val layoutMan = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.layoutManager = layoutMan

        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerAdapter = ProgressionSheetRecyclerAdapter(this, this, this, this)

        binding.recyclerView.adapter = recyclerAdapter



    }

    private fun setupListeners(){
        binding.btnStatistics.setOnClickListener {
            displayStatisticsDialog()
        }

    }

    private fun displayStatisticsDialog(){

        val workCoverageStatistics = viewModel.getWorkCoverageStatistics()
        val hoursCoverageStatistics = viewModel.getHoursCoverageStatistics()

        val statisticsBinding = StatisticsBinding.inflate(LayoutInflater.from(this))
        statisticsBinding.tvNumberOfTopicProgrammed.text = workCoverageStatistics.numberOfTopicsProgramed
        statisticsBinding.tvNumberOfTopicsDone.text = workCoverageStatistics.numberOfTopicsDone
        statisticsBinding.tvPercentageOfTopicsDone.text = workCoverageStatistics.percentageCovered

        statisticsBinding.tvNumberOfHoursProgrammed.text = hoursCoverageStatistics.numberOfHoursProgramed
        statisticsBinding.tvNumberOfHoursDone.text = hoursCoverageStatistics.numberOfHoursDone
        statisticsBinding.tvPercentageOfHoursDone.text = hoursCoverageStatistics.percentageDone

        val dialogStatistics = AlertDialog.Builder(this).apply {
            setView(statisticsBinding.root)
            setPositiveButton(getString(R.string.ok)){btn, _ ->
                btn.dismiss()
            }.create()
        }
        dialogStatistics.show()
    }

    private fun displayDatePicker(topicIndex: Int, editButtonIndex: Int){
        val datePicker = DatePickerDialog(this)
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val tempDate = viewModel.getTopicDate(topicIndex, editButtonIndex)

        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK") { d, i ->

            val day = datePicker.datePicker.dayOfMonth
            val month = datePicker.datePicker.month + 1
            val year = datePicker.datePicker.year
            viewModel.updateTopicDate(topicIndex, editButtonIndex, "$day/$month/$year")
            recyclerAdapter.notifyItemChanged(topicIndex)
            d.dismiss()

        }
        datePicker.show()
        if(tempDate.isNotEmpty()){
            val date = LocalDate.parse(tempDate, formatter)
            datePicker.datePicker.updateDate(date.year, date.monthValue - 1 , date.dayOfMonth)
        }

    }

    override fun onStop() {
        super.onStop()
        viewModel.updateClassDataInRoom()

    }

    override fun onItemClicked(itemPosition: Int) {
        gotoTopicsDetailsActivity(itemPosition)
    }

    override fun onItemCheckStateChanged(itemPosition: Int, checkState: Boolean) {
        viewModel.updateTopicTaughtState(itemPosition, checkState)
//        activityViewModel?.updateSchemeDataForAcademicYear(fragmentViewModel?.getClassSchemeData()!!)
    }

    override fun onEditDateClicked(topicIndex: Int, dateEditButtonIndex: Int) {
        displayDatePicker(topicIndex, dateEditButtonIndex)
    }

    private fun gotoTopicsDetailsActivity(itemPosition: Int){
        val intent = TopicDetailsActivity.getIntent(this)
        intent.apply {
            putExtra(Constants.ITEM_POSITION, itemPosition)
            putExtra(Constants.CLASS_NAME, viewModel.getClassName())
            putExtra(Constants.ACADEMIC_YEAR, viewModel.getAcademicYear())
        }
        startActivity(intent)
    }

    companion object {
        fun getIntent(context: Context): Intent{
            return Intent(context, ProgressionSheetActivity::class.java)
        }
    }
}