package com.xently.ui.demo

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher


enum class TextType { ERROR, HINT }

fun hasTextInputLayoutText(text: String, type: TextType = TextType.ERROR): Matcher<View> =
    object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("Shows a ${TextInputLayout::class.java.simpleName} error or hint text")
        }

        override fun matchesSafely(item: View?): Boolean {
            if (item == null) return false
            if (item is TextInputLayout) {
                return when (type) {
                    TextType.ERROR -> item.error == text
                    TextType.HINT -> item.hint == text
                }
            }
            return false
        }

    }

fun hasTextInputLayoutText(@StringRes text: Int, type: TextType = TextType.ERROR): Matcher<View> =
    object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("Shows a ${TextInputLayout::class.java.simpleName} error or hint text")
        }

        override fun matchesSafely(item: View?): Boolean {
            if (item == null) return false
            if (item is TextInputLayout) {
                val context = item.context
                return when (type) {
                    TextType.ERROR -> item.error == context.getString(text)
                    TextType.HINT -> item.hint == context.getString(text)
                }
            }
            return false
        }

    }

/**
 * Verify message shown on snackbar
 */
fun checkSnackBarDisplayedWithMessage(@StringRes message: Int) {
    onView(withText(message))
        .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
//    onView(withId(com.google.android.material.R.id.snackbar_text))
//        .check(matches(withText(message)))
}

/**
 * @see checkSnackBarDisplayedWithMessage
 */
fun checkSnackBarDisplayedWithMessage(message: String) {
    onView(withText(message))
        .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
//    onView(withId(com.google.android.material.R.id.snackbar_text))
//        .check(matches(withText(message)))
}

/**
 * Click on Snackbar action button with text [label]
 */
fun clickSnackbarActionButton(@StringRes label: Int) {
    onView(allOf(withId(com.google.android.material.R.id.snackbar_action), withText(label)))
        .perform(click())
}

/**
 * @see clickSnackbarActionButton
 */
fun clickSnackbarActionButton(label: String) {
    onView(allOf(withId(com.google.android.material.R.id.snackbar_action), withText(label)))
        .perform(click())
}

fun clickDialogButton(@StringRes label: Int) {
    onView(withText(label)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
}

fun clickDialogButton(label: String) {
    onView(withText(label)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
}

fun checkViewsDisplayed(vararg viewIds: Matcher<View>) {
    viewIds.forEach {
        onView(it).check(matches(isDisplayed()))
    }
}

fun checkViewsDisplayed(@IdRes vararg viewIds: Int) {
    viewIds.forEach {
        onView(withId(it)).check(matches(isDisplayed()))
    }
}

fun checkViewsNotDisplayed(vararg viewIds: Matcher<View>) {
    viewIds.forEach {
        onView(it).check(matches(not((isDisplayed()))))
    }
}

fun checkViewsNotDisplayed(@IdRes vararg viewIds: Int) {
    viewIds.forEach {
        onView(withId(it)).check(matches(not((isDisplayed()))))
    }
}

fun withAdaptedData(dataMatcher: Matcher<Any?>): Matcher<View?>? {
    return object : TypeSafeMatcher<View?>() {
        override fun describeTo(description: Description) {
            description.appendText("with class name: ")
            dataMatcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            if (view !is AdapterView<*>) return false
            val adapter: Adapter = view.adapter
            for (i in 0 until adapter.count) {
                if (dataMatcher.matches(adapter.getItem(i))) return true
            }
            return false
        }
    }
}

fun selectTabAtPosition(tabIndex: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription() = "with tab at index $tabIndex"

        override fun getConstraints() =
            allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

        override fun perform(uiController: UiController, view: View) {
            val tabLayout = view as TabLayout
            val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                ?: throw PerformException.Builder()
                    .withCause(Throwable("No tab at index $tabIndex"))
                    .build()

            tabAtIndex.select()
        }
    }
}

fun onViewsTypeText(views: Set<Matcher<View>>, vararg texts: String) {
    views.forEachIndexed { index, matcher ->
        onView(matcher).perform(typeText(texts[index]))
    }
}

fun clickTextInputLayoutEndIcon(listener: View.OnClickListener) = object : ViewAction {
    val clazz = TextInputLayout::class.java

    override fun getDescription(): String = "Clicks on ${clazz.simpleName}"

    override fun getConstraints(): Matcher<View> = isAssignableFrom(clazz)

    override fun perform(uiController: UiController?, view: View?) {
        if (view is TextInputLayout) {
            view.setEndIconOnClickListener(listener)
        }
    }
}

fun clickRecyclerViewItemWithId(@IdRes viewId: Int) = object : ViewAction {
    override fun getDescription(): String {
        return "description"
    }

    override fun getConstraints(): Matcher<View>? {
        return null
    }

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<View>(viewId)?.performClick()
    }
}

object RecyclerViewItemCount {

    fun incrementedBy(expectedIncrement: Int): ViewAssertion {
        return ViewAssertion { view, _ ->
            val adapter = (view as RecyclerView).adapter
            val itemCount = adapter!!.itemCount
            assertThat(itemCount, greaterThanOrEqualTo(itemCount - expectedIncrement))
        }
    }

    fun decrementedBy(expectedDecrement: Int): ViewAssertion {
        return ViewAssertion { view, _ ->
            val adapter = (view as RecyclerView).adapter
            val itemCount = adapter!!.itemCount
            assertThat(itemCount, lessThanOrEqualTo(itemCount + expectedDecrement))
        }
    }

    fun equalTo(expectedCount: Int): ViewAssertion {
        return ViewAssertion { view, _ ->
            val adapter = (view as RecyclerView).adapter
            val itemCount = adapter!!.itemCount
            assertThat(itemCount, Matchers.equalTo(expectedCount))
        }
    }

    fun greaterThan(expectedCount: Int): ViewAssertion {
        return ViewAssertion { view, _ ->
            val adapter = (view as RecyclerView).adapter
            val itemCount = adapter!!.itemCount
            assertThat(itemCount, Matchers.greaterThan(expectedCount))
        }
    }

    fun lessThan(expectedCount: Int): ViewAssertion {
        return ViewAssertion { view, _ ->
            val adapter = (view as RecyclerView).adapter
            val itemCount = adapter!!.itemCount
            assertThat(itemCount, Matchers.lessThan(expectedCount))
        }
    }
}