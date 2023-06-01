package com.example.test5.recycler

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ch20_firebase.util.dateToString
import com.example.test5.AddActivity
import com.example.test5.MyApplication
import com.example.test5.model.ItemData
import com.example.test5.databinding.ItemMainBinding
import com.example.test5.util.ItemTouchHelperListener
import java.util.Collections
import java.util.Date

// 원래꺼
//class MyViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

//바꾼거
class MyViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val context: Context, val itemList: MutableList<ItemData>): RecyclerView.Adapter<MyViewHolder>(),
    ItemTouchHelperListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemMainBinding.inflate(layoutInflater))
    }


    //리스트의 사이즈
    override fun getItemCount(): Int {
        return itemList.size
    }

    //아이템을 드래그하면 호출되는 메서드
    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        //Toast.makeText(context, "from_position: $from_position, to_position: $to_position", Toast.LENGTH_SHORT).show()
        Log.d("ItemMove", "from_position: $from_position, to_position: $to_position")

        // 리스트 갱신
        //itemList.removeAt(from_position)
        //itemList.add(to_position, name)
       // Collections.swap(itemList, from_position, to_position)
        //notifyItemMoved(from_position, to_position)
        return true

    }

    //아이템를 스와이프 하면 호출되는 메서드
    override fun onItemSwipe(position: Int) {
        val item = itemList[position]
        Toast.makeText(context, "from_position: $position",  Toast.LENGTH_SHORT).show()
        // 리스트 아이템 삭제
        //itemList.removeAt(position)
        val docId = item.docId
        if (docId != null) {
            MyApplication.db.collection("shoes")
                .document(docId)
                .delete()
                .addOnSuccessListener {
                    notifyItemRemoved(position) }
            // 아이템 삭제되었다고 공지

        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            //itemEmailView.text=data.email
            //itemDateView.text=data.date
            itemContentView.text=data.shoe_name

        }
        if(data.shelf_status == true ) {
            holder.binding.itemExistenceView.text = "보유중"
            holder.binding.itemExistenceView.setTextColor(Color.GREEN)
        }else{
            holder.binding.itemExistenceView.text = "미보유"
            holder.binding.itemExistenceView.setTextColor(Color.RED)
        }
        //스토리지 이미지 다운로드........................
        val imgRef = MyApplication.storage.reference.child("shoeImages/${data.docId}.jpg")
        imgRef.downloadUrl.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Glide.with(context)
                    .load(task.result)
                    .into(holder.binding.itemImageView)
            }

        }
    }
}

