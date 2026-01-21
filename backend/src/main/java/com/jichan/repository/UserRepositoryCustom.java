package com.jichan.repository;

import com.jichan.dto.ProfileDto;
import com.jichan.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserRepositoryCustom {
    Slice<User> findProfiles(ProfileDto.ProfileRequest profileRequest, Pageable pageable);
}
