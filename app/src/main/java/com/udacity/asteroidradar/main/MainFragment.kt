package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this,MainViewModel.ViewModelFactory(activity.application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //binding inflater
        val binding = FragmentMainBinding.inflate(inflater)

        //binding lifecycle to xml variable
        binding.lifecycleOwner = this

        //binding viewModel to xml variable
        binding.viewModel = viewModel

        //observing navigate liveData to handle navigation
        viewModel.navigateToItem.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigateComplete()
            }
        })

        //passing adapter to recyclerView with click
        binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.ItemListener {
            viewModel.itemDetail(it)
        })

        viewModel.domainApod.observe(viewLifecycleOwner,{
            it?.let {
                binding.activityMainImageOfTheDay.contentDescription =
                    String.format(getString(R.string.nasa_picture_of_day_content_description_format,it.title))
            }
        })


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //handle item menu click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when(item.itemId){
                R.id.show_today_menu -> AsteroidFilter.DAY
                R.id.show_week_menu -> AsteroidFilter.WEEK
                else -> AsteroidFilter.ALL
            }
        )
        return true
    }

}
