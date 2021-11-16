package com.example.albazip.src.home.manager.opened

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomeOpenedBinding

class HomeOpenedChildFragment: BaseFragment<ChildFragmentHomeOpenedBinding>(
    ChildFragmentHomeOpenedBinding::bind,
    R.layout.child_fragment_home_opened) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // qr 조회 화면으로 이동
        binding.ibtnQrScan.setOnClickListener {
            val nextIntent = Intent(requireContext(),QRShowingActivity::class.java)
            startActivity(nextIntent)
        }
    }
}