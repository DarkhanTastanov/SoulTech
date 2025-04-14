package com.example.myfaith.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.example.myfaith.R
import java.io.BufferedReader
import java.io.InputStreamReader
import com.example.myfaith.adapter.VerseAdapter
import com.example.myfaith.entity.QuranData
import com.example.myfaith.entity.Surah


class QuranFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VerseAdapter
    private lateinit var surahTitle: TextView
    private lateinit var surahInfo: TextView
    private lateinit var paraInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView)
        surahTitle = view.findViewById(R.id.surahTitle)
        surahInfo = view.findViewById(R.id.surahInfo)
        paraInfo = view.findViewById(R.id.paraInfo)

        loadQuranData()
    }

    private fun loadQuranData() {
        try {
            val inputStream = context?.assets?.open("quran_data.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.readText()
            reader.close()

            val quranData = Gson().fromJson(jsonString, QuranData::class.java)

            if (quranData.surahs.isNotEmpty()) {
                val surah = quranData.surahs[0]

                updateSurahHeader(surah)

                adapter = VerseAdapter(surah.verses)

                recyclerView.adapter = adapter
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateSurahHeader(surah: Surah) {
        // Update the header views with the surah's information
        surahTitle.text = "${surah.number}. ${surah.name}"
        surahInfo.text = "Меккелік • ${surah.verses.size} Аят"
        paraInfo.text = "Пара 27 • ¼ Hizb 54"
    }
}
