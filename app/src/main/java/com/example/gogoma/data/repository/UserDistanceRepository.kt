package com.example.gogoma.data.repository

import com.google.firebase.database.FirebaseDatabase

data class UserData(
    val distance: Int = 0,
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
        // 예시 데이터: 누적거리 0
        val initialData = UserData(distance = 0)
        marathonRef.child(userId.toString()).setValue(initialData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // 누적 거리를 조회하는 메서드
    fun getUserCumulativeDistance(userId: Int, onResult: (Int?) -> Unit) {
        marathonRef.child(userId.toString()).child("distance").get()
            .addOnSuccessListener { snapshot ->
                val distance = snapshot.getValue(Int::class.java)
                onResult(distance)
            }
            .addOnFailureListener { onResult(null) }
    }

    // 누적 거리를 업데이트하는 메서드
    fun updateUserCumulativeDistance(
        userId: Int,
        newCumulativeDistance: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        marathonRef.child(userId.toString()).child("distance").setValue(newCumulativeDistance)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // (선택사항) 사용자의 전체 데이터를 삭제하는 메서드
    fun deleteUserData(
        userId: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        marathonRef.child(userId.toString()).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}
