package com.most4dev.cryptocurrencycourses.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most4dev.cryptocurrencycourses.models.CryptoModel
import com.most4dev.cryptocurrencycourses.retrofits.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CryptoViewModel(application: Application): AndroidViewModel(application) {

    var retrofitService: RetrofitService = RetrofitService.getInstance(getApplication<Application>())
    private val _listCrypto = MutableLiveData<List<CryptoModel>>()
    val listCrypto: LiveData<List<CryptoModel>>
        get() = _listCrypto
    private val _fiat = MutableLiveData<String>()
    val fiat: LiveData<String>
        get() = _fiat
    val inputCountValue = MutableLiveData<String>()
    val inputCryptocurrency = MutableLiveData<CryptoModel>()


    init {
        getTop100Cryptocurrencies()
    }

    fun getTop100Cryptocurrencies(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofitService.top100Cryptocurrencies(
                "usd",
                "market_cap_desc",
                "100"
            )
            _listCrypto.postValue(response)
        }
    }

    fun calculateCryptoToFiat(count: String, cryptoModel: CryptoModel?){
        val number = count.toDouble()

            if (cryptoModel != null) {
                _fiat.value = (number*cryptoModel.currentPrice).toString()
            }


    }



}