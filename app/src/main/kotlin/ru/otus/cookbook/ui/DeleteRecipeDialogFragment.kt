package ru.otus.cookbook.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.otus.cookbook.R

class DeleteRecipeDialogFragment : DialogFragment() {

    companion object {
        const val CONFIRMATION_RESULT = "confirmation_result"
    }

    private val args: DeleteRecipeDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val recipeTitle = args.recipeTitle
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.delete_recipe_message, recipeTitle))
            .setPositiveButton(R.string.ok) { _, _ ->
                dismiss()
                setResult(true)
            }
            .setNegativeButton(R.string.cancel
            ) { _, _ ->
                dismiss()
                setResult(false)
            }
            .create()
    }

    private fun setResult(result: Boolean) {
        val navController = findNavController()
        navController.previousBackStackEntry?.savedStateHandle?.set(
            CONFIRMATION_RESULT,
            result
        )
        navController.popBackStack()
    }

}