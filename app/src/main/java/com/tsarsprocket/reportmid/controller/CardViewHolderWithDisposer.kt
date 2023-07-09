package com.tsarsprocket.reportmid.controller

import androidx.cardview.widget.CardView
import io.reactivex.disposables.CompositeDisposable

open class CardViewHolderWithDisposer(cardView: CardView) : CardViewHolder(cardView) {
    val disposer = CompositeDisposable()
}