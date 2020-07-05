package com.envios.githubfinder.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.envios.githubfinder.R
import com.envios.githubfinder.databinding.MainFragmentBinding
import com.envios.githubfinder.utils.EndlessRecyclerViewScrollListener
import com.envios.githubfinder.utils.Failed
import com.envios.githubfinder.utils.Loading
import com.envios.githubfinder.utils.NetworkStateUtil
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding
    private lateinit var _filterText: String
    private lateinit var adapter: GithubUserAdapter
    private var resetPage =1
    private val perPage = 100
    private var isLoadMore = false
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.main = viewModel


        val layoutManager = LinearLayoutManager(context)
        binding.rvUsers.layoutManager = layoutManager
        adapter = context?.let { GithubUserAdapter(it) }!!
        binding.rvUsers.setHasFixedSize(true)
        adapter.setHasStableIds(true)
        binding.rvUsers.adapter = adapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: RecyclerView?
            ) {
                isLoadMore = true
                val newPage = page +1
                showLoading()
                viewModel.searchUsers(search.query.toString(),newPage,perPage)
            }

        }

        binding.rvUsers.addOnScrollListener(scrollListener)


        binding.search.dispatchSetActivated(true)
        binding.search.queryHint = "Masukkan keyword pencarian"
        binding.search.onActionViewExpanded()
        binding.search.setIconifiedByDefault(false)
        binding.search.clearFocus()

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                isLoadMore = false

                if (newText != "") {
                    showLoading()
                    viewModel.searchUsers(newText, resetPage, perPage)
//                    adapter.filter.filter(newText)
                    _filterText = newText
                } else {
                    hideLoading()
                    adapter.setData(emptyList())
                    adapter.filter.filter("")
                    adapter.notifyDataSetChanged()
                }

                return false
            }


        })



       searchUserByQuery()

    }

    private fun observeLiveData(){
        viewModel.states.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Loading -> {

                }

                is MainViewModel.UsersLoaded -> {
                    hideLoading()
                    if (isLoadMore) {
                        adapter.addData(it.userList)
                        adapter.filter.filter(_filterText)
                    } else {
                        if (it.userList.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_LONG).show()
                            adapter.setData(emptyList())
                        } else {
                            adapter.setData(it.userList)
                            adapter.filter.filter(_filterText)

                        }
                    }

                    adapter.notifyDataSetChanged()
                }

                is Failed -> {
                    hideLoading()
                    if (it.error != null) Toast.makeText(requireContext(),it.error!!, Toast.LENGTH_LONG).show()

                }
            }


        })

    }

    private fun searchUserByQuery() {
        val networkStateUtil = NetworkStateUtil(requireContext())

        networkStateUtil.observe(viewLifecycleOwner, Observer{
                isConnected ->
            isConnected?.let {
                if(it) {
                    search.visibility = View.VISIBLE
                    rv_users.visibility = View.VISIBLE
                    rl_no_connection.visibility = View.GONE
                    observeLiveData()

                    if(adapter.itemCount > 0 && search.query.isEmpty()) {
                        adapter.setData(emptyList())
                        adapter.notifyDataSetChanged()
                    }

                } else {
                    search.visibility = View.GONE
                    if (adapter.itemCount <= 0) {
                        rv_users.visibility = View.GONE
                        search.visibility = View.GONE
                        rl_no_connection.visibility = View.VISIBLE

                    } else {
                        Toast.makeText(requireContext(),"There is a problem with network connection",Toast.LENGTH_SHORT).show()
                        rv_users.visibility = View.VISIBLE
                        rl_no_connection.visibility = View.GONE
                        search.visibility = View.VISIBLE
                        hideLoading()
                    }

                }
            }
        } )
    }


    fun showLoading() {
        iv_rotated.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotated_view)
        iv_rotated.startAnimation(animation)
    }

     fun hideLoading() {
         iv_rotated.visibility = View.GONE
         iv_rotated.clearAnimation()

    }
}
