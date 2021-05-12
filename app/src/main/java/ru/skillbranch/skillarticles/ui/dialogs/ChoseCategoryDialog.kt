package ru.skillbranch.skillarticles.ui.dialogs


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.skillarticles.R

class ChoseCategoryDialog : DialogFragment() {
    companion object {
        const val CHOOSE_CATEGORY_KEY = "CHOOSE_CATEGORY_KEY"
        const val SELECTED_CATEGORIES = "SELECTED_CATEGORIES"
    }

    private val selectedCategories = mutableSetOf<String>()
    private val args: ChoseCategoryDialogArgs by navArgs()


    private val categoriesAdapter = CategoryAdapter { categoryId, isChecked ->
        if (isChecked) selectedCategories.add(categoryId)
        else selectedCategories.remove(categoryId)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        selectedCategories.clear()
        selectedCategories.addAll(
            savedInstanceState?.getStringArray("checked") ?: args.selectedCategories
        )

        val categoryItems =
            args.categories.map { it.toItem(selectedCategories.contains(it.categoryId)) }
        categoriesAdapter.submitList(categoryItems)

        val listView =
            layoutInflater.inflate(R.layout.fragment_chose_category_dialog, null) as RecyclerView
        with(listView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoriesAdapter
        }

        val adb = AlertDialog.Builder(requireContext())
            .setTitle("Chose category")
            .setView(listView)
            .setPositiveButton("Apply") { _, _ ->
                setFragmentResult(
                    CHOOSE_CATEGORY_KEY,
                    bundleOf(SELECTED_CATEGORIES to selectedCategories.toList())
                )
            }
            .setNegativeButton("Reset") { _, _ ->
                setFragmentResult(
                    CHOOSE_CATEGORY_KEY,
                    bundleOf(SELECTED_CATEGORIES to emptyList<String>())
                )
            }
        return adb.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArray("checked", selectedCategories.toTypedArray())
        super.onSaveInstanceState(outState)
    }
}
