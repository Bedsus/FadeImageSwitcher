package ru.bedsus.imageswitcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageList = listOf(
            "https://www.zastavki.com/pictures/640x480/2020_White_car_Tesla_Model_3_Volta_2019_on_the_background_of_mountains_139522_29.jpg",
            "https://www.zastavki.com/pictures/640x480/2019_Red_car_Tesla_Roadster_on_the_road_near_the_mountains_131329_29.jpg",
            "https://www.zastavki.com/pictures/640x480/2020_White_car_Tesla_Model_S_Performance__2020_in_the_mountains_140273_29.jpg",
            "https://www.zastavki.com/pictures/640x480/2018_White_electric_car_Tesla_Model_X__2018_128909_29.jpg"
        )
        findViewById<AutoImageSwitcher>(R.id.autoImageSwitcher).showImages(imageList)


        val testText = "<b>test</b> <b>test</b> <b>test</b> <b>test</b> sdasda asdasdasd sadasd <b>test</b>"
        findViewById<TextView>(R.id.textView).text = extractTextByTag(
                text = testText,
                startTag = "<b>",
                endTag = "</b>",
                extractColor = ContextCompat.getColor(applicationContext, R.color.purple_500)
        )

        Glide.with(this)
                .load("https://kassa.rambler.ru/s/StaticContent/P/Img/1904/11/190411183707154.png")
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(findViewById(R.id.imageView1))

        Glide.with(this)
                .load("https://kassa.rambler.ru/s/StaticContent/P/Img/1904/11/190411183707154.png")
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(findViewById(R.id.imageView2))

        Glide.with(this)
                .load("https://kassa.rambler.ru/s/StaticContent/P/Img/1904/11/190411183707154.png")
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(findViewById(R.id.imageView3))
    }
}