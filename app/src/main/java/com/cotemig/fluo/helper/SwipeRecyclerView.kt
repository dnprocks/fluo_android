package com.cotemig.fluo.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.cotemig.fluo.R
import com.cotemig.fluo.ui.activities.MainActivity

class SwipeRecyclerViewHelper {

    companion object {

        fun createSwipe(
            context: Context,
            recyclerView: RecyclerView,
            listener: SwipeRecyclerViewListener
        ) {

            var context = context as MainActivity

            val touchHelper = ItemTouchHelper(
                object : ItemTouchHelper.Callback() {

                    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete)
                    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
                    private val intrinsicHeight = deleteIcon!!.intrinsicHeight
                    private val background = ColorDrawable()
                    private val clearPaint =
                        Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

                    private fun clearCanvas(
                        c: Canvas?,
                        left: Float,
                        top: Float,
                        right: Float,
                        bottom: Float
                    ) {
                        c?.drawRect(left, top, right, bottom, clearPaint)
                    }

                    override fun onChildDraw(
                        c: Canvas,
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        dX: Float,
                        dY: Float,
                        actionState: Int,
                        isCurrentlyActive: Boolean
                    ) {

                        val itemView = viewHolder.itemView
                        val itemHeight = itemView.bottom - itemView.top
                        val isCanceled = dX == 0f && !isCurrentlyActive

                        if (isCanceled) {
                            clearCanvas(
                                c,
                                itemView.right + dX,
                                itemView.top.toFloat(),
                                itemView.right.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            super.onChildDraw(
                                c,
                                recyclerView,
                                viewHolder,
                                dX,
                                dY,
                                actionState,
                                isCurrentlyActive
                            )
                            return
                        }

                        background.color = context.resources.getColor(R.color.transparent, null)
                        background.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                        background.draw(c)

// Calculate position of delete icon
                        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                        val deleteIconRight = itemView.right - deleteIconMargin
                        val deleteIconBottom = deleteIconTop + intrinsicHeight

// Draw the delete icon
                        deleteIcon!!.setBounds(
                            deleteIconLeft,
                            deleteIconTop,
                            deleteIconRight,
                            deleteIconBottom
                        )
                        deleteIcon!!.draw(c)

                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )

                    }

                    override fun getMovementFlags(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder
                    ): Int {
                        return makeMovementFlags(
                            0,
                            ItemTouchHelper.LEFT
                        )
                    }

                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        MaterialDialog.Builder(context)
                            .theme(Theme.LIGHT)
                            .title(R.string.confirm)
                            .content(R.string.delete_task)
                            .positiveText(R.string.confirm_yes)
                            .negativeText(R.string.confirm_no)
                            .onPositive { dialog, which ->
                                listener.removeSelectedItem(viewHolder.adapterPosition)
                            }
                            .onNegative { dialog, which ->
                                listener.reload()
                            }
                            .show()

                    }

                }
            )

            touchHelper.attachToRecyclerView(recyclerView)
        }

        interface SwipeRecyclerViewListener {
            fun removeSelectedItem(position: Int)
            fun reload()
        }

    }

}
