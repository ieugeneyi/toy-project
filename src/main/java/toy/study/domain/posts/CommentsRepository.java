package toy.study.domain.posts;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comments, Long> {

    @Query("SELECT c FROM Comments c WHERE c.posts.id=:postsId ORDER BY c.id DESC ")
    List<Comments> getCommentsByPostsId(@Param("postsId") Long postsId);

}
