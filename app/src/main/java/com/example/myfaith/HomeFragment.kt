package com.example.myfaith

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mynavigationapp.R // Make sure this import is correct

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_page_fragment, container, false)

        val quranButton = view.findViewById<Button>(R.id.quran_button)
        val quoteButton = view.findViewById<Button>(R.id.quote_button)
        val zikrButton = view.findViewById<Button>(R.id.zikr_button)
        val booksButton = view.findViewById<Button>(R.id.books_button)
        val compassButton = view.findViewById<Button>(R.id.compass_button)

        quranButton.setOnClickListener {
            // Navigate to Quran Fragment (if you have one)
            // Example: findNavController().navigate(R.id.action_homeFragment_to_quranFragment)
        }

        quoteButton.setOnClickListener {
            // Handle quote button click
        }

        zikrButton.setOnClickListener {
            // Navigate to Zikr Activity
            // Example:
            // val intent = Intent(requireContext(), ZikrActivity::class.java)
            // startActivity(intent)
        }

        booksButton.setOnClickListener {
            // Handle books button click
        }

        compassButton.setOnClickListener {
            // Navigate to Compass Fragment
            findNavController().navigate(R.id.compassFragment) // Use the ID from your nav_graph
        }

        return view
    }
}