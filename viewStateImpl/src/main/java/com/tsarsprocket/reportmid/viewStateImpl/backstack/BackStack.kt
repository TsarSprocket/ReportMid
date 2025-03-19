package com.tsarsprocket.reportmid.viewStateImpl.backstack

import android.os.Parcel
import android.os.ParcelUuid
import android.os.Parcelable
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.ViewStateHolderImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import java.util.UUID

internal class BackStack private constructor(
    private var top: UUID?,
    private val allOpRefs: MutableMap<UUID, OpRef>,
) : Parcelable {

    lateinit var holderResolver: UUID.() -> ViewStateHolderImpl?

    private val stackSizePublisher = MutableStateFlow(allOpRefs.size)

    val stackSize: StateFlow<Int>
        get() = stackSizePublisher.asStateFlow()

    constructor() : this(
        top = null,
        allOpRefs = mutableMapOf<UUID, OpRef>()
    )

    private constructor(parcel: Parcel) : this(
        top = parcel.readParcelable<ParcelUuid>(BackStack::class.java.classLoader)?.uuid,
        allOpRefs = parcel.readParcelableArray(BackStack::class.java.classLoader)
            ?.filterIsInstance<ParcelableEntry>()
            .orEmpty()
            .associate { (uuid, opRef) -> uuid to opRef }
            .toMutableMap()
    )

    override fun describeContents() = 0

    fun goBack() {
        top?.let { allOpRefs[it] }?.holderUUID?.holderResolver()?.doGoBack()
    }

    fun push(holder: ViewStateHolderImpl, operationUuid: UUID) {
        val opRef = OpRef(holder.globalId, top)
        allOpRefs[operationUuid] = opRef
        top?.let { allOpRefs[it]?.up = operationUuid }
        top = operationUuid
        stackSizePublisher.value = allOpRefs.size
    }

    /**
     * Removes operation assuming the holder did it already on its side
     */
    fun removeOperation(uuid: UUID) {
        allOpRefs.remove(uuid)?.let { opRef ->
            if(top === uuid) top = opRef.down
            opRef.down?.let { allOpRefs[it]?.up = opRef.up }
            opRef.up?.let { allOpRefs[it]?.down = opRef.down }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeParcelable(top?.let { ParcelUuid(it) }, flags)
            writeParcelableArray(allOpRefs.entries.map { (uuid, opRef) -> ParcelableEntry(uuid, opRef) }.toTypedArray(), flags)
        }
    }

    @Parcelize
    private data class OpRef(
        val holderUUID: UUID,
        var down: UUID?,
        var up: UUID? = null,
    ) : Parcelable

    @Parcelize
    private data class ParcelableEntry(
        val uuid: UUID,
        val opRef: OpRef,
    ) : Parcelable

    companion object CREATOR : Parcelable.Creator<BackStack> {
        override fun createFromParcel(parcel: Parcel?): BackStack? = parcel?.let { BackStack(it) }
        override fun newArray(size: Int): Array<BackStack?> = arrayOfNulls(size)
    }
}