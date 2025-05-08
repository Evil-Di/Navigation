package ru.otus.cookbook.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.VhRecipeCategoryBinding
import ru.otus.cookbook.databinding.VhRecipeItemBinding

class RecipeAdapter(private val context: Context, private val onRecipeClick: (Int) -> Unit):
    ListAdapter<RecipeListItem, RecyclerView.ViewHolder>(RecipeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEMTYPE_RECIPE-> {
                val binding =
                    VhRecipeItemBinding.inflate(LayoutInflater.from(context), parent, false)
                RecipeViewHolder(binding)
            }

            ITEMTYPE_CATEGORY-> {
                val binding =
                    VhRecipeCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
                CategoryViewHolder(binding)
            }

            else -> throw IllegalArgumentException(context.getString(R.string.unknown_view_type))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecipeListItem.RecipeItem -> ITEMTYPE_RECIPE
            is RecipeListItem.CategoryItem -> ITEMTYPE_CATEGORY
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RecipeListItem.RecipeItem -> (holder as RecipeViewHolder).bind(item)
            is RecipeListItem.CategoryItem -> (holder as CategoryViewHolder).bind(item)
        }
    }

    inner class RecipeViewHolder(private val binding: VhRecipeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeListItem.RecipeItem) {
            binding.firstLetter.text = item.title.firstOrNull()?.toString() ?: ""
            binding.title.text = item.title
            binding.description.text = item.description
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .centerCrop()
                .into(binding.image)
            binding.root.setOnClickListener { onRecipeClick(item.id) }
        }
    }

    inner class CategoryViewHolder(private val binding: VhRecipeCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeListItem.CategoryItem) {
            binding.categoryName.text = item.name
        }
    }

    companion object {
        const val ITEMTYPE_RECIPE = 1
        const val ITEMTYPE_CATEGORY = 2
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return when {
            oldItem is RecipeListItem.RecipeItem && newItem is RecipeListItem.RecipeItem -> oldItem.id == newItem.id
            oldItem is RecipeListItem.CategoryItem && newItem is RecipeListItem.CategoryItem -> oldItem.name == newItem.name
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem == newItem
    }
}