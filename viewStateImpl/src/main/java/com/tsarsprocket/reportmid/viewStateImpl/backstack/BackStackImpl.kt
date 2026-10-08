package com.tsarsprocket.reportmid.viewStateImpl.backstack

import android.os.Parcel
import android.os.ParcelUuid
import android.os.Parcelable
import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStackEntry
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.InternalViewStateHolder
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.ViewStateHolderImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import java.util.UUID

internal class BackStackImpl(
    private var top: UUID?,
    private val allOpRefs: MutableMap<UUID, OpRef>,
) : InternalBackStack {

    override var holderResolver: UUID.() -> InternalViewStateHolder? = { null }
        set(value) {
            field = value
            refreshPublishers()
        }

    private val stackSizePublisher = MutableStateFlow(allOpRefs.size)
    private val mutableEntries = MutableStateFlow(computeEntries())

    override val stackSize: StateFlow<Int>
        get() = stackSizePublisher.asStateFlow()

    override val entriesFlow: StateFlow<List<BackStackEntry>>
        get() = mutableEntries.asStateFlow()

    private constructor(parcel: Parcel) : this(
        top = parcel.readParcelable<ParcelUuid>(BackStackImpl::class.java.classLoader)?.uuid,
        allOpRefs = parcel.readParcelableArray(BackStackImpl::class.java.classLoader)
            ?.filterIsInstance<ParcelableEntry>()
            .orEmpty()
            .associate { (uuid, opRef) -> uuid to opRef }
            .toMutableMap()
    )

    override fun describeContents() = 0

    override fun goBack() {
        top?.let { allOpRefs[it] }?.holderUUID?.holderResolver()?.doGoBack()
    }

    /**
     * Restores the state identified by the back operation [uuid], unwinding every intermediate back operation
     * along the way.
     *
     * Starting from the top of the back stack, operations are inspected one by one until the one identified by
     * [uuid] is reached (the "target" operation, and the holder it belongs to the "target" holder). For each
     * intermediate operation:
     * - If the holder it belongs to is part of the target holder's hierarchy (i.e. the target holder can be
     *   reached by following [ViewStateHolderImpl.parentHolder] references from it), the operation is discarded
     *   without invoking its back [ViewIntent].
     * - Otherwise, the operation is left untouched and the walk continues to the next one down the stack.
     *
     * Once the target operation is reached, it is removed and its back [ViewIntent] is invoked.
     *
     * The whole back stack manipulation happens synchronously; only the final back-intent dispatch may involve
     * asynchronous work, and it only happens after every other adjustment has completed.
     */
    override fun goBackTo(uuid: UUID) {
        val operationIds = operationIdsFromTopTo(uuid)
        val targetHolderId = operationIds.lastOrNull()?.let(::holderIdFor) ?: return
        val targetHolder = targetHolderId.holderResolver() ?: return

        for(operationId in operationIds) {
            val holder = holderIdFor(operationId)?.let(holderResolver) ?: continue

            if(operationId == uuid) {
                holder.doGoBack(operationId)
            } else if(isWithinHierarchy(holder, targetHolder)) {
                holder.removeOperation(operationId)
            }
        }
    }

    /**
     * Returns the [ViewStateHolderImpl.globalId] of the holder that owns the operation identified by [uuid],
     * or `null` if no such operation is currently on the stack.
     */
    override fun holderIdFor(uuid: UUID): UUID? = allOpRefs[uuid]?.holderUUID

    /**
     * Returns the operation UUIDs ordered from the top of the stack down to and including [uuid].
     * Returns an empty list if [uuid] is not currently present on the stack.
     */
    override fun operationIdsFromTopTo(uuid: UUID): List<UUID> {
        val result = mutableListOf<UUID>()
        for(id in idsFromTop()) {
            result += id
            if(id == uuid) return result
        }
        return emptyList()
    }

    override fun push(holder: ViewStateHolder, operationUuid: UUID) {
        val opRef = OpRef(holder.globalId, top)
        allOpRefs[operationUuid] = opRef
        top?.let { allOpRefs[it]?.up = operationUuid }
        top = operationUuid
        refreshPublishers()
    }

    /**
     * Removes operation assuming the holder did it already on its side
     */
    override fun removeOperation(uuid: UUID) {
        allOpRefs.remove(uuid)?.let { opRef ->
            if(top === uuid) top = opRef.down
            opRef.down?.let { allOpRefs[it]?.up = opRef.up }
            opRef.up?.let { allOpRefs[it]?.down = opRef.down }
            refreshPublishers()
        }
    }

    /**
     * Yields operation UUIDs ordered from the top of the stack down to the bottom.
     */
    private fun idsFromTop(): Sequence<UUID> = generateSequence(top) { allOpRefs[it]?.down }

    /**
     * Resolves each entry's [ViewIntent] on demand from the owning holder's own local operations list rather
     * than keeping a second copy of it here, so every [ViewIntent] is parceled exactly once (as part of its
     * owning [ViewStateHolderImpl]'s state). An operation is omitted if its holder can no longer be resolved.
     */
    private fun computeEntries(): List<BackStackEntry> = idsFromTop().mapNotNull { id ->
        val holder = allOpRefs[id]?.holderUUID?.holderResolver() ?: return@mapNotNull null
        holder.goBackIntentFor(id)?.let { viewIntent -> BackStackEntryImpl(id, viewIntent) }
    }.toList()

    private fun refreshPublishers() {
        stackSizePublisher.value = allOpRefs.size
        mutableEntries.value = computeEntries()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeParcelable(top?.let { ParcelUuid(it) }, flags)
            writeParcelableArray(allOpRefs.entries.map { (uuid, opRef) -> ParcelableEntry(uuid, opRef) }.toTypedArray(), flags)
        }
    }

    /**
     * Returns `true` if [target] is reachable from [holder] by following [ViewStateHolderImpl.parentHolder]
     * references (including when [holder] *is* [target]), `false` if the root is reached first.
     */
    private fun isWithinHierarchy(holder: InternalViewStateHolder, target: InternalViewStateHolder): Boolean {
        var current: InternalViewStateHolder? = holder
        while(current != null) {
            if(current === target) return true
            current = current.parentHolder as InternalViewStateHolder
        }
        return false
    }

    @Parcelize
    private data class ParcelableEntry(
        val uuid: UUID,
        val opRef: OpRef,
    ) : Parcelable

    private data class BackStackEntryImpl(
        override val uuid: UUID,
        override val viewIntent: ViewIntent,
    ) : BackStackEntry

    companion object CREATOR : Parcelable.Creator<BackStackImpl> {
        override fun createFromParcel(parcel: Parcel?): BackStackImpl? = parcel?.let { BackStackImpl(it) }
        override fun newArray(size: Int): Array<BackStackImpl?> = arrayOfNulls(size)
    }
}
