package io.github.bidar.repository;

import io.github.bidar.domain.Relation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Relation entity.
 */
public interface RelationRepository extends JpaRepository<Relation,Long> {

    @Query("select relation from Relation relation where relation.user.login = ?#{principal.username}")
    List<Relation> findByUserIsCurrentUser();

    Relation findByIdAndUser_Login(Long id, String login);

    Page<Relation> findByUser_Login(String login, Pageable pageable);

    //List<Relation> findByTitleOr(String query);

    @Query("select relation from Relation relation where (relation.user.login = ?#{principal.username}) and (UPPER(relation.title) like UPPER(?1) or UPPER(relation.author) like UPPER(?2) or UPPER(relation.code) like UPPER(?3) or UPPER(relation.date)=UPPER(?4) or UPPER(relation.testimonial) like UPPER(?5))")
    List<Relation> search(String title, String author, String code, String date, String testimonial);
}
