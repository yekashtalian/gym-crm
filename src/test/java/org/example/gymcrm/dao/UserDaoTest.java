package org.example.gymcrm.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Optional;
import org.example.gymcrm.dao.impl.UserDaoImpl;
import org.example.gymcrm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

  @Mock private EntityManager entityManager;

  @Mock private TypedQuery<User> query;

  @InjectMocks private UserDaoImpl userDao;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUsername("testUser");
  }

  @Test
  void testFindByUsername_WhenUserExists() {
    when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
    when(query.setParameter("username", "testUser")).thenReturn(query);
    when(query.getSingleResult()).thenReturn(user);

    Optional<User> result = userDao.findByUsername("testUser");

    assertThat(result).isPresent().contains(user);
    verify(query).setParameter("username", "testUser");
    verify(query).getSingleResult();
  }

  @Test
  void testFindByUsername_WhenUserDoesNotExist() {
    when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
    when(query.setParameter("username", "testUser")).thenReturn(query);
    when(query.getSingleResult()).thenThrow(NoResultException.class);

    Optional<User> result = userDao.findByUsername("testUser");

    assertThat(result).isEmpty();
    verify(query).setParameter("username", "testUser");
    verify(query).getSingleResult();
  }
}
