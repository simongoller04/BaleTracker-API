package com.simongoller.baleTrackerAPI.model.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsImpl(
    private val user: User
) : UserDetails {

    override fun getUsername(): String {
        return user.username
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptySet() // Replace this with your custom authorities, if any
    }

    override fun isAccountNonExpired(): Boolean {
        return true // Replace this with your custom account expiration check
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true // Replace this with your custom credentials expiration check
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true // Replace this with your custom account locking check
    }
}