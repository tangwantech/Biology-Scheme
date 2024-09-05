package com.example.biologyscheme.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biologyscheme.Constants
import com.example.biologyscheme.R
import com.example.biologyscheme.databinding.FragmentProgressionSheetBinding
import com.example.biologyscheme.databinding.StatisticsBinding
import com.example.biologyscheme.models.TopicData
import com.example.biologyscheme.recycleradapters.ProgressionSheetRecyclerAdapter
import com.example.biologyscheme.viewmodels.MainActivityViewModel
import com.example.biologyscheme.viewmodels.ProgressionSheetViewModel
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProgressionSheetFragment : Fragment(),
    ProgressionSheetRecyclerAdapter.OnRecyclerItemClickListener,
    ProgressionSheetRecyclerAdapter.OnItemCheckStateChangeListener,
    ProgressionSheetRecyclerAdapter.OnEditDateButtonClickListener{

    companion object {
        const val FRAGMENT_NAME = "ProgressionSheetFragment"
        fun newInstance() = ProgressionSheetFragment()
    }

    private var activityViewModel: MainActivityViewModel? = null
    private var fragmentViewModel: ProgressionSheetViewModel? = null
    private lateinit var binding: FragmentProgressionSheetBinding
    private lateinit var recyclerAdapter: ProgressionSheetRecyclerAdapter
    private lateinit var onNavigateToTopicDetailsFragmentListener: OnNavigateToTopicDetailsFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNavigateToTopicDetailsFragmentListener){
            onNavigateToTopicDetailsFragmentListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProgressionSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupToolBar()
//        setupViewModels()
//        setupHeader()
//        setupListeners()

    }

    override fun onResume() {
        super.onResume()
        setupToolBar()
        setupViewModels()
        setupHeader()
        setupListeners()
        setupRecyclerView()
        setupObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
//        activityViewModel = null
//        fragmentViewModel = null
    }

    private fun setupToolBar(){
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolBar.toolBar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.title = requireContext().getString(R.string.progression_sheet)
    }

    private fun setupHeader(){
        binding.tvAcademicYear.text = activityViewModel?.getAcademicYear()
        binding.tvClassName.text = activityViewModel?.getClassName()
    }

    private fun setupViewModels(){

        activityViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]

        fragmentViewModel = ViewModelProvider(requireActivity())[ProgressionSheetViewModel::class.java]
        fragmentViewModel?.setAcademicYearAndIndex(activityViewModel?.getAcademicYear()!!, activityViewModel?.getAcademicYearIndex()!!)
        fragmentViewModel?.setClassName(activityViewModel?.getClassName()!!)

        val className = activityViewModel?.getClassName()!!
        val fileName = Constants.CLASS_SCHEME_FILE_NAMES[activityViewModel?.getClassName()]!!

        activityViewModel?.readClassDataFromRoom(className, fileName )


    }

    private fun setupObservers(){
        fragmentViewModel?.progressionSheetDataAvailable?.observe(requireActivity()){
            if(it){
//                println(fragmentViewModel?.getProgressionSheet())
                recyclerAdapter.updateData(fragmentViewModel?.getProgressionSheet()!!)
                recyclerAdapter.notifyDataSetChanged()
                binding.tvProgressionUnavailable.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

            }else{
                binding.tvProgressionUnavailable.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
//            showHideMainDisplay(it)
//            println("Testing........")
        }

        activityViewModel?.classSchemeAvailable?.observe(requireActivity()){classSchemeData  ->
//            classSchemeData?.let {
//                fragmentViewModel?.setClassSchemeData(it)
//            }

            fragmentViewModel?.setClassSchemeData(classSchemeData)

        }
    }

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
        val layoutMan = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.layoutManager = layoutMan

        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        recyclerAdapter = ProgressionSheetRecyclerAdapter(requireContext(), this, this, this)

        binding.recyclerView.adapter = recyclerAdapter



    }

    private fun setupListeners(){
        binding.btnStatistics.setOnClickListener {
            displayStatisticsDialog()
        }
    }

    private fun displayStatisticsDialog(){

        val workCoverageStatistics = fragmentViewModel?.getWorkCoverageStatistics()
        val hoursCoverageStatistics = fragmentViewModel?.getHoursCoverageStatistics()

        val statisticsBinding = StatisticsBinding.inflate(LayoutInflater.from(requireContext()))
        statisticsBinding.tvNumberOfTopicProgrammed.text = workCoverageStatistics?.numberOfTopicsProgramed
        statisticsBinding.tvNumberOfTopicsDone.text = workCoverageStatistics?.numberOfTopicsDone
        statisticsBinding.tvPercentageOfTopicsDone.text = workCoverageStatistics?.percentageCovered

        statisticsBinding.tvNumberOfHoursProgrammed.text = hoursCoverageStatistics?.numberOfHoursProgramed
        statisticsBinding.tvNumberOfHoursDone.text = hoursCoverageStatistics?.numberOfHoursDone
        statisticsBinding.tvPercentageOfHoursDone.text = hoursCoverageStatistics?.percentageDone

        val dialogStatistics = AlertDialog.Builder(requireContext()).apply {
            setView(statisticsBinding.root)
            setPositiveButton(requireContext().getString(R.string.ok)){btn, _ ->
                btn.dismiss()
            }.create()
        }
        dialogStatistics.show()
    }

    private fun displayDatePicker(topicIndex: Int, editButtonIndex: Int){
        val datePicker = DatePickerDialog(requireContext())
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val tempDate = fragmentViewModel?.getTopicDate(topicIndex, editButtonIndex)

        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK") { d, i ->

            val day = datePicker.datePicker.dayOfMonth
            val month = datePicker.datePicker.month + 1
            val year = datePicker.datePicker.year
            fragmentViewModel?.updateTopicDate(topicIndex, editButtonIndex, "$day/$month/$year")
            recyclerAdapter.notifyItemChanged(topicIndex)
            d.dismiss()

        }
        datePicker.show()
        if(tempDate?.isNotEmpty()!!){
            val date = LocalDate.parse(tempDate, formatter)
            datePicker.datePicker.updateDate(date.year, date.monthValue - 1 , date.dayOfMonth)
        }

    }

    override fun onItemClicked(itemPosition: Int) {
        onNavigateToTopicDetailsFragmentListener.onNavigateToTopicDetailsFragment(itemPosition)
    }

    override fun onItemCheckStateChanged(itemPosition: Int, checkState: Boolean) {
        fragmentViewModel?.updateTopicTaughtState(itemPosition, checkState)
//        activityViewModel?.updateSchemeDataForAcademicYear(fragmentViewModel?.getClassSchemeData()!!)
    }

    override fun onStop() {
        super.onStop()
        fragmentViewModel?.getClassSchemeData()?.let{
//            println(it)
            activityViewModel?.updateSchemeDataForAcademicYear(it)
        }

    }

    interface OnNavigateToTopicDetailsFragmentListener{
        fun onNavigateToTopicDetailsFragment(itemPosition: Int)
    }

    override fun onEditDateClicked(topicIndex: Int, dateEditButtonIndex: Int) {
//       println("Topic index: $topicIndex, Date button index: $dateEditButtonIndex")
        displayDatePicker(topicIndex, dateEditButtonIndex)
    }


}