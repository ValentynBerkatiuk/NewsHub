package com.simpleappsdev.news

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class NewsDataViewModel: ViewModel() {

    private val _state = MutableStateFlow(State.None)

    val state: StateFlow<State>
        get() = _state.asStateFlow()

}


sealed class State {

    object None: State()
    class Loading: State()
    class Error: State()
    class Success(val articles: Result<List<Article>>): State()
}