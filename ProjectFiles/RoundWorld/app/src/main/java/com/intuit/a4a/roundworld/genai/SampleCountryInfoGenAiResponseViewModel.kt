package com.intuit.a4a.roundworld.genai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.intuit.a4a.roundworld.BuildConfig
import com.intuit.a4a.roundworld.data.UiState
import com.intuit.a4a.roundworld.network.utils.interfaces.RepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * This is a sample implementation of Country info viewModel that integrates with Gemini for LLM response for a given country's info
 * This is NOT a complete implementation of Country Info ViewModel, only the Gemini integration part.
 * Candidate is still required to implement the part where given the country's name from the main country list, and fetch the country info
 * from the network layer through repository implementation
 *
 * @param repository: Implementation of the RepositoryInterface for fetching country info
 * @param generativeModel: A facilitator for a given multimodal Gemini model (config parameter is in the viewModel Factory)
 */
class SampleCountryInfoGenAiResponseViewModel(
    private val repository: RepositoryInterface,
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _genAiCountryInfo = MutableStateFlow<UiState<String>>(UiState.Loading)
    val genAiCountryInfo: StateFlow<UiState<String>> = _genAiCountryInfo.asStateFlow()

    /**
     * Given the country name, send the prompt of "tell me more about $countryName" to the generative model
     * The text response will be set to the [_genAiCountryInfo] state flow
     */
    fun getGenAiCountryResponse(countryName: String) {
        val chat = generativeModel.startChat(
            history = listOf()
        )
        viewModelScope.launch {
            val response = chat.sendMessage("tell me more about $countryName")
            response.text?.let { modelResponse ->
                _genAiCountryInfo.value = UiState.Success(modelResponse)
            }
        }
    }
}

class SampleCountryInfoGenAiResponseViewModelFactory(
    private val repository: RepositoryInterface,
) : ViewModelProvider.Factory {

    // Configuration for the LLM model used in the generative model
    private val llmConfig = generationConfig {
        temperature = 2.0f
        topP = 0.9f
    }

    // Construct the Gemini GenerativeModel based on the $config
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.genmiApiKey,
        generationConfig = llmConfig
    )

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return with(modelClass) {
            when {
                isAssignableFrom(SampleCountryInfoGenAiResponseViewModel::class.java) -> {
                    SampleCountryInfoGenAiResponseViewModel(repository, generativeModel) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}