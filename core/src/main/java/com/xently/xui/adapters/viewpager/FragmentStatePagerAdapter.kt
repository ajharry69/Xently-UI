package com.xently.xui.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Should be used when displaying more than 2 [Fragment]s in a view-pager otherwise use
 * [FragmentPagerAdapter]
 */
class FragmentStatePagerAdapter(
    private val fragmentList: Iterable<TitledFragment>,
    manager: FragmentManager
) : androidx.fragment.app.FragmentStatePagerAdapter(
    manager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    /*private var fragmentList: Iterable<TitledFragment> = emptyList()

    fun setFragmentList(fragmentList: Iterable<TitledFragment>) {
        this.fragmentList = fragmentList
        this.tabCount = this.fragmentList.count()
        notifyDataSetChanged()
    }*/

    private val tabCount
        get() = fragmentList.count()

    /**
     * Selects the middle or first [Fragment] as the default selected [Fragment] from a list
     * ([fragmentList]) of odd-numbered [Fragment]s and even-numbered [Fragment]s respectively.
     * For example, a view-pager supposed to display 3 fragments ([fragmentList] = 3) will have
     * it's default selected fragment position equal to 1(2nd [Fragment]) from the list whereas a
     * view-pager supposed to display 2 fragments ([fragmentList] = 2) will have it's default
     * selected fragment position equal to 0(1st [Fragment])
     */
    val middleFragmentPosition
        get() = middleFragmentPosition(tabCount)

    override fun getPageTitle(position: Int): CharSequence? =
        fragmentList.elementAt(position).title ?: super.getPageTitle(position)

    override fun getItem(position: Int): Fragment {

        return fragmentList.elementAt(position).fragment
    }

    override fun getCount(): Int = tabCount
}