package com.most4dev.cryptocurrencycourses.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.most4dev.cryptocurrencycourses.models.CryptoModel
import java.util.*

class CalculatorAdapter(
    private val mContext: Context,
    private val mLayoutResourceId: Int,
    listCryptocurrencies: List<CryptoModel>
) :
    ArrayAdapter<CryptoModel>(mContext, mLayoutResourceId, listCryptocurrencies) {
    private var cryptocurrencies: List<CryptoModel> = ArrayList(listCryptocurrencies)
    private var filteredListCrypto: List<CryptoModel> = ArrayList(listCryptocurrencies)

    override fun getCount(): Int {
        return cryptocurrencies.size
    }

    override fun getItem(position: Int): CryptoModel {
        return cryptocurrencies[position]
    }

    override fun getItemId(position: Int): Long {
        return cryptocurrencies[position].marketCap
    }

    fun setItems(listCryptocurrencies: List<CryptoModel>) {
        cryptocurrencies = listCryptocurrencies
        filteredListCrypto = listCryptocurrencies
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = (mContext as Activity).layoutInflater
            convertView = inflater.inflate(mLayoutResourceId, parent, false)
        }
        try {
            val cryptoModel: CryptoModel = getItem(position)
            val view: TextView = convertView as TextView? ?: LayoutInflater.from(context)
                .inflate(mLayoutResourceId, parent, false) as TextView
            view.text = cryptoModel.name
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView!!
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                Log.d("TAGING", "publishResults")
                cryptocurrencies = filterResults.values as List<CryptoModel>
                if (filterResults.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                Log.d("TAGING", "performFiltering")

                val results = FilterResults()

                val filterText: String = charSequence.toString().lowercase(Locale.ROOT)
                if (filterText.isEmpty()) {
                    synchronized(this) {
                        results.values = filteredListCrypto
                        results.count = filteredListCrypto.size
                    }
                } else {
                    val filterList: ArrayList<CryptoModel> = ArrayList<CryptoModel>()
                    val unfilterList: ArrayList<CryptoModel> = ArrayList<CryptoModel>()
                    synchronized(this) { unfilterList.addAll(filteredListCrypto) }
                    var i = 0
                    val l: Int = unfilterList.size
                    while (i < l) {
                        val m: CryptoModel = unfilterList[i]
                        if (m.name.lowercase(Locale.ROOT).contains(filterText)) {
                            filterList.add(m)
                        }
                        i++
                    }
                    results.values = filterList
                    results.count = filterList.size
                }

                return results
            }
        }
    }
}