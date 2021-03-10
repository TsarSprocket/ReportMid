package com.tsarsprocket.reportmid.controller

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

open class CardViewHolderWithDisposer(cardView: CardView) : CardViewHolder(cardView) {
    val disposer = CompositeDisposable()
}