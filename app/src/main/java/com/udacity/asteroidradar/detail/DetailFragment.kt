package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //get arguments from list item
        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).argAsteroid

        //pass arguments to xml variable to binding
        binding.asteroid = asteroid

        //dialog text button
        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        //display asteroid name in actionBar
        (activity as AppCompatActivity).supportActionBar?.title = asteroid.codename

        //some content descriptions
        binding.activityMainImageOfTheDay.contentDescription = if (asteroid.isPotentiallyHazardous){
            getString(R.string.potentially_hazardous_asteroid_image)
        }else{
            getString(R.string.not_hazardous_asteroid_image)
        }

        binding.helpButton.contentDescription = getString(R.string.astronomical_unit_explanation_button)

        return binding.root
    }

    //building dialog
    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }


}
