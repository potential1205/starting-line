package com.example.gogoma.data.util

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "UserDistanceRepository"

data class UserData(
    val distance: Int = 0,
    val currentTime: Int = 0
)

object UserDistanceRepository {
    private val database = FirebaseDatabase.getInstance("https://gogomarathon-b07af-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val marathonRef = database.getReference("users")

    // 사용자의 초기 데이터를 생성하는 메서드 (예: 누적거리 0, 시작 시간 등)
    fun createInitialUserData(
        userId: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val initialData = UserData(distance = 0, currentTime = 0)
        marathonRef.child(userId.toString()).setValue(initialData)
            .addOnSuccessListener {
                Log.d(TAG, "User $userId 초기 데이터 생성 성공: $initialData")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "User $userId 초기 데이터 생성 실패", exception)
                onFailure(exception)
            }
    }

    // 누적 거리를 조회하는 메서드
    fun getUserCumulativeDistance(userId: Int, onResult: (Int?) -> Unit) {
        marathonRef.child(userId.toString()).child("distance").get()
            .addOnSuccessListener { snapshot ->
                val distance = snapshot.getValue(Int::class.java)
                //Log.d(TAG, "User $userId 누적 거리 조회 성공: $distance")
                onResult(distance)
            }
            .addOnFailureListener { exception ->
                //Log.e(TAG, "User $userId 누적 거리 조회 실패", exception)
                onResult(null)
            }
    }

    // 누적 거리를 업데이트하는 메서드
    fun updateUserCumulativeDistance(
        userId: Int,
        newCumulativeDistance: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        marathonRef.child(userId.toString()).child("distance").setValue(newCumulativeDistance)
            .addOnSuccessListener {
                Log.d(TAG, "User $userId 누적 거리 업데이트 성공: $newCumulativeDistance")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "User $userId 누적 거리 업데이트 실패", exception)
                onFailure(exception)
            }
    }

    // 누적 시간을 업데이트하는 메서드
    fun updateUserCurrentTime(
        userId: Int,
        newCurrentTime: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        marathonRef.child(userId.toString()).child("currentTime").setValue(newCurrentTime)
            .addOnSuccessListener {
                Log.d(TAG, "User $userId 누적 거리 업데이트 성공: $newCurrentTime")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "User $userId 누적 거리 업데이트 실패", exception)
                onFailure(exception)
            }
    }

    // 시간 조회
    // 누적 거리를 조회하는 메서드
    fun getUserCurrentTime(userId: Int, onResult: (Int?) -> Unit) {
        marathonRef.child(userId.toString()).child("currentTime").get()
            .addOnSuccessListener { snapshot ->
                val currentTime = snapshot.getValue(Int::class.java)
                //Log.d(TAG, "User $userId 누적 거리 조회 성공: $distance")
                onResult(currentTime)
            }
            .addOnFailureListener { exception ->
                //Log.e(TAG, "User $userId 누적 거리 조회 실패", exception)
                onResult(null)
            }
    }

    // (선택사항) 사용자의 전체 데이터를 삭제하는 메서드
    fun deleteUserData(
        userId: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        marathonRef.child(userId.toString()).removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "User $userId 데이터 삭제 성공")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "User $userId 데이터 삭제 실패", exception)
                onFailure(exception)
            }
    }
}
