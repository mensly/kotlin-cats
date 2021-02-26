package au.com.agl.kotlincats.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.agl.kotlincats.R
import java.lang.IllegalArgumentException

class CatAdapter: RecyclerView.Adapter<CatAdapter.ViewHolder>() {
    private enum class ViewType {
        Heading, Cat, CutestCat
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.text)
    }

    private var cats = emptyMap<String, List<String>>()
    private var headers = emptyList<String>()
    private var headerIndices = emptyList<Int>()
    private var headerCutest = 2

    fun updateCats(cats: Map<String, List<String>>) {
        headers = cats.keys.sorted()
        val headerIndices = mutableListOf<Int>()
        var index = 0
        for (header in headers) {
            headerIndices += index
            index += cats[header]!!.size + 1
        }
        headerCutest = headers.size + cats.values.map(List<String>::size).sum()
        headerIndices += headerCutest
        this.headerIndices = headerIndices
        this.cats = cats
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = (when {
        position in headerIndices -> ViewType.Heading
        position == itemCount - 1 -> ViewType.CutestCat
        else -> ViewType.Cat
    }).ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = when (viewType) {
            ViewType.Heading.ordinal -> R.layout.cell_heading
            ViewType.Cat.ordinal -> R.layout.cell_cat
            ViewType.CutestCat.ordinal -> R.layout.cell_cutest
            else -> throw IllegalArgumentException("Invalid viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when {
            position > headerCutest -> { /* no-op */ }
            position == headerCutest -> holder.textView.setText(R.string.category_cutest)
            position in headerIndices -> holder.textView.text = headers[headerIndices.indexOf(position)]
            else -> {
                var positionOffset = 1
                for ((i, gender) in headers.withIndex()) {
                    val cats = this.cats[gender]!!
                    if (position < headerIndices[i + 1]) {
                        holder.textView.text = cats[position - positionOffset]
                        break
                    }
                    positionOffset += cats.size + 1
                }
            }
        }
    }

    override fun getItemCount() = headerCutest + 2
}