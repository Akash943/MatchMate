package com.example.matchmate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchmate.MatchMateApplication
import com.example.matchmate.data.model.UserProfile
import com.example.matchmate.data.network.RetrofitClient
import com.example.matchmate.data.repository.MatchRepository
import kotlinx.coroutines.launch

class MatchViewModel : ViewModel() {

    // Initialize repository
    private val repository = MatchRepository(
        RetrofitClient.api,
        MatchMateApplication.database.userDao()
    )
    // LiveData exposed from Room (reactively updates UI)
    val matchList: LiveData<List<UserProfile>> = repository.allUsers

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentPage = 1

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val maxPage = 5
    private val _pageLimitReached = MutableLiveData<Boolean>(false)
    val pageLimitReached: LiveData<Boolean> get() = _pageLimitReached


   internal fun fetchUsers() {
        if (currentPage > maxPage) {
            _pageLimitReached.value = true
            return
        }
        _isLoading.value = true

        viewModelScope.launch {
            try {
                repository.fetchUsersFromApi(currentPage)
                currentPage++
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to fetch users"
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    internal fun acceptUser(user: UserProfile) {
        updateStatus(user.email, "ACCEPTED")
    }

    internal fun declineUser(user: UserProfile) {
        updateStatus(user.email, "DECLINED")
    }

    private fun updateStatus(email: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateUserStatus(email, newStatus)
        }
    }
}
