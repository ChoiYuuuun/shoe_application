package com.example.test5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
/*val start_remove = findViewById<Button>(R.id.start_remove)
        start_remove.setOnClickListener{
            val intent = Intent(this,LoadingActivity::class.java)
            startActivity(intent)
        }  <-이 코드 item_main.xml에서 넘어가는 부분에 추가하기*/
// 이거 왜 빨간 줄 뜨는지 모르겠다
class LoadingActivity: AppCompatActivity() {

    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()  // firestore instance 받아옴
    private val signalDocRef: DocumentReference = firestore.collection("signal")
        .document("from_app_to_RPi")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        checkRequestValue()
    }

    private fun checkRequestValue() {
        // 초기에 request 값을 1로 설정
        signalDocRef.update("request", true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // request 값을 1로 설정한 후에 감지 시작
                    lifecycleScope.launch {
                        delay(2000) // 2000ms(2초) 동안 지연
                        // 원하는 작업 실행
                        observeRequestValue()
                    }
                } else {
                    // 오류 처리
                }
            }
    }
//계속 파이어베이스 확인함
    private fun observeRequestValue() {
    //변화읽음
        signalDocRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // 오류 처리
                return@addSnapshotListener
            }

            val request = snapshot?.getBoolean("request")
            if (request == false) {
                // request 값이 0이 되면 activity 종료
                finish()
            } else {
                // request 값이 0이 아니면 계속 감시
                observeRequestValue()
            }
        }
    }
}