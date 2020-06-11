package com.gmail.devu.study.room_repository.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.devu.study.room_repository.R
import com.gmail.devu.study.room_repository.media.Media
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {
    private val TAG = this::class.java.simpleName

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var viewAdapter: MediaListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        // Setup the adapter
        viewAdapter = MediaListAdapter()
        view.findViewById<RecyclerView>(R.id.media_list).apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
            itemTouchHelper.attachToRecyclerView(this)
        }

        // Setup the 'Add' button
        view.findViewById<FloatingActionButton>(R.id.meida_add).apply {
            setOnClickListener {
                val dialog_view =
                    LayoutInflater.from(activity).inflate(R.layout.media_add_dialog, null)
                AlertDialog.Builder(activity)
                    .setTitle(R.string.dialog_title)
                    .setView(dialog_view)
                    .setPositiveButton(R.string.button_add) { dialog, which ->
                        val title = dialog_view.findViewById<EditText>(R.id.edit_title).text
                        val artist = dialog_view.findViewById<EditText>(R.id.edit_artist).text

                        if (title.isNotEmpty()) {
                            Log.v(
                                TAG, "Add title: %s, artist: %s".format(
                                    title.toString(),
                                    artist.toString()
                                )
                            )
                            viewModel.insert(Media(0, title.toString(), artist.toString(), 0, 0, 0))
                        }
                    }
                    .setNegativeButton(R.string.button_cancel, null)
                    .setCancelable(true)
                    .show()
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // At this point the logs show that the database is not open.
        Log.v(TAG, "Got a ViewModel")

        viewModel.allMedias.observe(viewLifecycleOwner, Observer { medias ->
            // When the database is opened, this observer is called.
            Log.v(TAG, "allMedias is updated, meida count = %d".format(medias.size))
            medias?.let {
                viewAdapter.setMedias(medias)
            }
        })
    }

    private val itemTouchHelper by lazy {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            private var moving: Boolean = false
            private var oldPos: Int = 0
            private var newPos: Int = 0

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int =
                makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Save position, then update data upon completion of drag and drop
                moving = true
                oldPos = viewHolder.adapterPosition
                newPos = target.adapterPosition
                Log.v(TAG, "onMove(%d -> %d)".format(oldPos, newPos))
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                Log.v(TAG, "onSwiped(%d, %d)".format(pos, direction))
                viewModel.removeAt(pos)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                when (actionState) {
                    ACTION_STATE_DRAG -> {
                        Log.v(TAG, "onSelectedChanged(ACTION_STATE_DRAG)")
                    }
                    ACTION_STATE_IDLE -> {
                        Log.v(TAG, "onSelectedChanged(ACTION_STATE_IDLE)")
                        if (moving) {
                            moving = false
                            viewModel.move(oldPos, newPos)
                        }
                    }
                }
            }
        })
    }
}
