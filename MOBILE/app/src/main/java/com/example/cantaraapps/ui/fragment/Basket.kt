package com.example.cantaraapps.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cantaraapps.R
import com.example.cantaraapps.activity.CheckoutActivity
import com.example.cantaraapps.databinding.FragmentBasketBinding

class Basket : Fragment() {
    private lateinit var binding: FragmentBasketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketBinding.inflate(inflater, container, false)

        binding.buttonPesan2.setOnClickListener {
            val intent = Intent(requireActivity(), CheckoutActivity::class.java)
            startActivity(intent)
        }

        val editText = binding.editText
        val btnPlus = binding.btnPlus
        val btnMinus = binding.btnMinus

        btnPlus.setOnClickListener {
            val valueStr = editText.text.toString()

            var value = Integer.parseInt(valueStr)

            value++

            editText.setText(value.toString())
        }

        btnMinus.setOnClickListener {
            val valueStr = editText.text.toString()

            var value = Integer.parseInt(valueStr)

            if (value > 0) {
                value--
                editText.setText(value.toString())
            }
        }

        return binding.root
    }
}