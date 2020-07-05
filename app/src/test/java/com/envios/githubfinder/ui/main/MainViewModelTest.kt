package com.envios.githubfinder.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.envios.githubfinder.data.remote.model.GithubUser
import com.envios.githubfinder.data.remote.model.User
import com.envios.githubfinder.data.repository.GithubUserRepository
import com.envios.githubfinder.utils.ViewModelState
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class MainViewModelTest {

    @Mock
    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var repository: GithubUserRepository


    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        viewModel =
            MainViewModel(
             repository
            )
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun searchAsync() {
        val listUser : MutableList<User?> = mutableListOf()
        data class UsersLoaded(val userList: List<User?>? = null) : ViewModelState()

        val user = User(
            "https://avatars2.githubusercontent.com/u/10897992?v=4",
            "https://api.github.com/users/ArefMq/events{/privacy}",
            "https://api.github.com/users/ArefMq/followers",
            "https://api.github.com/users/ArefMq/following{/other_user}",
            "https://api.github.com/users/ArefMq/gists{/gist_id}",
            "",
            12232,
            "a",
            "awawerawer",
            "Aawerasefasd",
            ""

        )
        listUser.add(user)

        val githubUser = GithubUser(false,listUser, 1)
        val response :Response<GithubUser> = Response.success(githubUser)
        runBlocking {
            Mockito.`when`(repository.searchUsers("a", 1, 100)).thenReturn(response)

            viewModel._states.value = UsersLoaded(response.body()?.items)
            assertNotNull(viewModel.states.value)
        }
    }

}