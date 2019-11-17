package com.example.videotimelines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class ViewModelAdapter : RecyclerView.Adapter<ViewModelAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ViewDataBinding>(view)
    }

    data class CellInfo(val layoutId: Int, val bindingId: Int)

    private val sharedObjects = Hashtable<Int, Any>()

    protected val items = LinkedList<Any>()

    private val cellMap = Hashtable<Class<out Any>, CellInfo>()



    protected fun cell(clazz: Class<out Any>, @LayoutRes layoutId: Int, bindingId: Int) {
        cellMap[clazz] = CellInfo(layoutId, bindingId)
    }

    protected fun getCellInfo(viewModel: Any): CellInfo {
        cellMap.entries
            .filter { it.key == viewModel.javaClass }
            .first { return it.value }

        throw Exception("Cell info for class ${viewModel.javaClass.name} not found.")
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return getCellInfo(items[position]).layoutId
    }

    fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(viewType, parent, false)
        val viewHolder = ViewHolder(view)

        sharedObjects.forEach { viewHolder.binding!!.setVariable(it.key, it.value) }

        return viewHolder
    }

    fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder != null) {
            val cellInfo = getCellInfo(items[position])
            if (cellInfo.bindingId != 0)
                holder.binding!!.setVariable(cellInfo.bindingId, items[position])
        }
    }

    protected fun sharedObject(sharedObject: Any, bindingId: Int) {
        sharedObjects[bindingId] = sharedObject
    }

}