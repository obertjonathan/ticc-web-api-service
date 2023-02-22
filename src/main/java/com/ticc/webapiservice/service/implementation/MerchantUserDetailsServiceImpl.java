package com.ticc.webapiservice.service.implementation;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ticc.webapiservice.dto.MerchantPrincipal;
import com.ticc.webapiservice.entity.Merchant;
import com.ticc.webapiservice.repository.MerchantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantUserDetailsServiceImpl implements UserDetailsService{

    private final MerchantRepository merchantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("loadMerchantUserByUsername: {}", username);
        Merchant merchant = merchantRepository.findByUsername(username)
        .orElseThrow(()->new UsernameNotFoundException("Merchant username not found"));
        return MerchantPrincipal.of(merchant);
    }
    
}
