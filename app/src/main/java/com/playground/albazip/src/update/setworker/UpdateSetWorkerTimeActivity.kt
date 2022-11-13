package com.playground.albazip.src.update.setworker

import WorkingTimePickerBottomSheetDialog
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityUpdateSetWorkerTimeBinding
import com.playground.albazip.src.update.runtime.adater.RunningTimeAdapter
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.src.update.setworker.adapter.WorkingTimeAdapter
import com.playground.albazip.src.update.setworker.custom.SetAllWorkTimePickerBottomSheetDialog
import com.playground.albazip.src.update.setworker.custom.WorkTimeCancelBottomSheetDialog
import com.playground.albazip.src.update.setworker.data.WorkerTimeData
import com.playground.albazip.util.GetTimeDiffUtil

class UpdateSetWorkerTimeActivity :
    BaseActivity<ActivityUpdateSetWorkerTimeBinding>(ActivityUpdateSetWorkerTimeBinding::inflate),
    WorkingTimePickerBottomSheetDialog.BottomSheetClickListener,
    SetAllWorkTimePickerBottomSheetDialog.BottomSheetClickListener {

    private lateinit var workingTimeAdapter: WorkingTimeAdapter
    var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtnEvent()
        initAllSameBtn()
        initRVAdapter()

        initDoneBtn()

        getIntentRv()
    }

    // 이미 들어간 데이터가 있다면 다음과 같이 설정
    private fun getIntentRv() {
        if(intent.getBooleanExtra("workingTimeFlag",false)) {
            workingTimeAdapter =
                WorkingTimeAdapter(
                    { setDoneBtnVisibilityOn() },
                    { setDoneBtnVisibilityOff() },
                    { setDoneOn() },
                    { setDoneOff() },
                    true)
            workingTimeAdapter.workerTimeList = intent.getSerializableExtra("adapterList") as ArrayList<WorkerTimeData>
            binding.rvWorkerTime.itemAnimator = null
            binding.rvWorkerTime.adapter = workingTimeAdapter
            setRvItemClickEvent()
        }
    }

    // 완료 이벤트
    private fun initDoneBtn() {
        binding.tvSetWorkTimeDone.setOnClickListener {
            val returnIntent = Intent()

            val adapterList = workingTimeAdapter.workerTimeList

            returnIntent.putExtra(
                "adapterList",
                adapterList as ArrayList<WorkerTimeData>
            )

            returnIntent.putExtra("workingTimeFlag", true)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

    // 뒤로가기 이벤트
    private fun initBackBtnEvent() {
        binding.ivRunningTimeBackBtn.setOnClickListener {
            if (intent.getBooleanExtra("workingTimeFlag",false)) {
                finish()
            } else {
                WorkTimeCancelBottomSheetDialog { finish() }.show(supportFragmentManager, "BACK_EVENT")
            }
        }
    }

    // 모든 근무 시간이 같아요
    private fun initAllSameBtn() {
        binding.ivCheckboxRunningTimeCheckbox.setOnClickListener {
            if (workingTimeAdapter.isNoneSelected()) {
                showCustomToast("시간을 설정할 근무일을 선택해주세요.")
            } else {
                SetAllWorkTimePickerBottomSheetDialog().show(supportFragmentManager, "SET_ALL_TIME")
            }
        }
    }

    private fun initRVAdapter() {
        workingTimeAdapter =
            WorkingTimeAdapter(
                { setDoneBtnVisibilityOn() },
                { setDoneBtnVisibilityOff() },
                { setDoneOn() },
                { setDoneOff() },
                false)
        workingTimeAdapter.workerTimeList.addAll(
            listOf(
                WorkerTimeData("월요일"),
                WorkerTimeData("화요일"),
                WorkerTimeData("수요일"),
                WorkerTimeData("목요일"),
                WorkerTimeData("금요일"),
                WorkerTimeData("토요일"),
                WorkerTimeData("일요일")
            )
        )
        binding.rvWorkerTime.adapter = workingTimeAdapter
        binding.rvWorkerTime.animation = null
        binding.rvWorkerTime.itemAnimator = null

        setRvItemClickEvent()
    }

    private fun setRvItemClickEvent() {
        workingTimeAdapter.setItemClickListener(object : WorkingTimeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, tag: String) {

                selectedPosition = position // 어떤 아이템이 선택되었는지 포시션 받기

                when (tag) {
                    "SET_START_HOUR" -> WorkingTimePickerBottomSheetDialog(0).show(
                        supportFragmentManager,
                        "SET_START_HOUR"
                    )
                    "SET_END_HOUR" -> WorkingTimePickerBottomSheetDialog(1).show(
                        supportFragmentManager,
                        "SET_END_HOUR"
                    )
                }

            }
        })
    }

    override fun onTimeSelected(h: String, m: String, flag: Int) {
        val displayTime = GetTimeDiffUtil().getDisplayTime(h, m)

        if (flag == 0) { // 오픈시간 설정
            workingTimeAdapter.setItemTimeUI(0, selectedPosition, displayTime)

            // 마감시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (workingTimeAdapter.workerTimeList[selectedPosition].closeFlag) {
                areYou24Hour(0)
            }

        } else { // 마감시간 설정
            workingTimeAdapter.setItemTimeUI(1, selectedPosition, displayTime)

            // 오픈시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (workingTimeAdapter.workerTimeList[selectedPosition].openFlag) {
                areYou24Hour(1)
            }
        }
    }

    // 시간이 같을 때 재설정하게 하기
    private fun areYou24Hour(flags: Int) {
        // 시간이 같을 때 24시간 여부 묻기
        if (workingTimeAdapter.workerTimeList[selectedPosition].totalTime == "0시간") {
            if (flags == 0) { // 출근 다시 받기
                WorkingTimePickerBottomSheetDialog(0).show(
                    supportFragmentManager,
                    "SET_START_HOUR"
                )
                showCustomToast("퇴근 시간과 같아요. 시간을 다시 설정해주세요.")
            } else { // 퇴근 다시 받기
                WorkingTimePickerBottomSheetDialog(1).show(
                    supportFragmentManager,
                    "SET_END_HOUR"
                )
                showCustomToast("출근 시간과 같아요. 시간을 다시 설정해주세요.")
            }
        }
    }

    // 선택 시간 일괄 변경
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onTimeAllTimeSelected(
        allOpenHour: String,
        allCloseHour: String,
        allTotalTime: String,
    ) {
        setDoneBtnVisibilityOn()
        workingTimeAdapter.setAllTime(allOpenHour, allCloseHour, allTotalTime)
    }

    private fun setDoneOn() {
        binding.tvSetWorkTimeDone.isEnabled = true
    }

    private fun setDoneOff() {
        binding.tvSetWorkTimeDone.isEnabled = false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDoneBtnVisibilityOff() {
        Glide.with(this).load(resources.getDrawable(R.drawable.ic_circle_check_inactive, null))
            .into(binding.ivCheckboxRunningTimeCheckbox)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDoneBtnVisibilityOn() {
        Glide.with(this).load(resources.getDrawable(R.drawable.ic_circle_check_active, null))
            .into(binding.ivCheckboxRunningTimeCheckbox)
    }
}