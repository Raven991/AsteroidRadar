package com.example.asteroidradar.main

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.request.RequestOptions
import com.example.asteroidradar.Asteroid
import com.example.asteroidradar.Constants
import com.example.asteroidradar.PictureOfDay
import com.example.asteroidradar.R
import com.example.asteroidradar.databinding.FragmentMainBinding
import com.squareup.picasso.Picasso

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: PhotoAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = PhotoAdapter(PhotoAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            if (asteroids != null) {
                adapter.submitList(asteroids)
            }
        }

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) { asteroid ->
            if (asteroid != null) {
                navigateToDetailFragment(asteroid)
                viewModel.doneNavigating()
            }
        }

//        viewModel.displaySnackbarEvent.observe(viewLifecycleOwner) { displaySnackbarEvent ->
//            if (displaySnackbarEvent) {
//                displaySnackbar(
//                    getString(R.string.error_retrieving_online),
//                    requireView()
//                )
//                viewModel.doneDisplayingSnackbar()
//            }
//        }

        viewModel.pictureOfDay.observe(viewLifecycleOwner) {
            setUpPictureOfDay(it!!)
            setupPictureOfTheDay(it!!.url)
        }
    }

    private fun navigateToDetailFragment(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.week_menu -> viewModel.onViewWeekAsteroidsClicked()
            R.id.today_menu -> viewModel.onTodayAsteroidsClicked()
            R.id.saved_menu -> viewModel.onSavedAsteroidsClicked()
        }
        return true
    }

//    fun displaySnackbar(snackbarText: String, view: View) {
//        val snackbar = Snackbar.make(view, snackbarText, Snackbar.LENGTH_SHORT)
//        val backgroundColor = ContextCompat.getColor(view.context, R.color.default_text_color)
//        snackbar.view.setBackgroundColor(backgroundColor)
//        snackbar.show()
//    }

    private fun setupPictureOfTheDay(url: String) {
        try {
            Log.e(TAG, "setupPictureOfTheDay URL: $url")

            Picasso.get()
                .load(url)
                .resize(0, 200)
                .centerCrop()
                .error(R.drawable.no_image_available)
                .into(binding.ivPictureOfTheDay)
//
//                Glide.with(this)
//                    .load("https://apod.nasa.gov/apod/image/2208/SiccarPoint_CuriosityGill_1080.jpg")
//                    .apply(RequestOptions().override(600,200))
//                    .into(binding.ivPictureOfTheDay)


        } catch (e: Exception) {
            Log.e(TAG, "setupPictureOfTheDay: ${e.message}")
        }

    }
    private fun setUpPictureOfDay(data: PictureOfDay) {
        binding.ivPictureOfTheDay.contentDescription = data.title
        binding.mainPicDayTitle.text = data.title
    }

}