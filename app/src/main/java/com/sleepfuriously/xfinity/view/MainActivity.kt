package com.sleepfuriously.xfinity.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sleepfuriously.xfinity.R

import com.sleepfuriously.xfinity.dummy.DummyContent
import kotlinx.android.synthetic.main.character_list_activity.*
import kotlinx.android.synthetic.main.character_content.view.*
import kotlinx.android.synthetic.main.char_list.*

/**
 * The main Activity for this app (it all starts here).
 *
 * This activity has different presentations for handset and tablet-size devices.
 * On handsets, the activity presents a list of character names, which when touched,
 * lead to a [CharacterDetailActivity] representing character details.
 * On tablets, this activity presents the list of character names and
 * their details side-by-side using two vertical panes.
 */
class MainActivity : AppCompatActivity() {

    //----------------------------
    //  data
    //----------------------------

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false


    //----------------------------
    //  functions
    //----------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_list_activity)

        setSupportActionBar(toolbar)
        toolbar.title = title

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        if (char_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(character_list_rv)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(
            this,
            DummyContent.ITEMS,
            twoPane
        )
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  inner classes
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: MainActivity,
        private val values: List<DummyContent.DummyItem>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = CharacterDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(CharacterDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.char_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, CharacterDetailActivity::class.java).apply {
                        putExtra(CharacterDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.charName.text = item.id

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val charName: TextView = view.char_name_tv
        }
    }
} // class CharacterListActivity
