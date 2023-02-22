package com.ticc.webapiservice.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ticc.webapiservice.entity.Merchant;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data(staticConstructor = "of")
public class MerchantPrincipal implements UserDetails {
    private final Merchant merchant;

    public MerchantPrincipal(Merchant merchant) {
        log.debug("MerchantPrincipal({})", merchant);
        this.merchant = merchant;
    }

    @Override
    public String getPassword() {
        return merchant.getPassword();
    }

    @Override
    public String getUsername() {
        return merchant.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_MERCHANT"));
    }

}
