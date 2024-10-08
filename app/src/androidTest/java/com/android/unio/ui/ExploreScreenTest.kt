package com.android.unio.ui

import androidx.compose.ui.test.junit4.createComposeRule
import com.android.unio.model.association.AssociationType
import com.android.unio.ui.explore.getCategoryNameWithFirstLetterUppercase
import com.android.unio.ui.explore.getFilteredAssociationsByCategoryAndAlphabeticalOrder
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ExploreScreenTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testGetFilteredAssociationsByCategory() {
    val musicAssociations =
        getFilteredAssociationsByCategoryAndAlphabeticalOrder(AssociationType.MUSIC)
    assertEquals(1, musicAssociations.size)
    assertEquals("Musical", musicAssociations[0].association.acronym)

    val festivalAssociations =
        getFilteredAssociationsByCategoryAndAlphabeticalOrder(AssociationType.FESTIVALS)
    assertEquals(4, festivalAssociations.size)
    assertEquals("Artiphys", festivalAssociations[0].association.acronym)
  }

  @Test
  fun testGetCategoryNameWithFirstLetterUppercase() {
    val musicCategory = getCategoryNameWithFirstLetterUppercase(AssociationType.MUSIC)
    assertEquals("Music", musicCategory)

    val festivalCategory = getCategoryNameWithFirstLetterUppercase(AssociationType.FESTIVALS)
    assertEquals("Festivals", festivalCategory)
  }
}
