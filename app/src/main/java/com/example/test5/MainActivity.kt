package com.example.test5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch20_firebase.util.myCheckPermission

import com.example.test5.databinding.ActivityMainBinding
import com.example.test5.model.ItemData
import com.example.test5.recycler.MyAdapter
import com.example.test5.util.ItemTouchHelperCallback
import java.util.Collections
import com.example.test5.ListActivity


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myCheckPermission(this) // 작성한 함수임
        binding.addFab.setOnClickListener {
            if(MyApplication.checkAuth()){
                startActivity(Intent(this, ListActivity::class.java))
            }
            else {
                Toast.makeText(this, "인증진행해주세요 ",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if(!MyApplication.checkAuth()){
            binding.logoutTextView.visibility= View.VISIBLE
            binding.mainRecyclerView.visibility=View.GONE
        }else {
            binding.logoutTextView.visibility= View.GONE
            binding.mainRecyclerView.visibility=View.VISIBLE
            makeRecyclerView()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, AuthActivity::class.java))
        return super.onOptionsItemSelected(item)
    }


    // 기본 화면에 신발 목록 나타나는 방식
    private fun makeRecyclerView(){
        //var shoesRef = MyApplication.db.collection("shoes")
        //shoesRef.orderBy("order")
        MyApplication.db.collection("shoes")
            .orderBy("order")
            .get()
            .addOnSuccessListener { result->
                val itemList = mutableListOf<ItemData>()
                for(document in result){
                    val item = document.toObject(ItemData::class.java)
                    item.docId=document.id
                    itemList.add(item)
                    //Collections.swap(itemList,0,1)
                }


                //Collections.swap(itemList,0,1)
                //itemList.sortBy{it.num}
                binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.mainRecyclerView.adapter = MyAdapter(this, itemList)

                // 여기서 부터는 순서 바꾸기 해보기
                //val itemTouchHelperCallback = ItemTouchHelperCallback(MyAdapter(this,itemList))
                //  ItemTouchHelper의 생성자로 ItemTouchHelper.Callback객체 세팅
                //val helper = ItemTouchHelper(itemTouchHelperCallback)
                //helper.attachToRecyclerView(binding.mainRecyclerView)



            }
            .addOnFailureListener{exception->
                Log.d("yun","error..getting document..",exception)
                Toast.makeText(this,"서버 데이터 획득 실패",Toast.LENGTH_SHORT).show()

            }

    }

}