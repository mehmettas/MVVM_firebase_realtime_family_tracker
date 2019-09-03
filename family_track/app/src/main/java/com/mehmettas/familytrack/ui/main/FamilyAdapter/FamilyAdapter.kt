package com.mehmettas.familytrack.ui.main.FamilyAdapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.data.remote.model.family.Member
import com.mehmettas.familytrack.utils.extensions.inflate
import kotlinx.android.synthetic.main.layout_button_add_family.view.*

class FamilyAdapter(
    private var items:ArrayList<Member>,
    private var listener:FamilyAdapterListener
) : RecyclerView.Adapter<FamilyAdapter.FamilViewHolder>()
{
    private var BUTTON_ADD_MEMBER = 1
    private var CONTENT_REGULAR = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilViewHolder {
        if(viewType == BUTTON_ADD_MEMBER)
            return FamilViewHolder(parent.inflate(R.layout.layout_button_add_family))
        return FamilViewHolder(parent.inflate(R.layout.layout_memberlist_item))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FamilViewHolder, position: Int) {
        holder.bind(getItem(position), listener, position)
    }

    fun addData(list: ArrayList<Member>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int):Member = items[position]

    inner class FamilViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item:Member,
            listener:FamilyAdapterListener,
            selectedItem: Int
        ) = with(itemView) {

            if (selectedItem==itemCount.minus(1))
                btnAddFamilyMember.setOnClickListener {
                    listener.onNewFamilyMemberSelected()
                }

            itemView.setOnClickListener {
                    listener.onFamilyMemberSelected(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position==itemCount.minus(1))
            return BUTTON_ADD_MEMBER
        return CONTENT_REGULAR
    }

    interface FamilyAdapterListener{
        fun onFamilyMemberSelected(item: Member)
        fun onNewFamilyMemberSelected()
    }
}