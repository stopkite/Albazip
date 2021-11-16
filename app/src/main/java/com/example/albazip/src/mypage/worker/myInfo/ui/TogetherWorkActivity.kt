package com.example.albazip.src.mypage.worker.myInfo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityTogetherWorkBinding
import com.example.albazip.src.mypage.common.workerdata.cotask.data.GetCoTaskInfoResponse
import com.example.albazip.src.mypage.common.workerdata.cotask.network.CoTaskFragmentView
import com.example.albazip.src.mypage.common.workerdata.cotask.network.CoTaskService
import com.example.albazip.src.mypage.worker.adapter.OutWorkListAdapter
import com.example.albazip.src.mypage.worker.data.local.InWorkListData
import com.example.albazip.src.mypage.worker.data.local.OutWorkListData
import com.example.albazip.src.mypage.worker.myInfo.data.AllCoWorkData

class TogetherWorkActivity:BaseActivity<ActivityTogetherWorkBinding>(ActivityTogetherWorkBinding::inflate), CoTaskFragmentView{
    private lateinit var outWorkListAdapter:OutWorkListAdapter

    // 모든 데이터 받아오기
    val saveAllData = ArrayList<AllCoWorkData>()

    // 날짜는 최신순으로 내림차순 정렬
    // 날짜를 담을 배열 생성
    val dateList = ArrayList<String>()

    // out rv를 담을 배열
    val outWorkList = ArrayList<OutWorkListData>()

    // in rv를 담을 배열
    val inWorkListOne = ArrayList<InWorkListData>()
    val inWorkListTwo = ArrayList<InWorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoTaskService(this).tryCoTask()
        showLoadingDialog(this)

        // 첫 번째 보조 rv 생성
        //inWorkListOne.add(InWorkListData("우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))

        // 두 번째 보조 rv 생성
        //inWorkListTwo.add(InWorkListData("우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))
        //inWorkListTwo.add(InWorkListData("우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))

        // 본 rv 생성
        //outWorkList.add(OutWorkListData("2021.07.04.",inWorkListOne))
        //outWorkList.add(OutWorkListData("2021.07.24.",inWorkListTwo))

    }

    override fun onCoTaskGetSuccess(response: GetCoTaskInfoResponse) {
        dismissLoadingDialog()

        if (response.code == 200) {
            // 완료 업무 수 표시
            binding.tvLateCnt.text = response.data.size.toString()

            if(response.data.size == 0){
                binding.clNoListView.visibility = View.VISIBLE
            }

            // 모든 데이터 받아오기
            for (i in 0 until response.data.size) {
                saveAllData.add(
                    AllCoWorkData(
                        response.data[i].title,
                        response.data[i].content,
                        response.data[i].complete_date
                    )
                )
            }

            // 날짜 리스트 배열에 저장하기
            for (i in 0 until response.data.size)
                dateList.add(response.data[i].complete_date.substring(0, 10))

            // 날짜 리스트 중복 제거하기
            val pureDateList = dateList.distinct()

            val innerRv = ArrayList<InWorkListData>()
            val outterRv = ArrayList<ArrayList<InWorkListData>>()

            //for (j in 0 until dateList.size){
            //    for(i in 0 until response.data.size){
            //        if(response.data[i].complete_date.substring(0,10) == dateList[j]){
            //            innerRv.add(InWorkListData(response.data[i].complete_date.substring(0,10),response.data[i].content,"완료 "+response.data[i].complete_date.substring(11,16)))
           //         }
           //     }
           // }

            // 서버에 있는 공동업무 리스트 데이터 모두 받아오기
            for(i in 0 until response.data.size){
                innerRv.add(InWorkListData(response.data[i].complete_date.substring(0,10),response.data[i].content,"완료 "+response.data[i].complete_date.substring(11,16)))
            }

            // 날짜 데이터에
            for(j in 0 until innerRv.size){
                for(k in 0 until pureDateList.size)
                if(innerRv[j].getDate == pureDateList[k]){
                }
            }

            outterRv.add(innerRv)

            Log.d("testing",innerRv.toString())
            Log.d("innerRv[0].getDate",innerRv[0].getDate)
            Log.d("innerRv[1].getDate",innerRv[1].getDate)
            Log.d("innerRv[2].getDate",innerRv[2].getDate)


//            for(i in 0 until response.data.size){
//                for(j in 0 until pureDateList.size){
//                    if(response.data[i].complete_date.substring(0,10) == pureDateList[j]){
//                        innerRv.add(InWorkListData(response.data[i].complete_date.substring(0,10),response.data[i].content,"완료 "+response.data[i].complete_date.substring(11,16)))
//                        Log.d("innerRv",innerRv.toString())
//                        if(innerRv[i].getDate == pureDateList[j]) {
//                            outterRv.add(ArrayList(innerRv))
//                        }
//                        //outterRv.add(ArrayList<InWorkListData>.add(InWorkListData(response.data[i].complete_date.substring(0,10),response.data[i].content,"완료 "+response.data[i].complete_date.substring(11,16))))
//                        //outWorkList.add(OutWorkListData(pureDateList[j].replace("-",".")+".",innerRv))
//                    }
//                }
//            }

            Log.d("Please",outterRv.toString())

           // for (i in 0 until pureDateList.size){
           //     outWorkList.add(OutWorkListData(pureDateList[i].replace("-",".")+".",outterRv[i]))
           // }

            // out rv의 adapter 객체 생성 후 데이터 전달
            outWorkListAdapter = OutWorkListAdapter(this,outWorkList)

            // out rv의 레이아웃 설정
            binding.rvRecord.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false)

            binding.rvRecord.adapter = outWorkListAdapter

            // showCustomToast(response.message.toString())
        }
    }

    override fun onCoTaskGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}