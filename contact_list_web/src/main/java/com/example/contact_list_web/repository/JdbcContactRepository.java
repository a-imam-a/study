package com.example.contact_list_web.repository;

import com.example.contact_list_web.Contact;
import com.example.contact_list_web.exception.ContactNotFoundException;
import com.example.contact_list_web.repository.mapper.ContactRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcContactRepository implements ContactRepository {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Contact> findAll() {
        log.debug("Calling JdbcContactRepository->findAll");

        String sql = "SELECT * FROM contacts";
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }

    @Override
    public Optional<Contact> findById(Long id) {
        log.debug("Calling JdbcContactRepository->findById with ID: {}", id);

        String sql = "SELECT * FROM contacts WHERE id = ?";
        Contact contact = DataAccessUtils.singleResult(
                jdbcTemplate.query(
                        sql,
                        new ArgumentPreparedStatementSetter(new Object[] {id}),
                        new RowMapperResultSetExtractor<>(new ContactRowMapper(), 1)
                )
        );
        return Optional.ofNullable(contact);
    }

    @Override
    public Contact save(Contact contact) {
        log.debug("Calling JdbcContactRepository->save with contact: {}", contact);

        contact.setId(System.currentTimeMillis());
        jdbcTemplate.update(queryForInsert(),
                contact.getId(), contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone());

        return contact;
    }

    @Override
    public Contact update(Contact contact) {
        log.debug("Calling JdbcContactRepository->update with contact: {}", contact);

        Contact existedContact = findById(contact.getId()).orElse(null);
        if (existedContact != null) {
            String sql = "UPDATE contacts SET first_name = ?, last_name = ?, email = ? , phone = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone(), contact.getId());

            return contact;
        }
        log.debug("Task with ID {} not found", contact.getId());
        throw new ContactNotFoundException("Task for update not found! ID: " + contact.getId());
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Calling JdbcContactRepository->deleteById with id: {}", id);
        String sql = "DELETE  FROM contacts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void batchInsert(List<Contact> contacts) {
        log.debug("Calling JdbcContactRepository->batchInsert");

        jdbcTemplate.batchUpdate(queryForInsert(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Contact contact = contacts.get(i);
                ps.setLong(1, contact.getId());
                ps.setString(2, contact.getFirstName());
                ps.setString(3, contact.getLastName());
                ps.setString(4, contact.getEmail());
                ps.setString(5, contact.getPhone());
            }

            @Override
            public int getBatchSize() {
                return contacts.size();
            }
        });
    }

    private String queryForInsert() {
        return "INSERT INTO contacts (id, first_name, last_name, email, phone) VALUES (?, ?, ?, ?, ?)";
    }
}
