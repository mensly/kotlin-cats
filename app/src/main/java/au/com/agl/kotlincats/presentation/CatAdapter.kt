package au.com.agl.kotlincats.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.agl.kotlincats.R
import au.com.agl.kotlincats.data.model.Categories
import java.lang.IllegalArgumentException

class CatAdapter: RecyclerView.Adapter<CatAdapter.ViewHolder>() {
    private enum class ViewType {
        Heading, Cat, CutestCat
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.text)
    }

    private var cats = emptyMap<String, List<String>>()
    private val headerMale = 0
    private var headerFemale = 1
    private var headerCutest = 2

    fun updateCats(cats: Map<String, List<String>>) {
        val maleCatCount = cats[Categories.Male.name]!!.size
        val femaleCatCount = cats[Categories.Female.name]!!.size
        headerFemale = 1 + maleCatCount
        headerCutest = 2 + maleCatCount + femaleCatCount
        this.cats = cats
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = (when (position) {
        headerMale, headerFemale, headerCutest -> ViewType.Heading
        itemCount - 1 -> ViewType.CutestCat
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
            position == headerMale -> holder.textView.setText(Categories.Male.label)
            position == headerFemale -> holder.textView.setText(Categories.Female.label)
            position == headerCutest -> holder.textView.setText(Categories.Cutest.label)
            position < headerFemale -> holder.textView.text = cats[Categories.Male.name]!![position - headerMale - 1]
            position < headerCutest -> holder.textView.text = cats[Categories.Female.name]!![position - headerFemale - 1]
        }
    }

    override fun getItemCount() =
        3 + // Headers
        cats.values.map(List<String>::size).sum() + // Slightly less cute cats
        1 // Balrog
}