import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gogoma.presentation.pager.PersonalFragment
import com.example.gogoma.presentation.pager.TeamFragment
import com.example.gogoma.presentation.pager.TeamRoadFragment

class MyPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalFragment()
            1 -> TeamFragment()
            2 -> TeamRoadFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}