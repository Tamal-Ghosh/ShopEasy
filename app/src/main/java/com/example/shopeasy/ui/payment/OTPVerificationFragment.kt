package com.example.shopeasy.ui.payment

import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.shopeasy.databinding.FragmentOTPVerificationBinding
import com.example.shopeasy.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.example.shopeasy.R

class OTPVerificationFragment : Fragment() {

    private var _binding: FragmentOTPVerificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var generatedOtp: String
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOTPVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        insetsController.isAppearanceLightStatusBars = !isDarkTheme // dark icons for light theme


        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "user@example.com"
        binding.tvEmail.text = userEmail

        setupOtpBoxes()
        sendOtp()

        binding.btnConfirmOtp.setOnClickListener {
            val enteredOtp = getEnteredOtp()
            if (enteredOtp == generatedOtp) {
                Toast.makeText(requireContext(), "✅ Payment Successful", Toast.LENGTH_SHORT).show()
                // TODO: Navigate to success screen or clear cart
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment())
                    .addToBackStack(null) // Optional: remove if you don't want back press to return
                    .commit()

            } else {
                Toast.makeText(requireContext(), "❌ Invalid OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnResendOtp.setOnClickListener {
            sendOtp()
        }
    }

    private fun sendOtp() {
        generatedOtp = (100000..999999).random().toString()
        binding.tvOtpDisplay.text = "OTP: $generatedOtp"
        Toast.makeText(requireContext(), "OTP generated", Toast.LENGTH_SHORT).show()
        binding.btnResendOtp.isEnabled = false
        startCountdown()
    }

    private fun startCountdown() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdown.text = "Resend in ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                binding.btnResendOtp.isEnabled = true
                binding.tvCountdown.text = ""
            }
        }.start()
    }

    private fun getEnteredOtp(): String {
        return listOf(
            binding.etDigit1.text.toString(),
            binding.etDigit2.text.toString(),
            binding.etDigit3.text.toString(),
            binding.etDigit4.text.toString(),
            binding.etDigit5.text.toString(),
            binding.etDigit6.text.toString()
        ).joinToString("")
    }

    private fun setupOtpBoxes() {
        val otpFields = listOf(
            binding.etDigit1, binding.etDigit2, binding.etDigit3,
            binding.etDigit4, binding.etDigit5, binding.etDigit6
        )

        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < otpFields.size - 1) {
                        otpFields[i + 1].requestFocus()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }

    companion object {
        fun newInstance(method: String, extra: String): OTPVerificationFragment {
            val fragment = OTPVerificationFragment()
            val args = Bundle()
            args.putString("method", method)
            args.putString("extra", extra)
            fragment.arguments = args
            return fragment
        }
    }
}