package com.envios.githubfinder.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.envios.githubfinder.R
import com.envios.githubfinder.databinding.MainAdapterBinding
import com.envios.githubfinder.data.remote.model.User

class GithubUserAdapter(context: Context): RecyclerView.Adapter<GithubUserAdapter.MainViewHolder>(), Filterable{
    private var listUser: MutableList<User?>? = mutableListOf()
    private var listFilterUser: MutableList<User?>? = mutableListOf()

    private var valueFilter: ValueFilter? = null

    private var context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MainViewHolder{
        val binding: MainAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.main_adapter, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (listUser.isNullOrEmpty()) {
            0
        } else{
            listUser!!.size
        }
    }

    override fun getItemId(position: Int): Long {
        val movie: User? = listUser?.get(position)
        return movie!!.id!!.toLong()
    }

    fun addData(list: List<User?>?){
        if (list != null) {
            this.listUser?.addAll(list)
            this.listFilterUser?.addAll(list)
        }
    }

    fun setData(list: List<User?>?){
        if (list != null) {
            this.listUser?.clear()
            this.listUser?.addAll(list)
            this.listFilterUser?.clear()
            this.listFilterUser?.addAll(list)
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listUser?.get(holder.adapterPosition)?.let {
            holder.bindData(it, context )
        }
    }



    class MainViewHolder(private val binding: MainAdapterBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindData(model: User, context: Context){
            val viewModel = GithubUserListViewModel(model)
            binding.itemUser = viewModel
            itemView.setOnClickListener {

            }
        }



    }



    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as ValueFilter
    }

    private inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()


            if (constraint != null && constraint.isNotEmpty()) {
                val filterList = arrayListOf<User>()
                for (i in listFilterUser?.indices!!) {
                    if(listFilterUser?.get(i)?.login?.toLowerCase()?.startsWith(constraint.toString().toLowerCase())!!) {
                        filterList.add(listFilterUser?.get(i)!!)
                    }
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = listFilterUser!!.size
                results.values = listFilterUser
            }
            return results

        }

        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            listUser = results.values as MutableList<User?>
//            if(!listUser.isNullOrEmpty())  {
//                listUser!!.sortWith(compareBy {it?.login})
//            }
            notifyDataSetChanged()
        }

    }


}