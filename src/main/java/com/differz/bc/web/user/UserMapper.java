package com.differz.bc.web.user;

import com.differz.bc.core.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    UserDto mapToUserDto(User user);

}
