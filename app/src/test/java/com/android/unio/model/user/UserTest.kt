package com.android.unio.model.user

import com.android.unio.model.association.AssociationRepositoryFirestore
import com.android.unio.model.firestore.FirestorePaths.ASSOCIATION_PATH
import com.android.unio.model.firestore.FirestoreReferenceList
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

class UserTest {
  @Mock private lateinit var db: FirebaseFirestore
  @Mock private lateinit var collectionReference: CollectionReference

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    `when`(db.collection(any())).thenReturn(collectionReference)
  }

  @Test
  fun testUser() {
    val user =
        User(
            "1",
            "John",
            "john@example.com",
            FirestoreReferenceList.empty(
                db.collection(ASSOCIATION_PATH), AssociationRepositoryFirestore::hydrate))
    assertEquals("1", user.uid)
    assertEquals("John", user.name)
    assertEquals("john@example.com", user.email)
  }
}
