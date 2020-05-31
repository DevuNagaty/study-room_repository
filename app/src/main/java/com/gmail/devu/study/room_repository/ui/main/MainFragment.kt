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

        // Set the adapter
        viewAdapter = MediaListAdapter()
        view.findViewById<RecyclerView>(R.id.media_list).apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }

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
                                TAG,
                                "title: %s, artist: %s".format(title.toString(), artist.toString())
                            )
                            viewModel.insert(Media(0, title.toString(), artist.toString()))
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

}
