package com.levins.truecaller
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.levins.truecaller.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.beginButton.setOnClickListener(View.OnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                async { getTenthChar() }
                async {getTenthLetters() }
                async {countWords() }
            }
        })

    }
    private fun getTenthChar() {
        val websiteContent = readWebsite()
        val tenthLetter = websiteContent.substring(9,10).toString() // find the letter. here it will be empty ""
        //when we finish we need to publish it in the ui , which is in the main thread
        CoroutineScope(Dispatchers.Main).launch {
            binding.tenTextview.text = "Char number 10 in the website is: " +tenthLetter
        }
    }
    private fun getTenthLetters() {
        val websiteContent = readWebsite()
        var allTens = "All the chars in the 10th places are: \n"
        for (i in 10 until websiteContent.length step 10) {
            allTens = allTens + websiteContent.substring(i - 1, i) + " ,"
        }
        //when we finish we need to publish it in the ui , which is in the main thread:
        CoroutineScope(Dispatchers.Main).launch {
            binding.allTensTextView.text = allTens
        }
    }
    private fun countWords(){
        val websiteContent = readWebsite()
        val words = websiteContent.split(' ','\t','\n','\r','f')
        var occurences = mutableListOf<Pair<String,Int>>()
        //go through all the words we have and check for each of them how many times we count it(in the occurences list). and than ++ for its counting.
        for((index, currentWord) in words.withIndex()){
            val indexOfCurrenword = occurences.indexOf(occurences.find{it.first.equals(currentWord)})
            if(indexOfCurrenword != -1) {// if this is not the first time we see this phrase/word
                val howMany = occurences.get(indexOfCurrenword).second
                occurences[indexOfCurrenword] = Pair(currentWord,howMany + 1) //.second.absoluteValue = howMany + 1
            }
            else//if it's the first count, we will add it to the counting list - "occurences"
                occurences.add(Pair(currentWord,1))
        }
        val builder = StringBuilder()
        builder.append("The occurences of the words in the website are:  \n")
        for(ocurrence in occurences) {
            builder.append(ocurrence.first + ":  " + ocurrence.second + " times")
                .append("\n")
        }
        //when we finish we need to publish it in the ui , which is in the main thread
        CoroutineScope(Dispatchers.Main).launch {
            binding.occurencesTextView.text = builder.toString()
        }

    }
    //read the website content:
    private fun readWebsite():String {
        val url = "https://truecaller.blog/2018/01/22/life-as-an-android-engineer/" //"http://40.88.151.148/static/home_assignment"
        val doc: Document = Jsoup.connect(url).get()
        var reader: BufferedReader? = null
        return doc.toString()

    }
}