package org.example.gymcrm.indicator;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;

@ExtendWith(MockitoExtension.class)
public class DatabaseHealthIndicatorTest {

  @Mock private DataSource dataSource;

  @Mock private Connection connection;

  @Mock private DatabaseMetaData metaData;

  @Test
  public void testHealthUp() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.getMetaData()).thenReturn(metaData);
    when(metaData.getURL()).thenReturn("jdbc:h2:mem:testdb");

    DatabaseHealthIndicator databaseHealthIndicator = new DatabaseHealthIndicator(dataSource);

    // Act
    Health health = databaseHealthIndicator.health();

    // Assert
    assert health.getStatus() == Health.up().build().getStatus();
    assert health.getDetails().get("database").equals("Available");
    assert health.getDetails().get("database url").equals("jdbc:h2:mem:testdb");
  }

  @Test
  public void testHealthDown() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

    DatabaseHealthIndicator databaseHealthIndicator = new DatabaseHealthIndicator(dataSource);

    // Act
    Health health = databaseHealthIndicator.health();

    // Assert
    assert health.getStatus() == Health.down().build().getStatus();
    assert health.getDetails().get("database").equals("Unavailable");
    assert health.getDetails().get("error").equals("Connection failed");
  }
}
