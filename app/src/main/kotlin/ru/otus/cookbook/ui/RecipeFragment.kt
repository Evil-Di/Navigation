package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private val args: RecipeFragmentArgs by navArgs()
    private val recipeId: Int get() = args.recipeId
    private val binding = FragmentBindingDelegate<FragmentRecipeBinding>(this)
    private val model: RecipeFragmentViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                set(RecipeFragmentViewModel.RECIPE_ID_KEY, recipeId)
            }
        },
        factoryProducer = { RecipeFragmentViewModel.Factory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentRecipeBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAppBar()

        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            DeleteRecipeDialogFragment.CONFIRMATION_RESULT
        )?.observe(viewLifecycleOwner) { result->
            if (result) {
                model.delete()
                navController.navigateUp()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }

    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun setupAppBar() {
        binding.withBinding {
            recipeToolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_to_cookbook)
            }
            recipeToolbar.setOnMenuItemClickListener { menuItem->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        val action = RecipeFragmentDirections.actionToDeleteRecipeDialog(getTitle())
                        findNavController().navigate(action)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun displayRecipe(recipe: Recipe) = binding.withBinding {
        // Display the recipe
        recipeTitle.text = recipe.title
        recipeShortDescription.text = recipe.category.name
        recipeFullDescription.text = recipe.description
        Glide.with(this@RecipeFragment)
            .load(recipe.imageUrl)
            .into(recipeImage)
    }
}