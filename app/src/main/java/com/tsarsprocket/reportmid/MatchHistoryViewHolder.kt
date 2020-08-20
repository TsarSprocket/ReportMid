package com.tsarsprocket.reportmid

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

class MatchHistoryViewHolder( val cardView: CardView): RecyclerView.ViewHolder( cardView ) {
    var allDisposables = CompositeDisposable()
}