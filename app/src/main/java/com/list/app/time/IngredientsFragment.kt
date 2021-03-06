package com.list.app.time

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.list.app.time.databinding.FragmentIngredientsBinding


class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val helper = activity?.let { Dbhelper(this.requireContext()) }
        val db = helper?.readableDatabase
         _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mListView: ListView = binding.recipeListView
        val dialog = Dialog(requireActivity())
        dialog.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )
        val recipeList: MutableList<String> = ArrayList()
        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, recipeList)

        /***********************************************************************************
         * Displays pop up that calls the ingredient pop up funtion to insert data
         * accordingly
         ***********************************************************************************/

        binding.addRecipeButton.setOnClickListener {
            dialog.setContentView(R.layout.pop_up)
            val submit: Button = dialog.findViewById(R.id.RecipeButton1) as Button
            val editT: EditText = dialog.findViewById(R.id.Redit1)
            dialog.show()
            submit.setOnClickListener {
                val userEntry = editT.text.toString()
                if (recipeList.size >= 1) {
                    if (recipeList.contains(userEntry)) {//duplicate checking
                        Toast.makeText(activity, "Duplicate Entry, Try Again!", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    } else {
                        helper!!.ingredientpopup(db,userEntry,editT,dialog, requireView())
                        Toast.makeText(activity, "Recipe Added", Toast.LENGTH_LONG).show()

                    }
                } else {
                    helper!!.ingredientpopup(db,userEntry,editT,dialog, requireView())
                    Toast.makeText(activity, "Recipe Added", Toast.LENGTH_LONG).show()
                    }
                }

            }

        val cursor = db?.rawQuery("SELECT * FROM  Recipe", null)//sets up cursor for printdata
        helper!!.printData(cursor!!, recipeList, mListView, arrayAdapter)
        sendData(mListView)

        /***********************************************************************************
         * Pulls value from listview based on longclick, calls deletion function and
         * deletes based on string pulled.
         ***********************************************************************************/
        mListView.setOnItemLongClickListener { parent, view, position, id ->
            var selectedObject = mListView.getItemAtPosition(position).toString()
            selectedObject = selectedObject.replace("'","''")
            dialog.setContentView(R.layout.list_layout)
            val submit: Button = dialog.findViewById(R.id.RecipeButton1) as Button
            dialog.show()
            submit.setOnClickListener {
                Toast.makeText(activity, "Recipe Deleted", Toast.LENGTH_LONG).show()
                val rString = "Recipe"
                helper.deleteRowData(selectedObject, db, arrayAdapter, recipeList, rString)
                dialog.dismiss()
            }

            return@setOnItemLongClickListener true
        }
        cursor.close()
        return root
    }
    /***********************************************************************************
     * The sendData function is called when a user taps on a listview object, this
     * function pulls the name from the object at the position tapped and sends it to
     * DisplayIngredients fragment to be utilized to display the correct data.
     ***********************************************************************************/
    private fun sendData(mListView: ListView){
            mListView.setOnItemClickListener { parent, view, position, id ->
                val selectedObject = mListView.getItemAtPosition(position).toString()
                activity?.intent?.putExtra("key2", selectedObject)
                view?.findNavController()?.navigate(R.id.action_navigation_Ingredients_to_navigation_DisplayIngredients)
            }
        }
}