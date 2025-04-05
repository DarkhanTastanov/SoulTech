package com.example.myfaith.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myfaith.activity.ZikrActivity
import com.example.mynavigationapp.R

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
            findNavController().navigate(R.id.quranFragment)
        }

        quoteButton.setOnClickListener {
        }

        zikrButton.setOnClickListener {
             val intent = Intent(requireContext(), ZikrActivity::class.java)
             startActivity(intent)

        }

        booksButton.setOnClickListener {
            findNavController().navigate(R.id.booksFragment)
        }

        compassButton.setOnClickListener {
            findNavController().navigate(R.id.compassFragment)
        }

        return view
    }
}