package com.ticc.webapiservice.service.implementation;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Map;

import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.MerchantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantAuthServiceImpl implements MerchantAuthService {

    private static final String ERROR="Error: {}";

    private final RSAPublicKey rsaPublicKey;

    private final RSAPrivateKey rsaPrivateKey;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final JwtDecoder jwtDecoder;

    private final MerchantService merchantService;


    @Override
    public String validateAndGenerated(String username, String password) {
        log.debug("validateAndGenerated: {}",username);
        UserDetails userDetails;
        try {
            userDetails=userDetailsService.loadUserByUsername(username);
            log.debug("UserDetails: {}",userDetails);
            if(passwordEncoder.matches(password, userDetails.getPassword()))
            {
                String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_MERCHANT");
                return generateClaims(username,Map.of("role",role));
            }
        } catch (Exception e) {
            log.error(ERROR, e.getMessage());
            throw new MerchantRequestNotValidException("Username or password is not valid");
        }
        return null;
    }

    @Override
    public MerchantResponseDTO decodeToken(String token) {
        log.debug("decodeToken: {}", token);
        try {
            String username = jwtDecoder.decode(token).getSubject();
            return merchantService.getMerchantByUsername(username);
            
        } catch (Exception e) {
            log.error(ERROR, e);
            throw new MerchantRequestNotValidException(e.getMessage());
        }
    }

    private String generateClaims(String subject, Map<String, String> claims) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        JWTCreator.Builder jBuilder = JWT.create()
        .withSubject(subject)
        .withExpiresAt(calendar.getTime())
        .withIssuedAt(Calendar.getInstance().getTime())
        .withIssuer("auth-ticc")
        .withClaim("claims", claims);

        claims.forEach(jBuilder::withClaim);
        return jBuilder.sign(Algorithm.RSA256(rsaPublicKey, rsaPrivateKey));

    }
    
}
