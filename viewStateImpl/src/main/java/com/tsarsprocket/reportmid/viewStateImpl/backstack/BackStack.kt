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
        allOpRefs = mutableMapOf()
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

    /**
     * Returns the [ViewStateHolderImpl.globalId] of the holder that owns the operation identified by [uuid],
     * or `null` if no such operation is currently on the stack.
     */
    fun holderIdFor(uuid: UUID): UUID? = allOpRefs[uuid]?.holderUUID

    /**
     * Returns the operation UUIDs ordered from the top of the stack down to and including [uuid].
     * Returns an empty list if [uuid] is not currently present on the stack.
     */
    fun operationIdsFromTopTo(uuid: UUID): List<UUID> {
        val result = mutableListOf<UUID>()
        var cursor = top
        while(cursor != null) {
            result += cursor
            if(cursor == uuid) return result
            cursor = allOpRefs[cursor]?.down
        }
        return emptyList()
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