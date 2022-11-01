package com.most4dev.cryptocurrencycourses.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.most4dev.cryptocurrencycourses.R
import com.most4dev.cryptocurrencycourses.adapters.CalculatorAdapter
import com.most4dev.cryptocurrencycourses.models.CryptoModel
import com.most4dev.cryptocurrencycourses.showSnackBar
import com.most4dev.cryptocurrencycourses.viewModels.CryptoViewModel
import kotlinx.android.synthetic.main.fragment_calculator.*

class CalculatorFragment : Fragment() {

    private lateinit var cryptoViewModel: CryptoViewModel
    private lateinit var calculatorAdapter: CalculatorAdapter
    private var cryptoModelCurrent: CryptoModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calculator, container, false)
        cryptoViewModel = ViewModelProvider(this)[CryptoViewModel::class.java]
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
        cryptoViewModel.listCrypto.observe(viewLifecycleOwner) { value ->
            value.let { show ->
                calculatorAdapter.setItems(show)
            }
        }

        cryptoViewModel.fiat.observe(viewLifecycleOwner) { valueFiat ->
            valueFiat.let {
                inputEditTextResult.setText(it)
            }
        }

        cryptoViewModel.inputCountValue.observe(viewLifecycleOwner){countValue ->
            //inputEditTextCount.setText(countValue)
            checkEmptyData(cryptoModelCurrent)
        }

        cryptoViewModel.inputCryptocurrency.observe(viewLifecycleOwner){inputCryptocurrency ->
            //autoCompleteTextViewCryptocurrencies.setText(inputCryptocurrency.name)
            checkEmptyData(inputCryptocurrency)
        }

        autoCompleteTextViewCryptocurrencies.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, id ->
                val selectedCryptoModel = parent.adapter.getItem(position) as CryptoModel?
                autoCompleteTextViewCryptocurrencies.setText(selectedCryptoModel?.name)
                selectedCryptoModel?.let {
                    //checkEmptyData(it)
                    cryptoViewModel.inputCryptocurrency.value = it
                }
            }

        inputEditTextCount.addTextChangedListener {
            //Log.d("TAGING", )
            //checkEmptyData(cryptoModelCurrent)
            cryptoViewModel.inputCountValue.value = it.toString()
        }


    }

    fun checkEmptyData(cryptoModel: CryptoModel?) {
        cryptoModel.let {
            cryptoModelCurrent = cryptoModel
            if (inputEditTextCount.text != null &&
                inputEditTextCount.text!!.isNotEmpty() &&
                autoCompleteTextViewCryptocurrencies.text.toString().isNotEmpty()
            ) {
                cryptoViewModel.calculateCryptoToFiat(
                    cryptoViewModel.inputCountValue.value.toString(),
                    cryptoModel
                )
            } else {
                constraintCalculator.showSnackBar(getString(R.string.enter_valid_data))
            }
        }
    }

    private fun createAdapter() {
        calculatorAdapter = CalculatorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            arrayListOf()
        )
        autoCompleteTextViewCryptocurrencies.setAdapter(
            calculatorAdapter
        )
    }

}