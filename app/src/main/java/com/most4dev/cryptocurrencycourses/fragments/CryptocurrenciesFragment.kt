package com.most4dev.cryptocurrencycourses.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.most4dev.cryptocurrencycourses.R
import com.most4dev.cryptocurrencycourses.adapters.CryptoAdapter
import com.most4dev.cryptocurrencycourses.viewModels.CryptoViewModel
import kotlinx.android.synthetic.main.fragment_cryptocurrencies.*

class CryptocurrenciesFragment : Fragment() {

    private lateinit var cryptoViewModel: CryptoViewModel
    private lateinit var cryptoAdapter: CryptoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cryptocurrencies, container, false)
        cryptoViewModel = ViewModelProvider(this)[CryptoViewModel::class.java]
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()

        cryptoViewModel.listCrypto.observe(viewLifecycleOwner) { value ->
            value.let { show ->
                cryptoAdapter.setItems(show)
            }
        }
    }

    private fun createAdapter() {
        cryptoAdapter = CryptoAdapter(requireContext(), arrayListOf())
        recycler_view_cryptocurrencies.layoutManager = LinearLayoutManager(context)
        recycler_view_cryptocurrencies.adapter = cryptoAdapter
    }
}