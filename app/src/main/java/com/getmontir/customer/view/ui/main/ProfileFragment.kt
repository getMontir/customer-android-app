package com.getmontir.customer.view.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.getmontir.customer.R
import com.getmontir.customer.data.ProfileMenuItem
import com.getmontir.customer.databinding.FragmentMainProfileBinding
import com.getmontir.customer.view.adapter.ProfileMenuAdapter
import com.getmontir.customer.view.ui.base.GetFragment

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ProfileFragment.
         */
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    private lateinit var binding: FragmentMainProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainProfileBinding.inflate( inflater, container, false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        binding.toolbar.setupWithNavController( findNavController() )

        // Setup view
        //binding.imgAvatar.setImageURI( Uri.parse(sessionManager.userImage) )
        binding.txtUserName.text = sessionManager.userName
        binding.txtUserEmail.text = sessionManager.userEmail

        val items: Array<ProfileMenuItem> = arrayOf(
            ProfileMenuItem( "profile", "Ubah Profile", R.drawable.ic_profile_change),
            ProfileMenuItem( "password", "Ubah Kata Sandi", R.drawable.ic_profile_change_password),
            ProfileMenuItem( "about", "Tentang getMontir", R.drawable.ic_profile_about),
            ProfileMenuItem( "terms", "Syarat & Ketentuan", R.drawable.ic_profile_terms),
            ProfileMenuItem( "privacy", "Kebijakan Privasi", R.drawable.ic_profile_privacy),
            ProfileMenuItem( "rate", "Beri Bintang", R.drawable.ic_profile_rate),
            ProfileMenuItem( "support", "Bantuan", R.drawable.ic_profile_support),
        )
        val adapter = ProfileMenuAdapter( items, object: ProfileMenuAdapter.ProfileMenuCallback {
            override fun onItemClicked(tag: String) {
                //Toast.makeText(requireContext(), tag, Toast.LENGTH_SHORT).show()
            }
        })
        binding.listMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.listMenu.adapter = adapter

        // Setup listener
        binding.btnLogOut.setOnClickListener {  }
    }
}