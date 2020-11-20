package com.tsarsprocket.reportmid.tools

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap


fun getRoundedCroppedDrawable(src: Drawable): Drawable {
    val bmp = src.toBitmap()
    val outWidth = bmp.width
    val outHeight = bmp.height
    val outBmp = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(outBmp)

    val paintColor = Paint(Paint.ANTI_ALIAS_FLAG)
    val rectF = RectF(Rect(0, 0, outWidth, outHeight))
    canvas.drawRoundRect(rectF, outWidth.toFloat() / 2, outHeight.toFloat() / 2, paintColor)

    val paintImage = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP) }

    canvas.drawBitmap(bmp, 0f, 0f, paintImage)

    return BitmapDrawable(outBmp)
}