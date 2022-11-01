package com.most4dev.cryptocurrencycourses.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.most4dev.cryptocurrencycourses.R
import com.most4dev.cryptocurrencycourses.models.CryptoModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cryprocurrencies.view.*
import java.util.*

class CryptoAdapter(
    private var context: Context,
    private var listCryptocurrencies: List<CryptoModel>
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {

        return CryptoLayoutHolder(
            R.layout.item_cryprocurrencies,
            parent
        )

    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(context, listCryptocurrencies[position])
    }

    override fun getItemCount(): Int {
        return listCryptocurrencies.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<CryptoModel>) {
        listCryptocurrencies = list
        notifyDataSetChanged()
    }

    open class CryptoLayoutHolder(
        @LayoutRes val layoutRes: Int,
        parent: ViewGroup
    ) : CryptoViewHolder(
        from(parent.context).inflate(
            layoutRes,
            parent,
            false
        )
    )

    open class CryptoViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(
        containerView
    ), LayoutContainer {

        fun bind(context: Context, cryptoModel: CryptoModel) {
            Glide.with(context).load(cryptoModel.image).into(containerView.image_item_crypto)

            containerView.market_cap.text =
                StringBuilder("#").append(cryptoModel.marketCapRank).toString()

            containerView.title_crypto.text =
                StringBuilder(cryptoModel.name)
                    .append("(")
                    .append(cryptoModel.symbol.uppercase(Locale.ROOT))
                    .append(")")
                    .toString()

            containerView.current_price.text =
                StringBuilder(context.getString(R.string.price))
                    .append(": ")
                    .append(cryptoModel.currentPrice)
                    .append("$")
                    .toString()

            containerView.total_volume.text =
                StringBuilder(context.getString(R.string.total_volume))
                    .append(": ")
                    .append(cryptoModel.totalVolume)

            containerView.high_24h.text =
                StringBuilder(context.getString(R.string.high_24h))
                    .append(": ")
                    .append(cryptoModel.high24h)

            containerView.low_24h.text =
                StringBuilder(context.getString(R.string.low_24h))
                    .append(": ")
                    .append(cryptoModel.low24h)

            containerView.ath.text =
                StringBuilder(context.getString(R.string.ath))
                    .append(": ")
                    .append(cryptoModel.ath)
                    .append("$")

            containerView.atl.text =
                StringBuilder(context.getString(R.string.atl))
                    .append(": ")
                    .append(cryptoModel.atl)
                    .append("$")


        }

    }

}