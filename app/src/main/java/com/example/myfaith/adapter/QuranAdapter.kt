package com.example.myfaith.adapter

import android. view. LayoutInflater
import androidx. recyclerview. widget. RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myfaith.R
import com.example.myfaith.entity.Verse
import android.text.Html

class QuranAdapter(private val verses: List<Verse>) : RecyclerView.Adapter<QuranAdapter.QuranViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_verse, parent, false)
        return QuranViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranViewHolder, position: Int) {
        val verse = verses[position]
        holder.bind(verse)
    }

    override fun getItemCount(): Int {
        return verses.size
    }

    class QuranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val arabicText: TextView = itemView.findViewById(R.id.arabicText)
        private val transliterationText: TextView = itemView.findViewById(R.id.transliterationText)
        private val translationText: TextView = itemView.findViewById(R.id.translationText)

        fun bind(verse: Verse) {
            arabicText.text = verse.arabicText
            transliterationText.text = verse.transliteration
            translationText.text = verse.translation

            // Подсветка слов
            for ((word, color) in verse.highlights) {
                val highlightedText = arabicText.text.toString().replace(word, "<font color=\"$color\">$word</font>")
                arabicText.text = Html.fromHtml(highlightedText)
            }
        }
    }
}
