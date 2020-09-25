package com.tsarsprocket.reportmid.controller

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

open class CardViewHolderWithDisposer(val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    var disposer = CompositeDisposable()
}