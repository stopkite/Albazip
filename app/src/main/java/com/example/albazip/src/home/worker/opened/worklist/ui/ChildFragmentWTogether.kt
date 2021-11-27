package com.example.albazip.src.home.worker.opened.worklist.ui

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWTodoListBinding
import com.example.albazip.databinding.DialogTodoAllDoneBinding
import com.example.albazip.src.home.worker.opened.worklist.adapter.HTogetherDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.adapter.HUnDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData
import com.example.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData
import com.example.albazip.src.home.worker.opened.worklist.network.WTodayTaskResult

class ChildFragmentWTogether(data: WTodayTaskResult?) : BaseFragment<ChildFragmentWTodoListBinding>(
    ChildFragmentWTodoListBinding::bind,
    R.layout.child_fragment_w_todo_list) {

    private var ResultData = data

    // 미완료 리스트
    private var unDoneList = ArrayList<HUnDoneWorkListData>()
    private lateinit var unDoneAdapter:HUnDoneAdapter

    // 완료 리스트
    private var doneList = ArrayList<HDoneWorkListData>()
    private lateinit var doneAdpater:HTogetherDoneAdapter

    // 다이얼로그 바인딩
    private lateinit var dialogBinding: DialogTodoAllDoneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogBinding = DialogTodoAllDoneBinding.inflate(layoutInflater)

        // 미완료 리스트가 없으면 (배열 개수 0)
        //unDoneList.add(HUnDoneWorkListData(0,"제목1","내용1","작성 날짜 및 작성자"))
        if(ResultData?.coTask?.nonComCoTask?.size != null){
            for(i in 0 until ResultData?.coTask?.nonComCoTask!!.size){

                var content = ResultData!!.coTask.nonComCoTask[i].taskContent
                if (content == "null" || content.isEmpty()){
                    content = "내용없음"
                }

                var writerAndDay = ResultData!!.coTask.nonComCoTask[i].writerTitle + " · " + ResultData!!.coTask.nonComCoTask[i].writerName + ResultData!!.coTask.nonComCoTask[i].registerDate.substring(0,9)

                unDoneList.add(HUnDoneWorkListData(0,ResultData!!.coTask.nonComCoTask[i].takTitle,content,writerAndDay))
            }
        }
        unDoneAdapter = HUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
        binding.rvUndone.adapter = unDoneAdapter

        // 미완료 cnt
        binding.tvUndoneCnt.text = unDoneList.size.toString()

        if(ResultData?.coTask?.comCoTask?.size != null){
            for(i in 0 until ResultData?.coTask?.comCoTask!!.size){
                doneList.add(HDoneWorkListData(0,ResultData!!.coTask.comCoTask[i].takTitle,"완료 "+ResultData!!.coTask.comCoTask[i].completeTime))
            }
        }
        // 완료 리스트가 없으면 (배열 개수 0)
        //doneList.add(HDoneWorkListData(0,"제목1","시간"))
        doneAdpater = HTogetherDoneAdapter(parentFragmentManager,requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        // 완료 cnt
        binding.tvDoneCnt.text = doneList.size.toString()

        if (unDoneList.size == 0 && doneList.size == 0){    //없무 없음
            binding.clNoBothWork.visibility = View.VISIBLE
        }else{ // 없무존재
            binding.clNoBothWork.visibility = View.GONE
        }

        if(unDoneList.size == 0 && doneList.size !=0){ // 모든 업무 완료
            binding.rlNoUndoneWork.visibility = View.VISIBLE
        }else{
            binding.rlNoUndoneWork.visibility = View.GONE
        }

        if(unDoneList.size !=0 && doneList.size ==0){ // 완료된 업무가 없어요
            binding.rlNoDoneWork.visibility = View.VISIBLE
        }else{
            binding.rlNoUndoneWork.visibility = View.GONE
        }

    }
}