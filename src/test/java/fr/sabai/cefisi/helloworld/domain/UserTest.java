package fr.sabai.cefisi.helloworld.domain;

import fr.sabai.cefisi.helloworld.config.InitDBConfig;
import fr.sabai.cefisi.helloworld.config.JpaTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaTestConfig.class, InitDBConfig.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class UserTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void should_read_user_with_id_1() {
        // given
        long id = 1L;

        // when
        User user = entityManager.find(User.class, id);

        // then
        assertThat(user.getFirstName()).isEqualTo("James");
        assertThat(user.getLastName()).isEqualTo("Lawson");
        assertThat(user.getEmail()).isEqualTo("Phasellus@commodoipsum.ca");
        assertThat(user.getAge()).isEqualTo(35);
    }


    @Test
    public void should_save_new_user() {
        // given
        User newUser = new User();
        newUser.setFirstName("Luke");
        newUser.setLastName("Skywalker");
        newUser.setEmail("luke@skywalker.net");
        newUser.setAge(26);
        Integer rowBeforeSave = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);

        // when
        entityManager.persist(newUser);
        entityManager.flush();

        // then
        Integer rowAfterSave = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
        assertThat(rowAfterSave).isEqualTo(rowBeforeSave + 1);

    }

    @Test
    public void should_count_number_of_user() {
        // given
        String jpQuery = "select count(u.id) from User u";


        // when
        Long number = (Long) entityManager.createQuery(jpQuery).getSingleResult();

        // then
        assertThat(number).isEqualTo(100);

    }
}
