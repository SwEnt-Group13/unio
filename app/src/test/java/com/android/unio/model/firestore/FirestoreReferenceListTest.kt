package com.android.unio.model.firestore

import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FirestoreReferenceListTest {

  @Mock private lateinit var mockCollection: CollectionReference
  @Mock private lateinit var mockSnapshot: DocumentSnapshot
  @Mock private lateinit var mockQuerySnapshot: QuerySnapshot
  @Mock private lateinit var mockTask: Task<QuerySnapshot>
  @Mock private lateinit var mockQuery: Query
  @Mock private lateinit var firestoreReferenceList: FirestoreReferenceList<String>

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)

    `when`(mockCollection.whereIn(eq(FieldPath.documentId()), any())).thenReturn(mockQuery)
    `when`(mockQuery.get()).thenReturn(mockTask)

    firestoreReferenceList =
        FirestoreReferenceList(mockCollection) { snapshot -> snapshot.getString("data") ?: "" }
  }

  @Test
  fun `test add does not request immediately`() {
    `when`(mockTask.addOnSuccessListener(any())).thenAnswer { invocation ->
      val callback = invocation.arguments[0] as OnSuccessListener<QuerySnapshot>
      callback.onSuccess(mockQuerySnapshot)
      mockTask
    }

    firestoreReferenceList.add("uid1")
    firestoreReferenceList.add("uid2")

    // Internal list of UIDs should now contain "uid1" and "uid2"
    assertEquals(0, firestoreReferenceList.list.value.size) // initial list should still be empty
  }

  @Test
  fun `test addAll does not request immediately`() {
    `when`(mockTask.addOnSuccessListener(any())).thenAnswer { invocation ->
      val callback = invocation.arguments[0] as OnSuccessListener<QuerySnapshot>
      callback.onSuccess(mockQuerySnapshot)
      mockTask
    }

    val uids = listOf("uid1", "uid2", "uid3")
    firestoreReferenceList.addAll(uids)

    assertEquals(0, firestoreReferenceList.list.value.size) // initial list should still be empty
  }

  @Test
  fun `test requestAll fetches documents and updates list`() = runTest {
    // Prepare mocks
    `when`(mockTask.addOnSuccessListener(any())).thenAnswer { invocation ->
      val callback = invocation.arguments[0] as OnSuccessListener<QuerySnapshot>
      callback.onSuccess(mockQuerySnapshot)
      mockTask
    }

    whenever(mockQuerySnapshot.documents).thenReturn(listOf(mockSnapshot, mockSnapshot))
    whenever(mockSnapshot.getString("data")).thenReturn("Item1", "Item2")

    // Add UIDs and call requestAll
    firestoreReferenceList.addAll(listOf("uid1", "uid2"))
    firestoreReferenceList.requestAll()

    // Assert that the list was updated correctly
    assertEquals(listOf("Item1", "Item2"), firestoreReferenceList.list.first())
    verify(mockQuery).get()
  }

  @Test
  fun `test requestAll clears list before updating`() = runTest {
    `when`(mockTask.addOnSuccessListener(any())).thenAnswer { invocation ->
      val callback = invocation.arguments[0] as OnSuccessListener<QuerySnapshot>
      callback.onSuccess(mockQuerySnapshot)
      mockTask
    }

    // Add initial UIDs
    firestoreReferenceList.addAll(listOf("uid1", "uid2"))

    // Request documents
    firestoreReferenceList.requestAll()

    // Assert that the list was cleared before updating
    assertEquals(0, firestoreReferenceList.list.value.size)
  }

  @Test
  fun `test fromList creates FirestoreReferenceList with UIDs`() = runTest {
    val list = listOf("uid1", "uid2")
    val fromList =
        FirestoreReferenceList.fromList(list, mockCollection) { snapshot ->
          snapshot.getString("data") ?: ""
        }

    assertEquals(0, fromList.list.value.size)
  }

  @Test
  fun `test empty creates FirestoreReferenceList without UIDs`() = runTest {
    val emptyList =
        FirestoreReferenceList.empty(mockCollection) { snapshot ->
          snapshot.getString("data") ?: ""
        }

    // Initial list should be empty
    assertEquals(0, emptyList.list.value.size)
  }
}
