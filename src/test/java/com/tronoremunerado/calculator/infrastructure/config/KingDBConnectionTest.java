package com.tronoremunerado.calculator.infrastructure.config;

import com.tronoremunerado.calculator.domain.RankingType;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.RankingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("KingDBConnection Tests")
class KingDBConnectionTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private KingDBConnection kingDBConnection;

    private KingEntity validKingEntity;

    @BeforeEach
    void setUp() {
        validKingEntity = new KingEntity(
                "testUser",
                480, // 8 hours in minutes
                9600, // 20 days * 8 hours
                115200, // 12 months * 20 days * 8 hours
                new BigDecimal("120.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("28800.00"),
                100.0
        );
    }

    @Nested
    @DisplayName("SaveKing Tests")
    class SaveKingTests {

        @Test
        @DisplayName("Should save king successfully when valid entity is provided")
        void shouldSaveKingSuccessfully() {
            // Given
            when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
                    .thenReturn(1);

            // When & Then
            assertThatNoException().isThrownBy(() -> kingDBConnection.saveKing(validKingEntity));

            // Verify
            ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);

            verify(jdbcTemplate).update(sqlCaptor.capture(), paramsCaptor.capture());

            String capturedSql = sqlCaptor.getValue();
            MapSqlParameterSource capturedParams = paramsCaptor.getValue();

            assertThat(capturedSql).contains("INSERT INTO king");
            assertThat(capturedParams.getValue("username")).isEqualTo("testUser");
            assertThat(capturedParams.getValue("dailyMinutes")).isEqualTo(480);
            assertThat(capturedParams.getValue("monthlyMinutes")).isEqualTo(9600);
            assertThat(capturedParams.getValue("yearlyMinutes")).isEqualTo(115200);
            assertThat(capturedParams.getValue("dailyEarnings")).isEqualTo(new BigDecimal("120.00"));
            assertThat(capturedParams.getValue("monthlyEarnings")).isEqualTo(new BigDecimal("2400.00"));
            assertThat(capturedParams.getValue("yearlyEarnings")).isEqualTo(new BigDecimal("28800.00"));
            assertThat(capturedParams.getValue("dailyPercentage")).isEqualTo(100.0);
            assertThat(capturedParams.getValue("id")).isNotNull();
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when king entity is null")
        void shouldThrowExceptionWhenKingIsNull() {
            // When & Then
            assertThatThrownBy(() -> kingDBConnection.saveKing(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("KingEntity must not be null");

            verify(jdbcTemplate, never()).update(anyString(), any(MapSqlParameterSource.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when username is null")
        void shouldThrowExceptionWhenUsernameIsNull() {
            // Given
            validKingEntity.setUsername(null);

            // When & Then
            assertThatThrownBy(() -> kingDBConnection.saveKing(validKingEntity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Username must not be empty");

            verify(jdbcTemplate, never()).update(anyString(), any(MapSqlParameterSource.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when username is empty")
        void shouldThrowExceptionWhenUsernameIsEmpty() {
            // Given
            validKingEntity.setUsername("");

            // When & Then
            assertThatThrownBy(() -> kingDBConnection.saveKing(validKingEntity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Username must not be empty");

            verify(jdbcTemplate, never()).update(anyString(), any(MapSqlParameterSource.class));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when no rows are affected")
        void shouldThrowExceptionWhenNoRowsAffected() {
            // Given
            when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
                    .thenReturn(0);

            // When & Then
            assertThatThrownBy(() -> kingDBConnection.saveKing(validKingEntity))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Failed to save king. Expected 1 row to be inserted, but 0 rows were affected.");
        }

        @Test
        @DisplayName("Should throw IllegalStateException when multiple rows are affected")
        void shouldThrowExceptionWhenMultipleRowsAffected() {
            // Given
            when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
                    .thenReturn(2);

            // When & Then
            assertThatThrownBy(() -> kingDBConnection.saveKing(validKingEntity))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Failed to save king. Expected 1 row to be inserted, but 2 rows were affected.");
        }

        @Test
        @DisplayName("Should throw RuntimeException when DataAccessException occurs")
        void shouldThrowRuntimeExceptionWhenDataAccessExceptionOccurs() {
            // Given
            DataAccessException dataAccessException = mock(DataAccessException.class);
            when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
                    .thenThrow(dataAccessException);

            // When & Then
            assertThatThrownBy(() -> kingDBConnection.saveKing(validKingEntity))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to save king data")
                    .hasCause(dataAccessException);
        }
    }

    @Nested
    @DisplayName("GetKingdomStatistics Tests")
    class GetKingdomStatisticsTests {

        @Test
        @DisplayName("Should return kingdom statistics successfully")
        void shouldReturnKingdomStatisticsSuccessfully() throws SQLException {
            // Given
            when(resultSet.getInt(1)).thenReturn(5); // totalKings
            when(resultSet.getInt(2)).thenReturn(576000); // totalYearlyMinutesSpent
            when(resultSet.getBigDecimal(3)).thenReturn(new BigDecimal("144000.00")); // totalYearlyEarnings
            when(resultSet.getInt(4)).thenReturn(480); // maxDailyMinutesSpent

            when(jdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenAnswer(invocation -> {
                        RowMapper<KingdomEntity> mapper = invocation.getArgument(2);
                        return mapper.mapRow(resultSet, 1);
                    });

            // When
            KingdomEntity result = kingDBConnection.getKingdomStatistics();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotalKings()).isEqualTo(5);
            assertThat(result.getTotalYearlyMinutesSpent()).isEqualTo(576000);
            assertThat(result.getTotalYearlyEarnings()).isEqualTo(new BigDecimal("144000.00"));
            assertThat(result.getMaxDailyMinutesSpent()).isEqualTo(480);

            ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).queryForObject(sqlCaptor.capture(), any(MapSqlParameterSource.class), any(RowMapper.class));

            String capturedSql = sqlCaptor.getValue();
            assertThat(capturedSql).contains("SELECT count(id), sum(yearly_minutes_spent), sum(yearly_earnings), MAX(daily_minutes_spent)");
            assertThat(capturedSql).contains("FROM king");
        }

        @Test
        @DisplayName("Should throw RuntimeException when DataAccessException occurs")
        void shouldThrowRuntimeExceptionWhenDataAccessExceptionOccurs() {
            // Given
            DataAccessException dataAccessException = mock(DataAccessException.class);
            when(jdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenThrow(dataAccessException);

            // When & Then
            assertThatThrownBy(() -> kingDBConnection.getKingdomStatistics())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to retrieve kingdom statistics")
                    .hasCause(dataAccessException);
        }

        @Test
        @DisplayName("Should handle null result from database query")
        void shouldHandleNullResultFromDatabaseQuery() {
            // Given
            when(jdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenReturn(null);

            // When & Then
            KingdomEntity result = kingDBConnection.getKingdomStatistics();
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("SQL Query Tests")
    class SqlQueryTests {

        @Test
        @DisplayName("Should use correct SQL for insert operation")
        void shouldUseCorrectSqlForInsert() {
            // Given
            when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
                    .thenReturn(1);

            // When
            kingDBConnection.saveKing(validKingEntity);

            // Then
            ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).update(sqlCaptor.capture(), any(MapSqlParameterSource.class));

            String capturedSql = sqlCaptor.getValue();
            assertThat(capturedSql).containsIgnoringWhitespaces("INSERT INTO king");
            assertThat(capturedSql).contains("id, username, daily_minutes_spent, monthly_minutes_spent, yearly_minutes_spent");
            assertThat(capturedSql).contains("daily_earnings, monthly_earnings, yearly_earnings, daily_percentage_of_shift");
            assertThat(capturedSql).contains(":id, :username, :dailyMinutes, :monthlyMinutes, :yearlyMinutes");
            assertThat(capturedSql).contains(":dailyEarnings, :monthlyEarnings, :yearlyEarnings, :dailyPercentage");
        }

        @Test
        @DisplayName("Should use correct SQL for statistics query")
        void shouldUseCorrectSqlForStatistics() throws SQLException {
            // Given
            when(resultSet.getInt(anyInt())).thenReturn(1);
            when(resultSet.getBigDecimal(anyInt())).thenReturn(BigDecimal.ONE);

            when(jdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenAnswer(invocation -> {
                        RowMapper<KingdomEntity> mapper = invocation.getArgument(2);
                        return mapper.mapRow(resultSet, 1);
                    });

            // When
            kingDBConnection.getKingdomStatistics();

            // Then
            ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).queryForObject(sqlCaptor.capture(), any(MapSqlParameterSource.class), any(RowMapper.class));

            String capturedSql = sqlCaptor.getValue();
            assertThat(capturedSql).containsIgnoringWhitespaces("SELECT count(id), sum(yearly_minutes_spent), sum(yearly_earnings), MAX(daily_minutes_spent)");
            assertThat(capturedSql).containsIgnoringWhitespaces("FROM king");
        }
    }

    @Nested
    @DisplayName("GetRanking Tests")
    class GetRankingTests {

        @Test
        @DisplayName("Should get ranking successfully for HIGHER_EARNINGS")
        void shouldGetRankingSuccessfullyForHigherEarnings() throws SQLException {
            // Given
            List<RankingEntity> expectedRanking = createMockRankingList();
            
            when(resultSet.getString("username")).thenReturn("king1", "king2");
            when(resultSet.getInt("daily_minutes_spent")).thenReturn(120, 100);
            when(resultSet.getBigDecimal("daily_earnings")).thenReturn(BigDecimal.valueOf(50.0), BigDecimal.valueOf(45.0));
            
            when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenAnswer(invocation -> {
                        RowMapper<RankingEntity> mapper = invocation.getArgument(2);
                        List<RankingEntity> result = new ArrayList<>();
                        result.add(mapper.mapRow(resultSet, 1));
                        result.add(mapper.mapRow(resultSet, 2));
                        return result;
                    });

            // When
            List<RankingEntity> result = kingDBConnection.getRanking(RankingType.HIGHER_EARNINGS);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getUsername()).isEqualTo("king1");
            assertThat(result.get(0).getDailyMinutesSpent()).isEqualTo(120);
            assertThat(result.get(0).getDailyEarnings()).isEqualTo(BigDecimal.valueOf(50.0));

            // Verify SQL construction
            ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), any(MapSqlParameterSource.class), any(RowMapper.class));
            
            String capturedSql = sqlCaptor.getValue();
            assertThat(capturedSql).contains("SELECT username, daily_minutes_spent, daily_earnings");
            assertThat(capturedSql).contains("FROM king");
            assertThat(capturedSql).contains("order by daily_earnings desc, username asc limit 5;");
        }

        @Test
        @DisplayName("Should get ranking successfully for HIGHER_MINUTES")
        void shouldGetRankingSuccessfullyForHigherMinutes() throws SQLException {
            // Given
            when(resultSet.getString("username")).thenReturn("speedKing");
            when(resultSet.getInt("daily_minutes_spent")).thenReturn(180);
            when(resultSet.getBigDecimal("daily_earnings")).thenReturn(BigDecimal.valueOf(30.0));
            
            when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenAnswer(invocation -> {
                        RowMapper<RankingEntity> mapper = invocation.getArgument(2);
                        return Collections.singletonList(mapper.mapRow(resultSet, 1));
                    });

            // When
            List<RankingEntity> result = kingDBConnection.getRanking(RankingType.HIGHER_MINUTES);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getUsername()).isEqualTo("speedKing");
            assertThat(result.get(0).getDailyMinutesSpent()).isEqualTo(180);

            // Verify SQL construction
            ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), any(MapSqlParameterSource.class), any(RowMapper.class));
            
            String capturedSql = sqlCaptor.getValue();
            assertThat(capturedSql).contains("order by daily_minutes_spent desc, username asc limit 5;");
        }

        @Test
        @DisplayName("Should get ranking successfully for LOWER_MINUTES")
        void shouldGetRankingSuccessfullyForLowerMinutes() throws SQLException {
            // Given
            when(resultSet.getString("username")).thenReturn("efficientKing");
            when(resultSet.getInt("daily_minutes_spent")).thenReturn(30);
            when(resultSet.getBigDecimal("daily_earnings")).thenReturn(BigDecimal.valueOf(15.0));
            
            when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenAnswer(invocation -> {
                        RowMapper<RankingEntity> mapper = invocation.getArgument(2);
                        return Collections.singletonList(mapper.mapRow(resultSet, 1));
                    });

            // When
            List<RankingEntity> result = kingDBConnection.getRanking(RankingType.LOWER_MINUTES);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getUsername()).isEqualTo("efficientKing");
            assertThat(result.get(0).getDailyMinutesSpent()).isEqualTo(30);

            // Verify SQL construction
            ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), any(MapSqlParameterSource.class), any(RowMapper.class));
            
            String capturedSql = sqlCaptor.getValue();
            assertThat(capturedSql).contains("order by daily_minutes_spent asc, username asc limit 5;");
        }

        @Test
        @DisplayName("Should return empty list when no ranking data found")
        void shouldReturnEmptyListWhenNoRankingDataFound() {
            // Given
            when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenReturn(Collections.emptyList());

            // When
            List<RankingEntity> result = kingDBConnection.getRanking(RankingType.HIGHER_EARNINGS);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should throw RuntimeException when DataAccessException occurs during ranking query")
        void shouldThrowRuntimeExceptionWhenDataAccessExceptionOccursduringRankingQuery() {
            // Given
            when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenThrow(new DataAccessException("Database connection failed") {});

            // When & Then
            assertThatThrownBy(() -> kingDBConnection.getRanking(RankingType.HIGHER_EARNINGS))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Failed to retrieve ranking");
        }

        @Test
        @DisplayName("Should use correct column names in ranking SQL")
        void shouldUseCorrectColumnNamesInRankingSQL() {
            // Given
            when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenReturn(Collections.emptyList());

            // When
            kingDBConnection.getRanking(RankingType.HIGHER_EARNINGS);

            // Then
            ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), any(MapSqlParameterSource.class), any(RowMapper.class));
            
            String capturedSql = sqlCaptor.getValue();
            assertThat(capturedSql).contains("username");
            assertThat(capturedSql).contains("daily_minutes_spent");
            assertThat(capturedSql).contains("daily_earnings");
        }

        @Test
        @DisplayName("Should handle all ranking types correctly")
        void shouldHandleAllRankingTypesCorrectly() {
            // Given
            when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                    .thenReturn(Collections.emptyList());

            // When & Then - Test all enum values
            for (RankingType type : RankingType.values()) {
                assertThatNoException().isThrownBy(() -> kingDBConnection.getRanking(type));
            }
        }

        private List<RankingEntity> createMockRankingList() {
            List<RankingEntity> ranking = new ArrayList<>();
            
            RankingEntity entity1 = new RankingEntity();
            entity1.setUsername("king1");
            entity1.setDailyMinutesSpent(120);
            entity1.setDailyEarnings(BigDecimal.valueOf(50.0));
            ranking.add(entity1);
            
            RankingEntity entity2 = new RankingEntity();
            entity2.setUsername("king2");
            entity2.setDailyMinutesSpent(100);
            entity2.setDailyEarnings(BigDecimal.valueOf(45.0));
            ranking.add(entity2);
            
            return ranking;
        }
    }
}
