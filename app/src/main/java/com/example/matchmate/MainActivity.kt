package com.example.matchmate

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.matchmate.databinding.ActivityMainBinding
import com.example.matchmate.ui.view.MatchAdapter
import com.example.matchmate.ui.viewmodel.MatchViewModel
import com.example.matchmate.util.isNetworkAvailable
import com.example.matchmate.worker.SyncWorker
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MatchViewModel by viewModels()
    private lateinit var adapter: MatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()
        fetchUsers()
    }

    private fun setupUI() {
        binding.topAppBar.title = HtmlCompat.fromHtml("<b>MatchMate</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        setSupportActionBar(binding.topAppBar)

        adapter = MatchAdapter(
            onAcceptClicked = { viewModel.acceptUser(it) },
            onDeclineClicked = { viewModel.declineUser(it) }
        )

        val layoutManager = LinearLayoutManager(this)
        binding.rvMatches.layoutManager = layoutManager
        binding.rvMatches.adapter = adapter

        setupPagination(layoutManager)
    }

    private fun setupPagination(layoutManager: LinearLayoutManager) {
        binding.rvMatches.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                val isNotLoading = viewModel.isLoading.value != true
                val isNotAtEnd = viewModel.pageLimitReached.value != true

                if (dy > 0 && isNotLoading && isNotAtEnd) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        Log.d("Pagination", "Fetching next page...")
                        viewModel.fetchUsers()
                    }
                }
            }
        })
    }

    private fun setupObservers() {
        // Observe LiveData
        viewModel.matchList.observe(this) { users ->
            adapter.submitList(users)
        }

        // Spinner visibility
        viewModel.isLoading.observe(this) { isLoading ->
            adapter.showLoadingSpinner(isLoading)
        }

        // Error Snackbar
        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction(getString(R.string.retry)) {
                    viewModel.fetchUsers()
                }.show()
            }
        }

        // Max page Snackbar
        viewModel.pageLimitReached.observe(this) { reached ->
            if (reached) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.max_page_reached),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchUsers() {
        // Fetch first page after checking the internet connection
        if (!isNetworkAvailable()) {
            Snackbar.make(binding.root, getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show()
        } else {
            viewModel.fetchUsers()
            scheduleSyncWorker()
        }
    }

    private fun scheduleSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(syncRequest)
    }
}
