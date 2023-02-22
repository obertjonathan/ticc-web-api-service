package com.ticc.webapiservice.service.implementation;

import java.util.List;

import java.util.UUID;

import com.ticc.webapiservice.exception.exts.merchant.MerchantAlreadyExistsException;
import com.ticc.webapiservice.exception.exts.merchant.MerchantNotFoundException;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import com.ticc.webapiservice.util.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ticc.webapiservice.dto.request.MerchantAddRequestDTO;
import com.ticc.webapiservice.dto.response.EventResponseDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.entity.Event;
import com.ticc.webapiservice.entity.Merchant;
import com.ticc.webapiservice.repository.MerchantRepository;
import com.ticc.webapiservice.service.MerchantService;
import com.ticc.webapiservice.util.PasswordValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantServiceImpl implements MerchantService{

    private final MerchantRepository merchantRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MerchantResponseDTO getMerchantByUsername(String username) {
        Merchant merchant = merchantRepository.findByUsername(username)
        .orElseThrow(()-> new MerchantNotFoundException("Merchant with specific username not found"));
        return convertToDTO(merchant);
    }

    private MerchantResponseDTO convertToDTO(Merchant merchant) {
        return modelMapper.map(merchant, MerchantResponseDTO.class);
    }

    private EventResponseDTO convertToDTOEvent(Event event) {
        return modelMapper.map(event, EventResponseDTO.class);
    }

    private Merchant convertToEntity(MerchantAddRequestDTO merchantAddRequestDTO)
    {
        return modelMapper.map(merchantAddRequestDTO, Merchant.class);
    }

    @Override
    public MerchantResponseDTO addMerchant(MerchantAddRequestDTO merchantAddRequestDTO) {
        try {
            if(merchantRepository.findByUsername(merchantAddRequestDTO.getUsername()).isPresent()) {
                throw new MerchantAlreadyExistsException("Merchant with specific Username is already exist");
            }

            if(merchantRepository.findByEmail(merchantAddRequestDTO.getEmail()).isPresent()) {
                throw new MerchantAlreadyExistsException("Merchant with specific Email is already exist");
            }

            if(!PasswordValidator.validatePassword(merchantAddRequestDTO.getPassword())) {
                throw new MerchantRequestNotValidException("Password must be 8 to 16 characters, "
                + "contain at least one uppercase letter, "
                + "contain at least one lowercase letter, "
                + "contain at least one number digit, "
                + "contain at least one special character, "
                + "and not contain any whitespace");
            }

            if (!EmailValidator.validateEmail(merchantAddRequestDTO.getEmail())) {
                throw new MerchantRequestNotValidException("Invalid email format");
            }

            merchantAddRequestDTO.setPassword(convertPassword(merchantAddRequestDTO.getPassword()));
        } catch (Exception e) {
            throw new MerchantRequestNotValidException(e.getMessage());
        }
        Merchant merchant = convertToEntity(merchantAddRequestDTO);
        merchant.setId((UUID.randomUUID()).toString());
        merchantRepository.save(merchant);
        return convertToDTO(merchant);
    }

    private String convertPassword(String password)
    {
        return passwordEncoder.encode(password);
    }

    @Override
    public List<EventResponseDTO> getEventByMerchant(String username) {
        Merchant merchant = merchantRepository.findByUsername(username).orElseThrow(() -> new MerchantNotFoundException("Merchant not found"));
        return merchant.getEvents().stream().filter(e -> e.getDeletedAt() == null).map(this::convertToDTOEvent).toList();

    }
}
