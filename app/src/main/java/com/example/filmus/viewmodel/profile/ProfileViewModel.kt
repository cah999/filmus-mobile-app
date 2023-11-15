package com.example.filmus.viewmodel.profile

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.common.Constants
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.favorites.FavoritesDatabase
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.database.profile.ProfileEntity
import com.example.filmus.domain.database.reviews.UserReviewDatabase
import com.example.filmus.domain.favorite.FavoritesCacheUseCase
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.profile.ProfileResponse
import com.example.filmus.domain.profile.ProfileUseCase
import com.example.filmus.domain.registration.validation.FieldValidationState
import com.example.filmus.domain.registration.validation.ValidateRegistrationDataUseCase
import com.example.filmus.domain.registration.validation.ValidationData
import com.example.filmus.domain.userReviews.UserReviewsUseCase
import com.example.filmus.repository.TokenManager
import com.example.filmus.repository.favorites.FavoritesCacheRepository
import com.example.filmus.repository.profile.ApiProfileRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import com.example.filmus.repository.userReviews.UserReviewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileViewModel(private val tokenManager: TokenManager) : ViewModel() {
    var id = ""
    var nickname = mutableStateOf(Constants.EMPTY)
    var email = mutableStateOf(Constants.EMPTY)
    var avatarLink = mutableStateOf(Constants.EMPTY)
    var name = mutableStateOf(Constants.EMPTY)
    var gender = mutableIntStateOf(0)
    var birthDate = mutableStateOf(Constants.EMPTY)
    val screenState = mutableStateOf(UIState.DEFAULT)

    val validationStates = mutableStateOf(emptyList<FieldValidationState>())
    private val validateUseCase = ValidateRegistrationDataUseCase()

    fun validateProfileData() {
        val data = ValidationData(
            name = name.value, login = null, email = email.value, birthDate = birthDate.value
        )

        val states = validateUseCase.validate(data)
        validationStates.value = states
    }

    fun getContainerColor(state: FieldValidationState?): Int {
        return if (state?.isValid == false) {
            0x1AE64646
        } else {
            0x00000000
        }
    }

    fun getOutlineColor(state: FieldValidationState?): Int {
        return if (state?.isValid == false) {
            0xFFE64646.toInt()
        } else {
            0xFF909499.toInt()
        }
    }

    fun logout(onResult: (Boolean) -> Unit) {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            val apiService = createApiService(token = tokenManager.getToken())
            val profileRepository = ApiProfileRepository(apiService)
            val profileUseCase = ProfileUseCase(profileRepository)
            when (profileUseCase.logout()) {
                is ApiResult.Success -> {
                    tokenManager.clearToken()
                    tokenManager.context.cacheDir.deleteRecursively()
                    clearProfileCache()
                    clearFavoritesCache()
                    clearUserReviewsCache()
                    withContext(Dispatchers.Main) {
                        onResult(true)
                    }
                    screenState.value = UIState.DEFAULT
                }

                is ApiResult.Error -> {
                    screenState.value = UIState.ERROR
                }

                is ApiResult.Unauthorized -> {
                    screenState.value = UIState.UNAUTHORIZED
                }

            }
        }
    }

    fun getInfo(force: Boolean = false) {
        if (force) {
            screenState.value = UIState.LOADING
            viewModelScope.launch {
                fetchAndPopulate()
                screenState.value = UIState.DEFAULT
            }
            return
        }
        viewModelScope.launch {
            val profileDatabase = ProfileDatabase.getDatabase(
                context = tokenManager.context
            )
            val profileDao = profileDatabase.profileDao()
            val profileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
            profileUseCase.getProfile().collect { cachedProfile ->
                if (cachedProfile != null) {
                    populateUI(cachedProfile)
                } else {
                    fetchAndPopulate()
                }
            }
        }
    }

    private suspend fun fetchAndPopulate() {
        val token = tokenManager.getToken()
        if (token != null) {
            val apiService = createApiService(token)
            val profileRepository = ApiProfileRepository(apiService)
            val profileUseCase = ProfileUseCase(profileRepository)

            when (val result = profileUseCase.getInfo()) {
                is ApiResult.Success -> {
                    if (result.data != null) {
                        cacheProfile(result.data)
                        populateUI(result.data)
                    }
                }

                is ApiResult.Unauthorized -> {
                    screenState.value = UIState.UNAUTHORIZED
                }

                is ApiResult.Error -> {
                    screenState.value = UIState.ERROR
                }
            }
        }
    }

    private suspend fun cacheProfile(profile: ProfileResponse) {
        val profileDatabase = ProfileDatabase.getDatabase(
            context = tokenManager.context
        )
        val profileDao = profileDatabase.profileDao()
        val profileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
        profileUseCase.cacheProfile(profile)
    }

    private fun populateUI(profile: ProfileEntity) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        id = profile.id
        nickname.value = profile.nickname
        email.value = profile.email
        avatarLink.value = profile.avatarLink ?: Constants.EMPTY
        name.value = profile.name
        gender.intValue = profile.gender
        birthDate.value = outputFormat.format(inputFormat.parse(profile.birthDate) ?: "")
    }

    private fun populateUI(profile: ProfileResponse) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        id = profile.id
        nickname.value = profile.nickname
        email.value = profile.email
        avatarLink.value = profile.avatarLink ?: Constants.EMPTY
        name.value = profile.name
        gender.intValue = profile.gender
        birthDate.value =
            outputFormat.format(inputFormat.parse(profile.birthDate) ?: Constants.EMPTY)
    }

    fun updateInfo(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val apiService = createApiService(tokenManager.getToken())
            val profileRepository = ApiProfileRepository(apiService)
            val profileUseCase = ProfileUseCase(profileRepository)

            val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = outputFormat.format(inputFormat.parse(birthDate.value) ?: Constants.EMPTY)
            val profileResponse = ProfileResponse(
                id = id,
                nickname = nickname.value,
                email = email.value,
                avatarLink = if (avatarLink.value == "") null else avatarLink.value,
                name = name.value,
                birthDate = date,
                gender = gender.intValue
            )
            when (profileUseCase.updateInfo(profileResponse)) {
                is ApiResult.Success -> {
                    cacheProfile(profileResponse)
                    populateUI(profileResponse)
                    withContext(Dispatchers.Main) {
                        onResult(true)
                    }
                }

                is ApiResult.Unauthorized -> {
                    screenState.value = UIState.UNAUTHORIZED
                }

                is ApiResult.Error -> {
                    screenState.value = UIState.UNAUTHORIZED
                }
            }
        }
    }

    private suspend fun clearProfileCache() {
        val profileDatabase = ProfileDatabase.getDatabase(
            context = tokenManager.context
        )
        val profileDao = profileDatabase.profileDao()
        val profileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
        profileUseCase.clearProfile()
    }

    private suspend fun clearFavoritesCache() {
        val favoritesDatabase = FavoritesDatabase.getDatabase(
            context = tokenManager.context
        )
        val favoritesDao = favoritesDatabase.favoritesDao()
        val favoritesUseCase = FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
        favoritesUseCase.clearFavorites()
    }

    private suspend fun clearUserReviewsCache() {
        val userReviewsDatabase = UserReviewDatabase.getDatabase(
            context = tokenManager.context
        )
        val userReviewsDao = userReviewsDatabase.userReviewDao()
        val userReviewsUseCase = UserReviewsUseCase(UserReviewsRepository(userReviewsDao))
        userReviewsUseCase.clearReviews()
    }
}