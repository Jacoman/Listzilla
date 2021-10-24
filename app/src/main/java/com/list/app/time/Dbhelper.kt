package com.list.app.time

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.ArrayAdapter


class Dbhelper(context: Context) : SQLiteOpenHelper(context, "RECIPES",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Recipe(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT UNIQUE)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Recipe")
        onCreate(db)
    }
    fun insertData(db: SQLiteDatabase?, userEntry: String){
        val userEntry = userEntry.replace("'", "''")//replaces ' with '' due to sql constraints
        db?.execSQL("CREATE TABLE `" + userEntry + "`(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT UNIQUE)")
        db?.execSQL("INSERT OR IGNORE into Recipe (RNAME) VALUES ('" + userEntry + "')")
    }
    fun deleteData(selectedObject: String, db: SQLiteDatabase?, arrayAdapter: ArrayAdapter<String>, recipeList: MutableList<String>){
        db?.execSQL("DELETE FROM Recipe WHERE RNAME = '" + selectedObject + "'")
        db?.execSQL("DROP TABLE `" + selectedObject +"`")
        val selectedObject =  selectedObject.replace("''","'")
        arrayAdapter.remove(selectedObject)
        recipeList.remove(selectedObject)
        arrayAdapter.notifyDataSetChanged()
    }

}