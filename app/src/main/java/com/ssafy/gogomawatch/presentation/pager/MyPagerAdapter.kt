import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssafy.gogomawatch.presentation.pager.PersonalFragment
import com.ssafy.gogomawatch.presentation.pager.TeamFragment

class MyPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalFragment()
            1 -> TeamFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}