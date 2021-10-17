package com.example.albazip.ui.register

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentInputPwBinding

class InputPWFragment : BaseFragment<FragmentInputPwBinding>(
    FragmentInputPwBinding::bind,
    R.layout.fragment_input_pw
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이전 화면으로 이동
        binding.btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, AgreementFragment()).commit()
        }

        // 기본 정보 입력 화면으로 이동
        binding.btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, InputInfoFragment()).commit()
        }

        // 포커스 여부 감지
        binding.etInputPw.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlPwInput.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlPwInput.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        // 포커스 여부 감지
        binding.etConfirmPw.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && binding.etConfirmPw.text.toString().isEmpty())
                binding.rlConfirmPw.background = ContextCompat.getDrawable(requireContext(), R.drawable.rectagnle_yellow_radius)
            else if (hasFocus && binding.etInputPw.text.toString() == binding.etConfirmPw.text.toString()) {
                binding.rlConfirmPw.background = ContextCompat.getDrawable(requireContext(), R.drawable.rectagnle_yellow_radius)
            } else {
                binding.rlConfirmPw.background = ContextCompat.getDrawable(requireContext(), R.drawable.rectangle_custom_white_radius)
            }
        }

        // 비밀번호 정상 입력 감지
        binding.etInputPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                // 비밀번호 입력란 자릿수 감지
                if (binding.etInputPw.text?.length!! >= 6) { // 6자리 이상 입력 되었을 때
                    binding.ivInputCheck.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_checked_correct
                        )
                    )
                } else {
                    binding.ivInputCheck.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_checked_normal
                        )
                    )
                }
            }

            // 비밀번호 일치여부 감지(1)
            override fun afterTextChanged(s: Editable?) {
                if (binding.etConfirmPw.text.toString()
                        .isNotEmpty() && binding.etInputPw.text.toString().isNotEmpty()
                ) { // 확인란이 공백이 아닐 때
                    binding.rlConfirmPw.background =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.rectangle_custom_white_radius
                        )

                    if (binding.etInputPw.text.toString() != binding.etConfirmPw.text.toString()) { // 일치하지 않을 때
                        binding.rlConfirmPw.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.rectagnle_red_radius
                        )
                        binding.ivConfirmCheck.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_checked_normal
                            )
                        )
                        binding.warningTv.visibility = View.VISIBLE
                        deActivateBtn()

                    } else {
                        binding.rlConfirmPw.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.rectangle_custom_white_radius
                        )
                        binding.ivConfirmCheck.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_checked_correct
                            )
                        )

                        binding.warningTv.visibility = View.INVISIBLE
                        activateBtn()

                    }
                }
            }
        })

        // 비밀번호 일치 여부 감지(2)
        binding.etConfirmPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) { // 불일치
                if (binding.etConfirmPw.text.toString().isNotEmpty()) { // 공백이 아닐 때
                    if (binding.etInputPw.text.toString() != binding.etConfirmPw.text.toString()) { // 불일치
                        binding.rlConfirmPw.background =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.rectagnle_red_radius
                            )
                        binding.ivConfirmCheck.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_checked_normal
                            )
                        )

                        binding.warningTv.visibility = View.VISIBLE
                        deActivateBtn()

                    } else { // 일치
                        binding.rlConfirmPw.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.rectagnle_yellow_radius
                        )
                        binding.ivConfirmCheck.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_checked_correct
                            )
                        )
                        binding.warningTv.visibility = View.INVISIBLE
                        activateBtn()
                    }
                } else { // 공백일 때
                    binding.warningTv.visibility = View.INVISIBLE
                }

            }
        })
    }

    // 버튼 활성화 함수
    private fun activateBtn() {
        binding.btnNext.isClickable = true
        binding.btnNext.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_main_yellow_fill_rounded)
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.btnNext.isClickable = false
        binding.btnNext.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_disable_yellow_fill_rounded)
    }

}