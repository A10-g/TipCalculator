package com.jscoe.akgupta.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
//import java.util.*
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG="MainActivity"
private const val INITIAL_TIP_PERCENT=15
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tipseekBar.progress=INITIAL_TIP_PERCENT
        percenttv.text="$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        tipseekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG,"onProgressChanged $p1")
                percenttv.text="$p1%"
                updateTipDescription(p1)
                computeTipandTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBase.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChanged $p0")
                    computeTipandTotal()
            }
        })

    }

    private fun updateTipDescription(p1: Int) {
        val tipDescription : String
        when(p1){
            in 0..9 -> tipDescription="Poor"
            in 10..14 -> tipDescription="Acceptable"
            in 15..19 -> tipDescription="Good"
            in 20..24 -> tipDescription="Great"
            else -> tipDescription="Amazing"
        }
        tvTipDescriptor.text=tipDescription
        val color=ArgbEvaluator().evaluate(p1.toFloat() / tipseekBar.max,
            ContextCompat.getColor(this,R.color.colorWorstTip),
            ContextCompat.getColor(this,R.color.colorBestTip)
        )as Int
        tvTipDescriptor.setTextColor(color)
    }

    private fun computeTipandTotal() {
        if(etBase.text.isEmpty()){
            tvTipAmount.text=""
            tvTotalamount.text=""
            return
        }
        val baseAmount =etBase.text.toString().toDouble()
        val tipPercent=tipseekBar.progress
        val tipAmount=baseAmount*tipPercent/100
        val totalAmount=baseAmount+tipAmount
        tvTipAmount.text="%.2f".format(tipAmount)
        tvTotalamount.text="%.2f".format(totalAmount)
    }
}