package com.content.mercy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_graph.*

class GraphActivity : AppCompatActivity() {

    private var mThread = Thread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        setGraph()
    }

    override fun onDestroy() {
        mThread.interrupt()
        mThread.join()
        super.onDestroy()
    }

    private fun setGraph() {
        /*
        chart.apply {
            invalidate()
            clear()
        }
        */
        val data = arrayListOf(
            BarEntry(0f, 100f),
            BarEntry(1f, 200f),
            BarEntry(2f, 300f),
            BarEntry(3f, 400f),
            BarEntry(4f, 500f),
            BarEntry(5f, 600f),
            BarEntry(6f, 700f)
        )
        val emotions = arrayListOf(
            "Anger",
            "Disgust/Contempt",
            "Afraid",
            "Happiness",
            "Sadness",
            "Surprise",
            "Neutral"
        )

        val barDataset = BarDataSet(data, "Realtime Emotion").apply {
            //color = ContextCompat.getColor(this@GraphActivity, R.color.colorAccent)
            colors = listOf(
                ContextCompat.getColor(this@GraphActivity, R.color.colorAccent),
                ContextCompat.getColor(this@GraphActivity, R.color.colorPrimary),
                ContextCompat.getColor(this@GraphActivity, R.color.colorPrimaryDark)
            )
        }
        //chart.animateY(5000)
        val barData = BarData(barDataset).apply {
            barWidth = 0.9f
        }

        val xAxis = chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String = emotions[value.toInt()]
            }
        }
        val yAxisLeft = chart.axisLeft.apply {

        }
        val yAxisRight = chart.axisRight.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }

        chart.description.text = ""
        chart.data = barData
        chart.setFitBars(true)
        chart.invalidate()

        button.setOnClickListener {
            for (i in 0 until data.size) {
                Log.d("Graph", "Data index: $i")
                //data.set(i, BarEntry(i.toFloat(), Math.random().toFloat() * 1000 % 1000))
                //data[i] = BarEntry(i.toFloat(), Math.random().toFloat() * 1000 % 1000)
                data[i].y = Math.random().toFloat() * 1000 % 1000
            }
            chart.notifyDataSetChanged()
            chart.invalidate()
            chart.animateY(1000)
        }

        /*
        mThread = Thread(Runnable {
            while (true) {
                val x = lineDataset.entryCount + 1
                Log.d("Graph", "Entrycount: $x")
                lineData.addEntry(Entry(x.toFloat(), Math.random().toFloat() % 1), 0)
                chart.notifyDataSetChanged()
                runOnUiThread {
                    chart.invalidate()
                }
                Thread.sleep(1000)
            }
        })
        mThread.start()
        */
    }
}
