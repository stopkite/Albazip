package com.playground.albazip.src.update.setworker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemUpdateRvSetWorkerTimeBinding
import com.playground.albazip.src.update.setworker.data.WorkerTimeData
import com.playground.albazip.util.GetTimeDiffUtil

class WorkingTimeAdapter() :
    RecyclerView.Adapter<WorkingTimeAdapter.WorkingTimeViewHolder>() {

    private lateinit var binding: ItemUpdateRvSetWorkerTimeBinding
    var workerTimeList = mutableListOf<WorkerTimeData>()

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int, tag: String)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkingTimeAdapter.WorkingTimeViewHolder {
        binding = ItemUpdateRvSetWorkerTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return WorkingTimeViewHolder(binding)
    }

    inner class WorkingTimeViewHolder(val binding: ItemUpdateRvSetWorkerTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 기본 세팅
        fun setItemList(workerTimeData: WorkerTimeData) {
            binding.apply {
                tvDay.text = workerTimeData.workDay
                tvOpenHour.text = workerTimeData.openTime
                tvCloseHour.text = workerTimeData.closeTime
                ivCheckboxDay.isSelected = workerTimeData.isSelected!!
                tvTotalTime.text = workerTimeData.totalTime
            }
        }

        // 1. 활성화(열리기) - 비활성화(닫히기)
        fun setCbBox(data: WorkerTimeData, position: Int) {
            binding.apply {
                ivCheckboxDay.setOnClickListener {
                    if(ivCheckboxDay.isSelected) { // 활성화 된 상태라면
                        ivCheckboxDay.isSelected = false
                        data.isSelected = false
                        setViewVisibility(false)
                    } else { // 비활성화 된 상태라면
                        ivCheckboxDay.isSelected = true
                        data.isSelected = true
                        setViewVisibility(true)
                    }
                }
            }
        }

        // -> 입력 활성화 여부
        private fun setViewVisibility(flag: Boolean) {
            if (flag) { // 활성화
                binding.apply {
                    llWorkerTime.visibility = View.VISIBLE
                    tvTotalTime.visibility = View.VISIBLE
                    tvDay.isEnabled = true
                }
            } else { // 비활성화
                binding.apply {
                    llWorkerTime.visibility = View.GONE
                    tvTotalTime.visibility = View.GONE
                    tvDay.isEnabled = false
                }
            }
        }

        // 2. 개별 item 요소의 시간을 설정할 수 있게 해준다.
        fun setItemTimeEvent(position: Int) {

            binding.clOpen.setOnClickListener { // 오픈 시간 설정
                itemClickListener.onClick(binding.clOpen, position, "SET_START_HOUR")
            }

            binding.clClose.setOnClickListener { // 마감 시간 설정
                itemClickListener.onClick(binding.clOpen, position, "SET_END_HOUR")
            }
        }

        // 3. 개별 Time 을 받아온 후의 UI를 변경
        fun initItemHourUIAfterInput(data: WorkerTimeData) {
            binding.tvOpenHour.isEnabled = data.openFlag // 텍스트 요소 설정
            binding.tvCloseHour.isEnabled = data.closeFlag
        }
    }

    override fun onBindViewHolder(holder: WorkingTimeAdapter.WorkingTimeViewHolder, position: Int) {
        holder.setItemList(workerTimeList[position])
        holder.setCbBox(workerTimeList[position],position)
        holder.setItemTimeEvent(position)
        holder.initItemHourUIAfterInput(workerTimeList[position])
    }

    override fun getItemCount(): Int = workerTimeList.size

    // 츌근, 퇴근 시간 설정
    fun setItemTimeUI(flags: Int, position: Int, displayTime: String) {

        if (flags == 0) {  // 오픈 시간
            workerTimeList[position].openTime = displayTime
            workerTimeList[position].openFlag = true
        } else { // 마감 시간
            workerTimeList[position].closeTime = displayTime
            workerTimeList[position].closeFlag = true
        }

        // 총시간 구하기
        workerTimeList[position].totalTime = GetTimeDiffUtil().getTimeDiffTxt(
            workerTimeList[position].openTime!!,
            workerTimeList[position].closeTime!!
        )

        notifyItemChanged(position)
    }

    // 선택 된 게 아무 것도 없을 때
    fun isNoneSelected(): Boolean {
        if(workerTimeList.all { it.isSelected == false }){
            return true
        }
        return false
    }
}