package com.example.albazip.src.main

import android.os.Bundle
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityWorkerMainBinding
import com.example.albazip.src.community.manager.MCommunityFragment
import com.example.albazip.src.community.worker.WCommunityFragment
import com.example.albazip.src.home.manager.MHomeFragment
import com.example.albazip.src.home.worker.WHomeFragment
import com.example.albazip.src.mypage.worker.WMyPageFragment
import com.example.albazip.util.BackPressCloseHandler

// 근무자 시작 화면
class WorkerMainActivity :
    BaseActivity<ActivityWorkerMainBinding>(ActivityWorkerMainBinding::inflate) {
    // 뒤로가기 핸들러
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPressCloseHandler = BackPressCloseHandler(this)

        if(intent.hasExtra("btmTabFlags")) {
            var getFlags = intent.getIntExtra("btmTabFlags", 0)
            if (getFlags == 1){
                binding.workerMainBtmNav.menu.getItem(1).isChecked = true
                supportFragmentManager.beginTransaction().replace(R.id.worker_main_frm, WCommunityFragment())
                    .commitAllowingStateLoss()
            }
        }else{
            supportFragmentManager.beginTransaction().replace(R.id.worker_main_frm, WHomeFragment())
                .commitAllowingStateLoss()
        }


        binding.workerMainBtmNav.run {
            setOnItemSelectedListener {
                when (it.itemId) {

                    // 홈
                    R.id.menu_main_btm_nav_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.worker_main_frm, WHomeFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                    // 소통창
                    R.id.menu_main_btm_nav_community -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.worker_main_frm, WCommunityFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }

                    // 마이페이지
                    R.id.menu_main_btm_nav_mypage -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.worker_main_frm, WMyPageFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                }
                false
            }
        }


    }

    // 뒤로가기 스택 감지
    override fun onBackPressed() {

        if (binding.workerMainBtmNav.selectedItemId == R.id.menu_main_btm_nav_home) {
            //super.onBackPressed()
            backPressCloseHandler.onBackPressed()
        } else {
            binding.workerMainBtmNav.selectedItemId = R.id.menu_main_btm_nav_home
        }
    }
}