package io.github.bidar.web.rest.mapper;

import io.github.bidar.web.rest.dto.RelationDTO;

import io.github.bidar.domain.Relation;
import io.github.bidar.domain.User;
import org.mapstruct.*;

/**
 * Mapper for the entity Relation and its DTO RelationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RelationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    RelationDTO relationToRelationDTO(Relation relation);

    @Mapping(source = "userId", target = "user")
    Relation relationDTOToRelation(RelationDTO relationDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
